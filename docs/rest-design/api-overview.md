# 📑 REST API Overview

Dieses Dokument beschreibt die Pfadstruktur und Statuscodes der REST‑API.  
Basis: 2 Controller (`TaskListsRestController`, `TasksRestController`).  
Ein optionaler dritter Controller (`AdminTaskController`) ist als Erweiterung gedacht.

---

## 📊 Architekturübersicht (Entities & Controller)

```text
+-------------------+          +-------------------+
|   TaskLists       |          |   Tasks           |
|   (Entity)        |          |   (Entity)        |
+-------------------+          +-------------------+
          |                             |
          |                             |
          v                             v
+---------------------------+   +---------------------------+
| TaskListsRestController   |   | TasksRestController       |
| - CRUD für TaskLists      |   | - CRUD für Tasks          |
| - Subresource:            |   |                           |
|   /tasklists/{id}/tasks   |   |                           |
+---------------------------+   +---------------------------+
          |
          |
          v
+---------------------------+
| AdminTaskController       |
| (optional, später)        |
| - Bulk-Operationen        |
| - Reports/Statistiken     |
| - Admin-spezifische Pfade |
+---------------------------+
```

---

## 🧠 Erklärung
- **TaskListsRestController** → kümmert sich um alle CRUD‑Operationen für `TaskList`.
- **TasksRestController** → kümmert sich um CRUD für `Task`.
- **AdminTaskController** → optional, für Sonderfälle wie Bulk‑Delete oder Reports.
- **Subresource**: `GET /tasklists/{id}/tasks` bleibt im `TaskListsRestController`, weil es logisch zur Liste gehört.

---

## 1️⃣ TaskListsRestController (für TaskList-Entity)

### Endpoints
- **GET /tasklists** → Alle TaskLists abrufen
- **GET /tasklists/{id}** → Einzelne TaskList abrufen
- **POST /tasklists** → Neue TaskList erstellen
- **PUT /tasklists/{id}** → TaskList aktualisieren
- **DELETE /tasklists/{id}** → TaskList löschen

### Statuscodes
| Endpoint              | Methode | Erfolgreich | Fehlerfälle (Beispiele) |
|-----------------------|---------|-------------|--------------------------|
| /tasklists            | GET     | 200 OK      | 404 Not Found (keine Listen) |
| /tasklists/{id}       | GET     | 200 OK      | 404 Not Found (nicht gefunden) |
| /tasklists            | POST    | 201 Created | 400 Bad Request (Validierung) |
| /tasklists/{id}       | PUT     | 200 OK      | 404 Not Found, 400 Bad Request |
| /tasklists/{id}       | DELETE  | 200 OK      | 404 Not Found |

---

## 2️⃣ TasksRestController (für Task-Entity)

### Endpoints
- **GET /tasks** → Alle Tasks abrufen
- **GET /tasks/{id}** → Einzelnen Task abrufen
- **POST /tasks** → Neuen Task erstellen
- **PUT /tasks/{id}** → Task aktualisieren
- **DELETE /tasks/{id}** → Task löschen

### Statuscodes
| Endpoint              | Methode | Erfolgreich | Fehlerfälle (Beispiele) |
|-----------------------|---------|-------------|--------------------------|
| /tasks                | GET     | 200 OK      | 404 Not Found (keine Tasks) |
| /tasks/{id}           | GET     | 200 OK      | 404 Not Found (nicht gefunden) |
| /tasks                | POST    | 201 Created | 400 Bad Request (Validierung) |
| /tasks/{id}           | PUT     | 200 OK      | 404 Not Found, 400 Bad Request |
| /tasks/{id}           | DELETE  | 200 OK      | 404 Not Found |

---

## 3️⃣ AdminTaskController (Gedanke für spätere Erweiterung)

> Optionaler Controller für **Admin‑Use Cases** (z. B. Bulk‑Operationen, Reports).  
> Wird nur eingeführt, wenn die Standard‑Controller zu groß oder unübersichtlich werden.

### Mögliche Endpoints
- **GET /admin/tasklists** → Erweiterte Übersicht aller TaskLists inkl. Details/Statistiken
- **DELETE /admin/tasklists/bulk** → Mehrere TaskLists gleichzeitig löschen
- **GET /admin/reports/task-progress** → Report über Fortschritt aller TaskLists

### Statuscodes
| Endpoint                   | Methode | Erfolgreich | Fehlerfälle (Beispiele) |
|----------------------------|---------|-------------|--------------------------|
| /admin/tasklists           | GET     | 200 OK      | 403 Forbidden (kein Admin), 404 Not Found |
| /admin/tasklists/bulk      | DELETE  | 200 OK      | 403 Forbidden, 404 Not Found |
| /admin/reports/task-progress | GET   | 200 OK      | 403 Forbidden |

---

## 🧠 Design-Entscheidung

- **Standard‑API**: Nur `TaskListDto` für CRUD → schlank und konsistent.
- **Tasks**: Eigener Controller, klar getrennt von TaskLists.
- **Admin**: Optionaler dritter Controller für Sonderfälle, um die Haupt‑Controller klein zu halten.
- **Fehlerbehandlung**: Exceptions werden zentral über den `RestExceptionHandler` abgefangen und in konsistente `APIResponse`‑Objekte übersetzt.

---

## 🔗 Subresource: Tasks innerhalb einer TaskList

