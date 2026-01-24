
# 📘 Analyse: Wohin gehört `activateTaskList(UUID id)`?
### *Entscheidungshilfe vor der Implementierung*

Diese Dokumentation beschreibt die Überlegungen, die notwendig sind, um zu entscheiden,  
ob die Methode `activateTaskList(UUID id)` in den **Aggregat‑Service** oder in den  
**Orchestrator‑Service** gehört.  
Die Entscheidung basiert vollständig auf den **Domain‑Regeln**.

---

# 1. 🎯 Grundregel (DDD)

> **Alles, was nur EIN Aggregat betrifft → gehört in den Aggregat‑Service.**  
> **Alles, was MEHRERE Aggregate betrifft → gehört in den Orchestrator.**

Diese Regel ist die Basis für jede Service‑Entscheidung im Domain‑Driven Design.

---

# 2. 🧠 Analyse des UseCases „TaskList aktivieren“

Um zu entscheiden, wohin die Methode gehört, muss die Domain‑Regel klar sein.

---

## 2.1 Variante A — Aktivieren betrifft **nur TaskList**
### Domain‑Regel:
> „Eine TaskList kann jederzeit wieder aktiviert werden.“

Das bedeutet:

- Es gibt **keine Abhängigkeit** zu Tasks
- Es gibt **keine Prüfung** über mehrere Aggregate
- Aktivieren ist ein **reiner Zustandswechsel** im Aggregat TaskList

### Ablauf:
- TaskList laden
- `taskList.activate()` ausführen
- speichern

### Entscheidung:
➡️ **Gehört in den TaskListService (Aggregat‑Service)**  
➡️ **Kein Orchestrator nötig**

Dies ist die **einfachste und häufigste** Variante.

---

## 2.2 Variante B — Aktivieren betrifft **TaskList + Tasks**
### Domain‑Regel:
> „Eine TaskList darf nur aktiviert werden, wenn alle Tasks abgeschlossen sind.“  
oder  
> „Beim Aktivieren müssen Tasks automatisch reaktiviert werden.“

Das bedeutet:

- Der UseCase betrifft **mehrere Aggregate**
- Es müssen **Tasks geladen und geprüft** werden
- Es entsteht **Cross‑Aggregate‑Logik**

### Ablauf:
1. TaskList laden
2. Tasks laden
3. Domain‑Regel prüfen
4. ggf. Tasks aktualisieren
5. TaskList aktivieren
6. speichern

### Entscheidung:
➡️ **Gehört in den Orchestrator**  
➡️ **Aggregat‑Service wäre überladen**

---

# 3. 🧩 Entscheidung für dieses Projekt (Tasks)

Die Domain‑Regel lautet:

> „Eine neue TaskList ist immer aktiv und kann jederzeit wieder aktiviert werden.“

Das bedeutet:

- Aktivieren betrifft **nur TaskList**
- Tasks spielen **keine Rolle**
- Es gibt **keine Cross‑Aggregate‑Regel**

### Finale Entscheidung:
➡️ **`activateTaskList(UUID id)` gehört in den TaskListService**  
➡️ **Keine Orchestrator‑Logik notwendig**

---

# 4. 🏁 Zusammenfassung

| Domain‑Regel | Betroffene Aggregate | Service | Begründung |
|--------------|----------------------|---------|------------|
| TaskList kann jederzeit aktiviert werden | nur TaskList | **TaskListService** | reiner Zustandswechsel |
| Aktivieren erfordert Prüfung der Tasks | TaskList + Tasks | **Orchestrator** | Cross‑Aggregate‑UseCase |
| Aktivieren reaktiviert Tasks | TaskList + Tasks | **Orchestrator** | mehrere Aggregate betroffen |

---

[//]: # (Wenn du möchtest, kann ich dir direkt:)

[//]: # ()
[//]: # (- die finale Implementierung)

[//]: # (- die Domain‑Methode `activate&#40;&#41;` prüfen)

[//]: # (- oder die Analyse für `archiveTaskList&#40;UUID id&#41;` erstellen)

[//]: # ()
[//]: # (Sag einfach Bescheid, wohin wir weitergehen.)