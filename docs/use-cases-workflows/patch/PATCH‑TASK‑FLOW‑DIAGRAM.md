[//]: # (docs/use-cases-workflows/PATCH‑TASK‑FLOW‑DIAGRAM.md)

# ✅ PATCH‑TASK‑FLOW (kompakt)

Der folgende Ablauf beschreibt den vollständigen **Partial‑Update‑Use‑Case (PATCH)** für:

```
PATCH /tasklists/{taskListId}/tasks/{taskId}
```

Er zeigt den gesamten Weg eines partiellen Updates durch alle Schichten:

- Controller  
- Orchestrator  
- Domain  
- Updater  
- Persistence  
- Mapping  
- Response  

---

# 📘 PATCH‑TASK‑FLOW‑DIAGRAM
```text
Controller  
    ↓  
Orchestrator.patchTaskInList()  
    ↓  
TaskListService.getTaskListOrThrow()  
    ↓  
TaskList.isArchived() → Domain‑Regel  
    ↓  
TaskService.getTaskOrThrow()  
    ↓  
Zugehörigkeitsprüfung  
    ↓  
TaskUpdater.applyPatch()  
    ↓  
(nur Felder ≠ null)  
Task.changeTitle()  
Task.changeDescription()  
Task.changeDueDate()  
Task.changePriority()  
Task.changeStatus()  
    ↓  
TaskService.updateTask()    #
    ↓  
taskRepository.save(task)  
    ↓  
TransformerUtil.transform()  
    ↓  
TaskSummaryDto  
    ↓  
Controller → APIResponse  
```
---

# 📝 Hinweise

- **Controller**: nimmt PATCH‑DTO entgegen, delegiert, baut APIResponse.  
- **Orchestrator**: führt den gesamten Use‑Case aus (DDD‑Use‑Case‑Koordinator).  
- **TaskListService**: lädt Aggregat‑Root und prüft Archivierungsregel.  
- **TaskService**: lädt Task und persistiert Änderungen.  
- **TaskUpdater**: setzt nur Felder, die im DTO nicht null sind.  
- **Domain (Task)**: erzwingt Status‑Transitions, Validierungen, setzt Timestamp.  
- **Transformer**: wandelt Domain‑Entity in API‑DTO um.  

---

# 🟩 Ergebnis

Der PATCH‑Flow ist:

- DDD‑konform  
- technisch sauber  
- klar getrennt nach Verantwortlichkeiten  
- vollständig atomar (durch @Transactional im Service)  
- stabil und wartbar