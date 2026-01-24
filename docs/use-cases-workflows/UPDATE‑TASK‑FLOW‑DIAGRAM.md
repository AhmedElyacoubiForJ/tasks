# ✅ ✅ ✅ **📘 FINALER UPDATE_TASK‑FLOW (kompakt)**

```text
HTTP Request (PUT / PATCH)
        ↓
Controller (ITaskListsTasksApi)
        ↓
Orchestrator (TaskListsTaskOrchestratorImpl)
        ↓
TaskListService.getTaskListOrThrow(taskListId)
        ↓
TaskService.getTaskOrThrow(taskId)
        ↓
TaskUpdater.applyFullUpdate(dto)   ← PUT
oder
TaskUpdater.applyPatch(dto)        ← PATCH
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