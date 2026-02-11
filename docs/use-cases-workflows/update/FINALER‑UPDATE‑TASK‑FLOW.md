[//]: # (docs/use-cases-workflows/FINALER‑UPDATE‑TASK‑FLOW.md)
# 📘 FINALER‑UPDATE‑TASK‑FLOW
*(PUT /tasklists/{taskListId}/tasks/{taskId})*

## Ziel
Dieser Flow beschreibt den vollständigen technischen und fachlichen Ablauf beim **vollständigen Aktualisieren (Full Update / PUT)** einer bestehenden Task innerhalb einer TaskList.  
Er dient als Referenz für Entwickler, um Verantwortlichkeiten, Datenflüsse und DDD‑Grenzen klar zu verstehen.

---

# 1. Eingangspunkt: REST‑Controller

Der Controller übernimmt ausschließlich:

- Entgegennahme der HTTP‑Anfrage
- Extraktion von `taskListId`, `taskId` und `FullUpdateTaskDto`
- Delegation an den Orchestrator
- Aufbau einer standardisierten `APIResponse<TaskSummaryDto>`
- Rückgabe eines `200 OK`

**Wichtig:**  
Der Controller enthält **keine Business‑Logik** und **keine Persistenzlogik**.

---

# 2. Orchestrator: Ablaufsteuerung (Use‑Case‑Koordinator)

Der Orchestrator koordiniert den gesamten Update‑Prozess.  
Er führt **keine Domain‑Logik** aus, sondern orchestriert Services und Domain‑Methoden.

## 2.1 TaskList laden
- `taskListService.getTaskListOrThrow(taskListId)`
- Fehler, wenn nicht vorhanden

## 2.2 Domain‑Regel prüfen
- `taskList.isArchived()`
- Fehler, wenn archiviert

## 2.3 Task laden
- `taskService.getTaskOrThrow(taskId)`
- Fehler, wenn nicht vorhanden

## 2.4 Zugehörigkeit prüfen
- Sicherstellen, dass `task.getTaskList().getId().equals(taskListId)`
- Fehler, wenn Task nicht zur TaskList gehört

## 2.5 Update anwenden
Der Orchestrator ruft **nicht** direkt Domain‑Methoden auf.  
Stattdessen delegiert er an den `TaskUpdater`:

- `taskUpdater.applyFullUpdate(task, dto)`

## 2.6 Persistieren
- `taskService.updateTask(task)`

## 2.7 Rückgabe
- Rückgabe des gemappten `TaskSummaryDto`

---

# 3. TaskUpdater: Anwendung der Update‑Regeln

Der TaskUpdater ist eine technische Hilfsklasse, die DTO‑Felder auf Domain‑Methoden abbildet.

## 3.1 Full Update (PUT)

Alle Felder werden gesetzt, aber **nur wenn sich der Wert tatsächlich geändert hat**:

- `task.changeTitle(dto.title())`
- `task.changeDescription(dto.description())`
- `task.changeDueDate(dto.dueDate())`
- `task.changePriority(dto.priority())`
- `task.changeStatus(dto.status())`

**Wichtig:**  
Der Updater enthält **keine Business‑Regeln**.  
Alle fachlichen Regeln liegen in der Domain (z. B. Status‑Transitionen).

---

# 4. Domain‑Modell: Fachliche Regeln & Invarianten

Die Task‑Entity führt ausschließlich fachliche Änderungen durch.

- `changeTitle()` → Validierung + Timestamp
- `changeDescription()` → optional + Timestamp
- `changeDueDate()` → Validierung + Timestamp
- `changePriority()` → darf nicht null sein + Timestamp
- `changeStatus()` → erzwingt gültige Status‑Transitions + Timestamp

Alle Änderungen laufen über Domain‑Methoden.  
Es gibt **keine Setter**.

---

# 5. TaskService: Persistenz + Mapping

Der TaskService übernimmt:

- Persistieren der Task (`taskRepository.save(task)`)
- Transaktion (`@Transactional`)
- Mapping in `TaskSummaryDto` über dein Transformer‑System:  
  `TransformerUtil.transform(TaskTransformer.TASK_TO_SUMMARY, saved)`

Der Service enthält **keine Business‑Logik**.

---

# 6. Repository: Datenbankzugriff

Das Repository:

- Speichert die Task
- Lädt die Task
- Führt keine Logik aus
- Ist rein technisch

---

# Zusammenfassung

Der Update‑Flow folgt strikt den DDD‑Schichten:

- **Controller** → HTTP‑Schicht
- **Orchestrator** → Ablaufsteuerung (Use‑Case)
- **TaskUpdater** → DTO → Domain‑Methoden
- **Domain** → Business‑Regeln & Invarianten
- **TaskService** → Persistenz & Mapping
- **Repository** → Datenbankzugriff

Jede Schicht hat eine klar definierte Verantwortung.  
Der Flow ist vollständig frei von Seiteneffekten, Leaks oder Schichtverletzungen.