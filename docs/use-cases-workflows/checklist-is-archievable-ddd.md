# Checklist: Verantwortlichkeiten im Use-Case **isArchivable** (DDD-konform)

> Diese kompakte Checkliste definiert klar, welche Logik in **Domain**, **Service** oder **Orchestrator** gehört – speziell für den Use-Case **„TaskList ist archivierbar?“**.
> Sie dient als wiederverwendbarer Leitfaden für zukünftige Use-Cases im Tasks-Projekt.

---

## 1. Domain Layer (TaskList Aggregate Root)
**Fachliche Regeln gehören immer in die Domain.**

### ✔ Gehört in die Domain
- Prüfung, ob eine TaskList archivierbar ist  
- Zugriff auf Tasks innerhalb des Aggregates  
- Fachliche Regel: *„Alle Tasks müssen abgeschlossen sein“*

### Beispiel
```java
public boolean isArchievable() {
    return tasks.stream().allMatch(Task::isCompleted);
}
```

### ❌ Gehört NICHT in die Domain
- Repository-Zugriffe
- Laden oder Speichern von Entities
- Technische Fehlerbehandlung
- Logging

---

## 2. Service Layer (TaskListService)
**Der Service bildet die Persistence-Boundary des Aggregates.**

### ✔ Gehört in den Service
- Laden einer TaskList: `getTaskListOrThrow(UUID id)`
- Speichern einer TaskList: `save(TaskList list)`
- Repository-Filter wie `findByStatus(ACTIVE)` oder `findByStatus(ARCHIVED)`

### ❌ Gehört NICHT in den Service
- Fachliche Regeln wie „archivierbar?“
- Iteration über Tasks
- Prüfung von Task-Zuständen
- Orchestrierung von Use-Cases

---

## 3. Orchestrator Layer
**Der Orchestrator koordiniert den Use-Case, führt aber keine Domain-Logik aus.**

### ✔ Gehört in den Orchestrator
- TaskList laden
- Domain-Methode aufrufen: `taskList.isArchievable()`
- Ergebnis zurückgeben
- Transaktion öffnen (`@Transactional(readOnly = true)`)
- Logging auf Use-Case-Ebene

### ❌ Gehört NICHT in den Orchestrator
- Fachliche Regeln implementieren
- Tasks einzeln laden
- TaskService als Dependency
- Repository-Zugriffe

---

## 4. Kurzfassung (Merksatz)

| Ebene | Verantwortung | Beispiel                        |
|-------|---------------|---------------------------------|
| **Domain** | Fachliche Regeln | `taskList.isArchievable()`      |
| **Service** | Persistence-Boundary | `taskListRepository.findById()` |
| **Orchestrator** | Use-Case-Ablauf | `return list.isArchievable()`   |

---

## 5. Warum diese Aufteilung ideal ist
- **SRP** wird eingehalten
- **Testbarkeit** ist maximal (Domain-Methoden isoliert testbar)
- **DDD-Regeln** werden sauber umgesetzt
- **Lazy Loading** funktioniert korrekt innerhalb der Orchestrator-Transaktion
- **Keine unnötigen Abhängigkeiten** (z.B. TaskService im TaskListService)
- **Keine doppelte Logik**

---

Diese Checkliste ist bewusst kompakt gehalten und dient als wiederverwendbarer Leitfaden für alle zukünftigen Use-Cases im Tasks-Projekt.


---

[//]: # (Wenn du willst, kann ich dir auch:)

[//]: # ()
[//]: # (- eine **zweite Checkliste** für den nächsten Use‑Case erstellen  )

[//]: # (- eine **komplette DDD‑Guideline** für dein Projekt bauen  )

[//]: # (- oder wir gehen weiter zum nächsten Endpoint, den du prüfen willst.)

[//]: # ()
[//]: # (Sag einfach Bescheid.)