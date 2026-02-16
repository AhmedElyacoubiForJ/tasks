### 🧩 High-Level-Schichten

```text
[ Client / Frontend ]
          │
          ▼
+----------------------+
|    REST Controller   |
+----------------------+
          │
          ▼
+----------------------+
|     Orchestrator     |  (Application Layer)
+----------------------+
          │
          ▼
+----------------------+
|   Domain (Aggregat)  |
|  TaskList + Tasks    |
+----------------------+
          │
          ▼
+----------------------+
|   Service + Repo     |  (Persistence Boundary)
+----------------------+
          │
          ▼
     [ Datenbank ]
```

---

### 🔍 Detail: Request-Fluss „TaskList archivieren“

```text
HTTP PUT /tasklists/{id}/archive
                │
                ▼
+-----------------------------------------+
| REST Controller                         |
| - validiert Request (technisch)         |
| - ruft orchestrator.archiveTaskList(id) |
+-----------------------------------------+
                │
                ▼
+-----------------------------------------+
| Orchestrator                            |
| - taskList = taskListService.get...     |
| - taskList.archive()                    |
| - taskListService.save(taskList)        |
+-----------------------------------------+
                │
                ▼
+-----------------------------------------+
| Domain: TaskList                        |
| - archive():                            |
|   - if (status == ARCHIVED) return      |
|   - if (!isArchivable()) throw ...      |
|   - status = ARCHIVED                   |
|   - updated = now                       |
| - private isArchivable():               |
|   - tasks.stream().allMatch(isCompleted)|
+-----------------------------------------+
                │
                ▼
+-----------------------------------------+
| TaskListService                         |
| - getTaskListOrThrow(id)                |
| - save(taskList) → repository.save(...) |
+-----------------------------------------+
                │
                ▼
+---------------------------+
| TaskListRepository (JPA) |
+---------------------------+
                │
                ▼
           [ DB: task_lists, tasks ]
```

---

### 🎯 Verantwortlichkeiten im Überblick

```text
Controller:
  - kennt DTOs
  - kennt Orchestrator
  - kennt keine Domain-Regeln

Orchestrator:
  - kennt Services
  - kennt Domain-Methoden
  - kennt keine DTOs
  - kennt keine Regeln

Domain:
  - kennt Regeln
  - kennt Invarianten
  - kennt keine DTOs
  - kennt keine Controller/Orchestrator/Services

Service:
  - kennt Repository
  - kennt Domain-Entities
  - kennt keine Regeln
  - kennt keine DTOs
```