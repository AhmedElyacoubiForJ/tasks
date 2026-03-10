## 🧩 Kompaktversion – nur Endpunkt‑Pfade, gruppiert nach API‑Interfaces

### **ITaskListsCrudApi**
```
POST    /tasklists
GET     /tasklists
GET     /tasklists/{id}
PUT     /tasklists/{id}
PATCH   /tasklists/{id}
DELETE  /tasklists/{id}
```

### **ITaskListsTasksCrudApi**
```
POST    /tasklists/{id}/tasks
GET     /tasklists/{id}/tasks
GET     /tasklists/{id}/tasks/{taskId}
PUT     /tasklists/{id}/tasks/{taskId}
PATCH   /tasklists/{id}/tasks/{taskId}
DELETE  /tasklists/{id}/tasks/{taskId}
```

### **ITaskListsUseCaseApi**
```
POST    /tasklists/{id}/tasks/{taskId}/complete
POST    /tasklists/{id}/tasks/{taskId}/reopen
POST    /tasklists/{id}/archive
POST    /tasklists/{id}/restore
```