### Endpoints
- **GET /tasklists/{id}/tasks**  
  → Alle Tasks einer bestimmten TaskList abrufen

- **POST /tasklists/{id}/tasks**  
  → Neuen Task innerhalb einer bestimmten TaskList erstellen

---

### Statuscodes
| Endpoint                  | Methode | Erfolgreich | Fehlerfälle (Beispiele) |
|---------------------------|---------|-------------|--------------------------|
| /tasklists/{id}/tasks     | GET     | 200 OK      | 404 Not Found (TaskList nicht gefunden) |
| /tasklists/{id}/tasks     | POST    | 201 Created | 404 Not Found (TaskList nicht gefunden), 400 Bad Request (Validierung) |

---

### 🧠 Design-Entscheidung
- **Subresource**: Tasks sind logisch Teil einer TaskList → deshalb im `TaskListsRestController`.
- **Pfadstruktur**: `/tasklists/{id}/tasks` macht die Beziehung klar und REST‑konform.
- **Fehlerbehandlung**: Falls die TaskList nicht existiert → `EntityNotFoundException` → globaler `RestExceptionHandler` liefert `404`.

---


[//]: # (TODO: nächste Schritt eine Mini-Checkliste für API-Dokumentation baue (z.B. was man neben Pfaden und Statuscodes noch dokumentieren sollte: DTOs, Beispiel-Responses, Fehlerstruktur&#41;?)

---

[//]: # (Sehr gute Nachfrage, Ahmed 😄🧑‍💻 – lass uns die **globalen Task‑Operationen** klar abgrenzen, damit du entscheiden kannst, ob du dafür einen eigenen `TasksRestController` brauchst oder ob alles im Subresource &#40;`/tasklists/{id}/tasks`&#41; bleibt.)

[//]: # ()
[//]: # ()
[//]: # (## 🔎 Globale Task‑Operationen – typische Beispiele)

[//]: # ()
[//]: # (- **Alle Tasks im System abrufen**  )

[//]: # (  `GET /tasks` → z.B. für eine Übersicht aller offenen Tasks, unabhängig von ihrer Liste.)

[//]: # ()
[//]: # (- **Einzelnen Task abrufen**  )

[//]: # (  `GET /tasks/{taskId}` → Zugriff auf einen Task direkt über seine ID, ohne den Umweg über die TaskList.)

[//]: # ()
[//]: # (- **Suche/Filter über alle Tasks**  )

[//]: # (  `GET /tasks?status=open&assignee=Ahmed` → globale Queries, die nicht an eine bestimmte Liste gebunden sind.)

[//]: # ()
[//]: # (- **Reports/Statistiken**  )

[//]: # (  `GET /tasks/reports/progress` → Fortschritt aller Tasks im System, evtl. gruppiert nach Usern oder Status.)

[//]: # ()
[//]: # (- **Bulk‑Operationen**  )

[//]: # (  `DELETE /tasks/bulk` → mehrere Tasks auf einmal löschen, unabhängig von ihrer Liste.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 🧠 Entscheidungskriterium)

[//]: # ()
[//]: # (- **Wenn Tasks nur im Kontext einer TaskList existieren** → Subresource reicht &#40;`/tasklists/{id}/tasks`&#41;.)

[//]: # (- **Wenn du globale Operationen brauchst** &#40;Suche, Reports, Bulk‑Aktionen&#41; → dann ist ein eigener `TasksRestController` sinnvoll.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 🚀 Empfehlung für dein Projekt)

[//]: # ()
[//]: # (- **Minimalistisch starten**: Nur Subresource im `TaskListsRestController`.)

[//]: # (- **Später erweitern**: Falls du globale Features brauchst &#40;z. B. Suche über alle Tasks&#41;, dann baue einen `TasksRestController` dazu.)

[//]: # ()
[//]: # (So bleibst du schlank und kannst trotzdem jederzeit skalieren.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (👉 Willst du, dass ich dir eine **kompakte Tabelle** baue, die die Unterschiede zwischen *Subresource‑Tasks* und *globalen Task‑Operationen* gegenüberstellt, damit du die Entscheidung schwarz auf weiß in deiner Doku festhalten kannst?)

[//]: # ()
[//]: # (---)

---

[//]: # (## 📊 Vergleich: Subresource vs. globale Task-Operationen)

[//]: # ()
[//]: # (| Aspekt                  | Subresource &#40;`/tasklists/{id}/tasks`&#41;            | Global &#40;`/tasks`&#41;                          |)

[//]: # (|--------------------------|--------------------------------------------------|--------------------------------------------|)

[//]: # (| Kontext                  | Task existiert nur innerhalb einer TaskList      | Task ist eigenständige Ressource            |)

[//]: # (| Pfadstruktur             | Klarer Bezug zur Liste, REST-konform             | Direkter Zugriff über Task-ID               |)

[//]: # (| Typische Endpoints       | GET/POST/PUT/DELETE unter `/tasklists/{id}/tasks`| GET /tasks, GET /tasks/{id}, Suche, Reports |)

[//]: # (| Vorteile                 | Schlank, logisch, einfache Doku                  | Flexibel, globale Suche & Filter möglich    |)

[//]: # (| Nachteile                | Kein Zugriff ohne TaskList                      | Mehr Controller, etwas komplexer            |)

[//]: # (| Einsatzempfehlung        | Wenn Tasks nur im Kontext einer Liste relevant   | Wenn Tasks auch global benötigt werden      |)

[//]: # ()

---
