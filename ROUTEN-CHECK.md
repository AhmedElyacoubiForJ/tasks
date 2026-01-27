**Welche REST‑Routen wir bereits *vollständig* geprüft haben – vom Request → Orchestrator/Service → Domain → Repository → Response.**

Ich gehe also durch **alle Routen**, die in deinem Projekt existieren (basierend auf allem, was wir gemeinsam gebaut, refaktoriert oder besprochen haben) und markiere:

- **DONE** → vollständig geprüft
- **NOT CHECKED** → noch offen
- **PARTIALLY** → teilweise geprüft, aber nicht end‑to‑end

Damit hast du sofort eine klare Roadmap, ohne dass du alles manuell durchsuchen musst.

---

# ✅ **1. TaskList CRUD API**

### **GET /tasklists**
→ *Alle TaskLists abrufen*  
**Status: DONE**  
Wir haben Logs gesehen, Response geprüft, Transformer geprüft.

---

### **GET /tasklists/{id}**
→ *Eine TaskList abrufen*  
**Status: NOT CHECKED**  
Wir haben diese Route noch nicht end‑to‑end getestet.

---

### **POST /tasklists**
→ *Neue TaskList erstellen*  
**Status: PARTIALLY**  
Wir haben den Service geprüft (CreateTaskListDto → Service → Repository),  
aber **keinen echten REST‑Call** getestet.

---

### **PUT /tasklists/{id}**
→ *TaskList aktualisieren (Titel/Beschreibung/Status)*  
**Status: NOT CHECKED**  
Wir haben zwar UpdateTaskListDto erweitert,  
aber die Route selbst noch nicht getestet.

---

### **DELETE /tasklists/{id}**
→ *TaskList löschen*  
**Status: NOT CHECKED**  
Wir haben diese Route nie angesprochen.

---

# 🎯 **2. TaskList Scenario API**

### **PUT /tasklists/{id}/archive**
→ *Archivieren, wenn alle Tasks abgeschlossen sind*  
**Status: DONE**  
Wir haben:
- Orchestrator geprüft
- Domain‑Regeln geprüft
- REST‑Route finalisiert
- Transformer geprüft
- Logs gesehen  
  → vollständig getestet.

---

### **PUT /tasklists/{id}/activate**
→ *TaskList wieder aktivieren*  
**Status: NOT CHECKED**  
Wir haben diese Route noch nicht implementiert oder getestet.

---

# 📝 **3. Task API**

### **GET /tasklists/{id}/tasks**
→ *Alle Tasks einer TaskList abrufen*  
**Status: NOT CHECKED**

---

### **POST /tasklists/{id}/tasks**
→ *Task erstellen*  
**Status: PARTIALLY**  
Wir haben TaskService.createTask(Task) geprüft,  
aber **nicht die REST‑Route**.

---

### **GET /tasks/{taskId}**
→ *Einzelnen Task abrufen*  
**Status: NOT CHECKED**

---

### **PUT /tasks/{taskId}**
→ *Task aktualisieren (Titel, Beschreibung, Status, DueDate, Priority)*  
**Status: NOT CHECKED**

---

### **DELETE /tasks/{taskId}**
→ *Task löschen*  
**Status: NOT CHECKED**

---

### **PUT /tasklists/{listId}/tasks/{taskId}/move**
→ *Task in eine andere TaskList verschieben*  
**Status: NOT CHECKED**  
Wir haben diesen UseCase noch nicht gebaut.

---

# 🔥 **4. Zusammenfassung – Was ist DONE?**

| Route | Status |
|-------|--------|
| GET /tasklists | **DONE** |
| PUT /tasklists/{id}/archive | **DONE** |

Alles andere ist entweder **NOT CHECKED** oder **PARTIALLY**.

---

# 🚀 Empfehlung: Was wir als Nächstes tun sollten

Wenn du willst, können wir jetzt:

### Option A — **Alle CRUD‑Routen der TaskList prüfen**
→ GET /tasklists/{id}  
→ POST /tasklists  
→ PUT /tasklists/{id}  
→ DELETE /tasklists/{id}

### Option B — **Task‑Routen prüfen**
→ POST /tasklists/{id}/tasks  
→ GET /tasklists/{id}/tasks  
→ GET /tasks/{taskId}  
→ PUT /tasks/{taskId}  
→ DELETE /tasks/{taskId}

### Option C — **Scenario‑Routen erweitern**
→ /tasklists/{id}/activate  
→ /tasklists/{id}/deactivate  
→ /tasklists/{id}/tasks/{taskId}/move

---

Sag mir einfach, **welche Route wir als Nächstes end‑to‑end prüfen sollen**,  
und wir machen sie sofort **DONE**.