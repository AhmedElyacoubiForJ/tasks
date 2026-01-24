# 📄 **UPDATE‑TASK‑FLOW‑QUICKREF**

Kurzübersicht des vollständigen Update‑Flows für bestehende Tasks.  
Optimiert für Entwickler, die schnell Verantwortlichkeiten und Reihenfolge erfassen möchten.

---

## 1. Controller (ITaskListsTasksApi)
- Nimmt PUT‑Request entgegen  
- Extrahiert `taskListId`, `taskId`, `UpdateTaskDto`  
- Delegiert an Orchestrator  
- Baut `APIResponse<TaskSummaryDto>`  
- Keine Business‑Logik

---

## 2. Orchestrator (TaskListsTaskOrchestratorImpl)
1. TaskList laden  
   → `taskListService.getTaskListOrThrow(taskListId)`
2. Task laden  
   → `taskService.getTaskOrThrow(taskId)`
3. Zugehörigkeit prüfen  
   → Task gehört zur TaskList?
4. Update anwenden  
   → `taskUpdater.applyFullUpdate(task, dto)`
5. Persistieren  
   → `taskService.updateTask(task)`
6. Ergebnis zurückgeben  
   → `TaskSummaryDto`

---

## 3. TaskUpdater
Technische Klasse für DTO → Domain‑Mapping.  
Keine Business‑Regeln.

### Setzt alle Felder (PUT):
```text
task.changeTitle(dto.title());
task.changeDescription(dto.description());
task.changeDueDate(dto.dueDate());
task.changePriority(dto.priority());
task.changeStatus(dto.status());
```

---

## 4. Domain (Task)
- Erzwingt fachliche Regeln  
- Setzt `updated` Timestamp  
- Validiert Titel, Beschreibung, DueDate  
- Erzwingt Status‑Transitions  
- Keine Setter, nur Methoden wie:

```text
changeTitle()
changeDescription()
changeDueDate()
changePriority()
changeStatus()
```

---

## 5. TaskService
- Persistiert Task → `taskRepository.save(task)`  
- Logging  
- Mapping → `TaskSummaryDto`  
- Fehlerbehandlung (z.B. Optimistic Locking)  
- Keine Business‑Logik

---

## 6. Repository
- Reiner Datenbankzugriff  
- Keine Logik

---

## TL;DR (Too Long; Didn’t Read)
**PUT‑Update‑Flow:**  
Controller → Orchestrator → TaskListService → TaskService (load) → TaskUpdater → Domain → TaskService (save) → Transformer → Response

