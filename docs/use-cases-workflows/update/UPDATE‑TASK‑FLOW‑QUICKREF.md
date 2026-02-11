[//]: # (docs/use-cases-workflows/UPDATE‑TASK‑FLOW‑QUICKREF.md)

# 📄 **UPDATE‑TASK‑FLOW‑QUICKREF**

Kurzübersicht des vollständigen Full‑Update‑Flows (PUT) für bestehende Tasks.  
Optimiert für Entwickler, die schnell Verantwortlichkeiten und Reihenfolge erfassen möchten.

---

## 1. Controller (ITaskListsTasksApi)

- Nimmt **PUT‑Request** entgegen  
- Extrahiert `taskListId`, `taskId`, `FullUpdateTaskDto`  
- Delegiert an Orchestrator  
- Baut `APIResponse<TaskSummaryDto>`  
- **Keine Business‑Logik**

---

## 2. Orchestrator (TaskListsTaskOrchestratorImpl)

1. **TaskList laden**  
   → `taskListService.getTaskListOrThrow(taskListId)`

2. **Archivierungsregel prüfen**  
   → `taskList.isArchived()`  
   → Fehler, wenn archiviert

3. **Task laden**  
   → `taskService.getTaskOrThrow(taskId)`

4. **Zugehörigkeit prüfen**  
   → `task.getTaskList().getId().equals(taskListId)`

5. **Update anwenden**  
   → `taskUpdater.applyFullUpdate(task, dto)`

6. **Persistieren**  
   → `taskService.updateTask(task)`

7. **Ergebnis zurückgeben**  
   → `TaskSummaryDto`

---

## 3. TaskUpdater

Technische Klasse für DTO → Domain‑Mapping.  
**Keine Business‑Regeln.**

### Setzt alle Felder (PUT), aber nur wenn geändert:

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
- **Keine Setter**, nur Methoden wie:

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
- Transaktion → `@Transactional`
- Mapping →  
  `TransformerUtil.transform(TaskTransformer.TASK_TO_SUMMARY, saved)`
- **Keine Business‑Logik**

---

## 6. Repository

- Reiner Datenbankzugriff
- Keine Logik

---

## TL;DR (Too Long; Didn’t Read)

**PUT‑Update‑Flow:**  
Controller  
→ Orchestrator  
→ TaskListService  
→ TaskService (load)  
→ TaskUpdater  
→ Domain  
→ TaskService (save)  
→ Transformer  
→ Response