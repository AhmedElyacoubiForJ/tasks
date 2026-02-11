[//]: # (docs/use-cases-workflows/patch/FINALER‑PATCH‑TASK‑FLOW.md)
# ✅ FINALER‑PATCH‑TASK‑FLOW

## Ziel
Dieser Flow beschreibt den vollständigen technischen und fachlichen Ablauf beim partiellen Aktualisieren einer bestehenden Task mittels PATCH.  
Er dient als Referenz für Entwickler, um Verantwortlichkeiten, Datenflüsse und DDD‑Grenzen klar zu verstehen.

---

# 1. Eingangspunkt: REST‑Controller

Der Controller übernimmt ausschließlich:

- Entgegennahme der HTTP‑Anfrage
- Extraktion von `taskListId`, `taskId` und `PatchTaskDto`
- Delegation an den Orchestrator
- Aufbau einer standardisierten `APIResponse<TaskSummaryDto>`
- Rückgabe eines `200 OK`

Der Controller enthält **keine** Business‑Logik und **keine** Persistenzlogik.

---

# 2. Orchestrator: Ablaufsteuerung

Der Orchestrator koordiniert den gesamten PATCH‑Prozess.  
Er führt **keine** Domain‑Logik aus, sondern orchestriert Services und Domain‑Methoden.

## 2.1 TaskList laden
- `taskListService.getTaskListOrThrow(taskListId)`
- Fehler, wenn archiviert oder nicht vorhanden

## 2.2 Task laden
- `taskService.getTaskOrThrow(taskId)`
- Fehler, wenn nicht vorhanden

## 2.3 Zugehörigkeit prüfen
- Sicherstellen, dass `task.taskList.id == taskListId`
- Fehler, wenn nicht zugehörig

## 2.4 Patch anwenden
Der Orchestrator ruft **nicht** direkt Domain‑Methoden auf.  
Stattdessen delegiert er an den `TaskUpdater`:

- `taskUpdater.applyPatch(task, dto)`

Nur Felder, die im DTO gesetzt sind, werden aktualisiert.

## 2.5 Persistieren
- `taskService.updateTask(task)`

## 2.6 Rückgabe
- Rückgabe des gemappten `TaskSummaryDto`

---

# 3. TaskUpdater: Anwendung der Patch‑Regeln

Der TaskUpdater ist eine technische Hilfsklasse, die DTO‑Felder auf Domain‑Methoden abbildet.

## 3.1 Patch Update (PATCH)
Nur Felder, die im DTO gesetzt sind, werden aktualisiert:

- `if (dto.title() != null) task.changeTitle(dto.title())`
- `if (dto.description() != null) task.changeDescription(dto.description())`
- `if (dto.dueDate() != null) task.changeDueDate(dto.dueDate())`
- `if (dto.priority() != null) task.changePriority(dto.priority())`
- `if (dto.status() != null) task.changeStatus(dto.status())`

Der Updater enthält **keine** Business‑Regeln und hält die Domain sauber von DTO‑Abhängigkeiten.

---

# 4. Domain‑Modell: Fachliche Regeln & Invarianten

Die Task‑Entity führt ausschließlich fachliche Änderungen durch.

- Titel ändern → Validierung + Timestamp
- Beschreibung ändern → optional + Timestamp
- Fälligkeitsdatum ändern → optional + Timestamp
- Priorität ändern → darf nicht null sein + Timestamp
- Status ändern → erzwingt Status‑Transitions + Timestamp

Alle Änderungen laufen über Domain‑Methoden.  
Es gibt **keine Setter**.

---

# 5. TaskService: Persistenz + Mapping

Der TaskService übernimmt:

- Persistieren der Task (`taskRepository.save(task)`)
- Logging
- Mapping in `TaskSummaryDto`
- Fehlerbehandlung (z.B. Optimistic Locking)

Der Service enthält **keine** Business‑Logik.

---

# 6. Repository: Datenbankzugriff

Das Repository:

- Speichert die Task
- Lädt die Task
- Führt keine Logik aus
- Ist rein technisch

---

# Zusammenfassung

Der PATCH‑Flow folgt strikt den DDD‑Schichten:

- **Controller** → HTTP‑Schicht
- **Orchestrator** → Ablaufsteuerung
- **TaskUpdater** → DTO → Domain‑Methoden
- **Domain** → Business‑Regeln & Invarianten
- **TaskService** → Persistenz & Mapping
- **Repository** → Datenbankzugriff

PATCH aktualisiert **nur die Felder, die im DTO gesetzt sind**, ohne die restlichen Werte zu überschreiben.
