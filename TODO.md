⭐ Wenn du willst, kann ich jetzt:
👉 Dir eine komplette Test‑Checkliste erstellen
👉 Dir Beispiel‑Requests für alle Endpoints generieren
👉 Dir HTMX‑Snippets für die UI bauen
👉 Dir ein README‑Update erstellen
👉 Dir ein Architekturdiagramm generieren

👉 eine REST‑Assured Testklasse für TaskList‑Endpoints erstellen
👉 eine REST‑Assured Testklasse für Statuswechsel (archive/activate) erstellen
👉 Domain‑Regel‑Tests für Task und TaskList bauen
👉 Testcontainers einrichten (PostgreSQL in Docker)
👉 MockMvc‑Tests für Controller‑Layer generieren

👉 REST‑Assured Testklasse für TaskList‑Endpoints
👉 REST‑Assured Testklasse für Statuswechsel (archive/activate)
👉 Domain‑Regel‑Tests (JUnit)
👉 Testcontainers Setup (PostgreSQL)
👉 MockMvc‑Tests für Controller‑Layer

👉 TaskApiTest (final, grün)  
👉 TaskListApiTest  
👉 Statuswechsel‑Tests  
👉 Testcontainers Setup

👉 komplette Testklasse
👉 alle CRUD‑Tests
👉 Statuswechsel‑Tests
👉 Fehlerfälle (404, 400, 409)
👉 Response‑Schemas
👉 Test‑Daten‑Factories
👉 Utility‑Methoden wie createTaskList() und createTask()


ITaskListsTasksApiImpl
    @GetMapping(value = "/tasklists/{taskListId}/tasks", produces = "application/json")
    ResponseEntity<APIResponseListTaskSummaryDto> getTasksByListId(
        @Parameter(description = "UUID der TaskList")
        @PathVariable("taskListId") UUID taskListId
    );
    @PostMapping(value = "/tasklists/{taskListId}/tasks", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponseTaskSummaryDto> createTaskInList(
        @PathVariable("taskListId") UUID taskListId,
        @Valid @RequestBody CreateTaskDto dto
    );
    @PutMapping(value = "/tasklists/{taskListId}/tasks/{taskId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponseTaskSummaryDto> updateTaskInList(
        @PathVariable("taskListId") UUID taskListId,
        @PathVariable("taskId") UUID taskId,
        @Valid @RequestBody FullUpdateTaskDto dto
    );
    @PatchMapping(value = "/tasklists/{taskListId}/tasks/{taskId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponseTaskSummaryDto> patchTaskInList(
        @PathVariable("taskListId") UUID taskListId,
        @PathVariable("taskId") UUID taskId,
        @Valid @RequestBody PatchTaskDto dto
    );
    @DeleteMapping(value = "/tasklists/{taskListId}/tasks/{taskId}", produces = "application/json")
    ResponseEntity<APIResponseVoid> deleteTaskInList(
        @PathVariable("taskListId") UUID taskListId,
        @PathVariable("taskId") UUID taskId
    );

---

23.02.2026

⭐ Was du jetzt bekommst
Alle Tests grün, wenn deine API korrekt implementiert ist

Alle Tests loggen den kompletten Response

Alle Tests sind Happy‑Path

Kein Fehlerfall (kommt später)

Saubere Struktur

Utility‑Methoden für TaskList & Task

Swagger‑kompatible JSON‑Bodies

⭐ Nächster Schritt
Wenn du willst, können wir jetzt:

👉 Fehlerfälle testen (404, 400, 409)  
👉 TaskListApiTest final bauen  
👉 Statuswechsel‑Tests  
👉 Testcontainers Setup  
👉 APIResponse‑Schema‑Validierung

---

👉 alle Tests als „DONE“ markieren
👉 die Fehlerfälle testen
👉 oder direkt zum Domain‑Fix übergehen (bidirektional)
👉 oder zum nächsten großen Thema gehen: Bidirektionale Beziehung fixen

---

⭐ 4. Was wir als Nächstes tun können
Jetzt, wo DELETE vollständig stabil ist, können wir weitergehen.

Hier sind die nächsten sinnvollen Schritte:

👉 Fehlerfall für PATCH testen
(z.B. ungültige Werte, Domain‑Regeln, Statuswechsel)

👉 Fehlerfall für PUT testen
(z.B. Pflichtfelder fehlen, invalides Enum, etc.)

👉 Fehlerfall für POST testen
(z.B. Titel leer, invalides Datum)

👉 Test‑Design überdenken
(du hattest gesagt, du willst mir etwas „aus dem Herzen“ sagen)

👉 Bidirektionale Beziehung fixen
(damit Create endlich die ID zurückgibt)

---

⭐ 6. Was du jetzt tun kannst
Du kannst:

👉 TaskApiFullSuite ausführen  
→ und ALLE Tests im gesamten API‑Bereich laufen lassen.

- Wenn du willst, können wir als Nächstes:

- die CRUD‑Tests füllen

- die Validation‑Tests ergänzen

- die Scenario‑Tests bauen

- oder das bidirektionale Mapping fixen (damit Create endlich IDs zurückgibt)
