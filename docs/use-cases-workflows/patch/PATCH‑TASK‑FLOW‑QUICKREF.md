# 📄 **PATCH‑TASK‑FLOW‑QUICKREF**

Kurzübersicht des partiellen Update‑Flows (PATCH) für bestehende Tasks.  
Optimiert für Entwickler, die schnell Verantwortlichkeiten und Reihenfolge erfassen möchten.

---

## 1. Controller (ITaskListsTasksApi)
- Nimmt PATCH‑Request entgegen  
- Extrahiert `taskListId`, `taskId`, `PatchTaskDto`  
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
4. Patch anwenden  
   → `taskUpdater.applyPatch(task, dto)`
5. Persistieren  
   → `taskService.updateTask(task)`
6. Ergebnis zurückgeben  
   → `TaskSummaryDto`

---

## 3. TaskUpdater
Technische Klasse für DTO → Domain‑Mapping.  
Keine Business‑Regeln.

### Aktualisiert nur gesetzte Felder (PATCH):
```
if (dto.title() != null)       task.changeTitle(dto.title());
if (dto.description() != null) task.changeDescription(dto.description());
if (dto.dueDate() != null)     task.changeDueDate(dto.dueDate());
if (dto.priority() != null)    task.changePriority(dto.priority());
if (dto.status() != null)      task.changeStatus(dto.status());
```

---

## 4. Domain (Task)
- Erzwingt fachliche Regeln  
- Setzt `updated` Timestamp  
- Validiert Titel, Beschreibung, DueDate  
- Erzwingt Status‑Transitions  
- Keine Setter, nur Methoden wie:

```
changeTitle()
changeDescription()
changeDueDate()
changePriority()
changeStatus()
```

---

## 5. TaskService
- Persistiert Task → `taskRepository.save(task)`
- Transaktion → @Transactional
- Logging  
- Mapping → `TaskSummaryDto`  
- Fehlerbehandlung (z.B. Optimistic Locking) *TO-DO*  
- Keine Business‑Logik

---

## 6. Repository
- Reiner Datenbankzugriff  
- Keine Logik

---

## TL;DR (Too Long; Didn’t Read)
**PATCH‑Flow:**  
Controller
   → Orchestrator
   → TaskListService
   → TaskService (load)
   → TaskUpdater (nur gesetzte Felder)
   → Domain
   → TaskService (save)
   → Transformer
   → Response
