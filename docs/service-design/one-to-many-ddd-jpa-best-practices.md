# **One‑To‑Many – Best Practices für Implementierung (DDD + JPA)**

## **1. Überblick**

In Domain‑Driven Design (DDD) und JPA ist die Modellierung einer One‑To‑Many‑Beziehung ein zentraler Bestandteil vieler Aggregate.  
Ein klassisches Beispiel ist:

```
TaskList 1  ----  * Task
```

Dieses Dokument erklärt:

- Wer **Owner** der Beziehung sein sollte
- Wie man **DDD‑Regeln** sauber abbildet
- Wie man **JPA‑Best Practices** einhält
- Welche Rolle **Orchestrator**, **Services** und **Domain** spielen
- Warum `addTask()` optional ist, aber trotzdem sinnvoll bleibt

---

## **2. JPA‑Best Practice: Die Many‑Seite ist der Owner**

In JPA ist die **Many‑Seite** fast immer der Owner der Beziehung.

### **Warum?**

- Der Foreign Key (`task_list_id`) liegt in der `tasks`‑Tabelle
- Persistenz ist einfacher
- Kein doppeltes Synchronisieren nötig
- Die One‑Seite (TaskList) bleibt schlank
- Task kann unabhängig persistiert werden

### **Technische Konsequenz**

Der Owner ist:

```java
@ManyToOne
@JoinColumn(name = "task_list_id")
private TaskList taskList;
```

Die One‑Seite ist nur die inverse Seite:

```java
@OneToMany(mappedBy = "taskList")
private List<Task> tasks;
```

---

## **3. DDD‑Best Practice: Aggregat‑Root schützt Regeln, nicht Beziehungen**

DDD unterscheidet klar zwischen:

- **DDD‑Owner** (Aggregat‑Root)
- **JPA‑Owner** (FK‑Besitzer)

In unserem Fall:

| Rolle | Klasse |
|-------|--------|
| **DDD‑Owner** | TaskList |
| **JPA‑Owner** | Task |

Das ist **kein Widerspruch**, sondern ein sehr verbreitetes Muster.

---

## **4. Domain‑Regel im Aggregat**

Die wichtigste Regel lautet:

> „Eine archivierte TaskList darf keine neuen Tasks annehmen.“

Diese Regel gehört **in die Domain**, nicht in Services oder Orchestrator.

```java
public void assertCanAddTask() {
    if (this.isArchived()) {
        throw new IllegalStateException(
            "Kann keinen Task zu einer archivierten TaskList hinzufügen."
        );
    }
}
```

---

## **5. Warum `addTask()` optional ist**

Deine Domain enthält:

```java
public void addTask(Task task) {
    if (task == null) {
        throw new IllegalArgumentException("Task darf nicht null sein.");
    }
    tasks.add(task);
    this.updated = LocalDateTime.now();
}
```

Diese Methode ist **DDD‑korrekt**, aber in deinem aktuellen Architektur‑Stil **nicht zwingend notwendig**.

### **Warum?**

Weil du den folgenden Stil nutzt:

- Task ist JPA‑Owner
- TaskFactory setzt `task.taskList = taskList`
- TaskService speichert Task
- JPA persistiert die Beziehung automatisch

Damit ist `addTask()` **nicht erforderlich**, aber:

### **Warum behalten wir sie trotzdem?**

- Sie dokumentiert die Aggregatsgrenzen
- Sie ist nützlich für zukünftige Use‑Cases (Bulk‑Add, Reordering, Events)
- Sie macht das Modell vollständig
- Sie erlaubt später einen Wechsel zu Stil A

---

## **6. Zwei mögliche DDD‑Stile**

### **Stil A — TaskList ist Domain‑Owner (klassisches DDD)**

- TaskList.addTask(task) wird genutzt
- TaskListService speichert TaskList
- TaskService speichert keine Tasks
- TaskList ist einziger Einstiegspunkt

### **Stil B — Task ist technischer Owner (dein aktueller Stil)**

- TaskFactory setzt TaskList
- TaskService speichert Task
- Orchestrator koordiniert
- TaskList schützt nur Regeln

**Beide Stile sind korrekt.**  
**Stil B ist in der Praxis einfacher und stabiler.**

---

## **7. Finaler Flow (Stil B – empfohlen)**

```
Controller
    ↓
Orchestrator (Use‑Case)
    ↓
TaskListService (lädt Aggregat)
    ↓
TaskList.assertCanAddTask()  ← Domain‑Regel
    ↓
TaskFactory.create(dto, taskList)
    ↓
TaskService.createTask(task)  ← Persistence‑Service
    ↓
TaskRepository.save(task)
```

---

## **8. Fazit**

- Die Many‑Seite (Task) ist der technische Owner → **Best Practice**
- TaskList bleibt Aggregat‑Root → **DDD‑konform**
- Domain‑Regeln gehören in TaskList → **sauber**
- Orchestrator koordiniert den Use‑Case → **klar getrennt**
- TaskService speichert Tasks → **SRP‑konform**
- `addTask()` ist optional, aber sinnvoll → **Domain‑Vollständigkeit**

Das Modell ist damit **stark, sauber und absolut DDD‑konform**.