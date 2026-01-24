                          ┌──────────────────────────┐
                          │        Controller        │
                          │  updateTaskInList(...)   │
                          └─────────────┬────────────┘
                                        │
                                        ▼
                          ┌──────────────────────────┐
                          │       Orchestrator       │
                          │  updateTaskInList(...)   │
                          └─────────────┬────────────┘
                                        │
        ┌───────────────────────────────┼────────────────────────────────┐
        │                               │                                │
        ▼                               ▼                                ▼
┌──────────────────┐        ┌──────────────────┐              ┌──────────────────┐
│ TaskListService  │        │   TaskService    │              │   TaskUpdater    │
│ getTaskListOr... │        │ getTaskOrThrow() │              │ applyFullUpdate()│
└──────────┬───────┘        └──────────┬───────┘              │ applyPatch()     │
           │                           │                      └──────────┬───────┘
           │                           │                                 │
           ▼                           ▼                                 ▼
┌──────────────────┐        ┌──────────────────┐              ┌──────────────────┐
│    TaskList      │        │       Task       │              │   Domain-Methoden│
│ isArchived()?    │        │ changeTitle()    │              │ changeStatus()   │
│                  │        │ changeDueDate()  │              │ changePriority() │
└──────────────────┘        │ changeDescription│              │ ...              │
                            └──────────┬───────┘              └──────────┬───────┘
                                       │                                 │
                                       ▼                                 ▼
                            ┌──────────────────┐              ┌──────────────────┐
                            │   TaskService    │              │   Repository     │
                            │  updateTask()    │────────────► │  save(task)      │
                            └──────────────────┘              └──────────────────┘

                                        ▼
                           ┌──────────────────────────┐
                           │   TaskTransformer        │
                           │  Task → TaskSummaryDto   │
                           └─────────────┬────────────┘
                                         │
                                         ▼
                           ┌──────────────────────────┐
                           │     APIResponse<T>       │
                           └──────────────────────────┘
