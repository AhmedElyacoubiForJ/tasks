# ✅ ✅ ✅ **📘 FINALER PATCH-TASK‑FLOW (kompakt)**

```text
HTTP Request (PATCH)
        ↓
Controller (ITaskListsTasksApi)
        ↓
Orchestrator (TaskListsTaskOrchestratorImpl)
        ↓
TaskListService.getTaskListOrThrow(taskListId)
        ↓
TaskService.getTaskOrThrow(taskId)
        ↓
TaskUpdater.applyPatch(dto)        ← nur gesetzte Felder aktualisieren
        ↓
Domain-Methoden (Task.changeXyz())
        ↓
TaskService.updateTask(task)       ← Persistieren + Mapping
        ↓
TaskTransformer.TASK_TO_SUMMARY    ← Domain → DTO
        ↓
APIResponse<TaskSummaryDto>
        ↓
HTTP Response (200 OK)

```