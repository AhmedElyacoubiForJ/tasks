# ✅ UPDATE TASK FLOW (Full Update)
```text
PUT /tasklists/{taskListId}/tasks/{taskId}
```

Dieses Diagramm zeigt den vollständigen End‑to‑End‑Ablauf eines Full Updates
(FullUpdateTaskDto → Domain → Persistenz → APIResponse).

---

## 📘 Flow Diagram
```text
Controller
    ↓
Orchestrator.updateTaskInList()
    ↓
TaskListService.getTaskListOrThrow()
    ↓
TaskList.isArchived() → Domain-Regel
    ↓
TaskService.getTaskOrThrow()
    ↓
Zugehörigkeitsprüfung (Task gehört zur TaskList?)
    ↓
TaskUpdater.applyFullUpdate()
    ↓
Task.changeTitle()
Task.changeDescription()
Task.changeDueDate()
Task.changePriority()
Task.changeStatus()
    ↓
TaskService.updateTask()
    ↓
taskRepository.save(task)
    ↓
TransformerUtil.transform(TaskTransformer.TASK_TO_SUMMARY)
    ↓
TaskSummaryDto
    ↓
Controller → APIResponse
```
---

## 📝 Hinweise

- **Controller**: nimmt DTO entgegen, delegiert, baut APIResponse.
- **Orchestrator**: führt den gesamten Use‑Case aus (DDD‑Use‑Case‑Koordinator).
- **TaskListService**: lädt Aggregat‑Root und prüft Archivierungsregel.
- **TaskService**: lädt Task und persistiert Änderungen.
- **TaskUpdater**: ruft ausschließlich Domain‑Methoden auf (keine Setter).
- **Task (Domain)**: prüft Status‑Transitionen und aktualisiert Felder.
- **Transformer**: wandelt Domain‑Entity in API‑DTO um.

---

## 🟩 Ergebnis

Der Full‑Update‑Flow ist:

- DDD‑konform
- technisch sauber
- klar getrennt nach Verantwortlichkeiten
- vollständig atomar (durch @Transactional im Service)
- stabil und wartbar  

