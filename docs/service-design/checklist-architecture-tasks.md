# ✅ Projektspezifische Architektur‑Checkliste  
### Tasks‑Projekt (TaskList & Task)

## A. Aggregate definieren

- [ ] `TaskList` ist Aggregat‑Root.
- [ ] `Task` ist Aggregat‑Root.
- [ ] `TaskList` enthält eine Kollektion von `Task`, aber `Task` bleibt eigenes Aggregat.
- [ ] Manipulation von Tasks erfolgt nur über Domain‑Methoden (keine direkte List‑Manipulation von außen).

---

## B. TaskListService (Single‑Aggregate)

Gehört in den `TaskListService`, wenn der UseCase nur `TaskList` betrifft:

- [ ] TaskList erstellen.
- [ ] TaskList aktualisieren (Titel, Beschreibung).
- [ ] TaskList löschen.
- [ ] TaskList aktivieren.
- [ ] TaskList archivieren (nur wenn keine Task‑bezogene Domain‑Regel greift).
- [ ] Statuswechsel, die nur TaskList betreffen.
- [ ] `updated` wird im Aggregat gesetzt.

---

## C. TaskService (Single‑Aggregate)

Gehört in den `TaskService`, wenn der UseCase nur `Task` betrifft:

- [ ] Task erstellen (ohne TaskList‑Prüfung).
- [ ] Task aktualisieren (Titel, Beschreibung, Status).
- [ ] Task löschen.
- [ ] Task abschließen / wieder öffnen.
- [ ] `updated` wird im Aggregat gesetzt.

---

## D. Orchestrator (Cross‑Aggregate)

Gehört in den `TaskListsTaskOrchestrator`, wenn der UseCase mehrere Aggregate betrifft:

- [ ] Task in einer TaskList erstellen (Task + TaskList).
- [ ] TaskList archivieren **mit Prüfung der zugehörigen Tasks**.
- [ ] TaskList archivieren und Tasks mitarchivieren.
- [ ] Task zwischen TaskLists verschieben.
- [ ] TaskList löschen mit speziellem Task‑Handling.
- [ ] Task‑Status beeinflusst TaskList‑Status oder umgekehrt.
- [ ] Mehrere Aggregate müssen gemeinsam geprüft oder verändert werden.

---

## E. Domain‑Regeln (Tasks‑Projekt)

- [ ] Eine neue TaskList ist immer aktiv.
- [ ] Eine TaskList kann jederzeit wieder aktiviert werden (ohne Task‑Prüfung).
- [ ] Archivieren kann Domain‑Regeln über Tasks enthalten (z. B. „nur wenn alle Tasks abgeschlossen sind“).
- [ ] `TaskList` schützt ihre Tasks (`ownsTask`, kontrollierte Manipulation).
- [ ] `Task` schützt seine Invarianten (Titel, Status, Zeitstempel).

---

## F. Controller‑Design (Subresource)

- [ ] Es gibt genau einen Controller: `TaskListTaskController`.
- [ ] `/tasklists` bedient TaskList‑bezogene UseCases.
- [ ] `/tasklists/{taskListId}/tasks` bedient Task‑UseCases als Subresource.
- [ ] Der Controller delegiert:
  - [ ] reine TaskList‑UseCases → `TaskListService`
  - [ ] reine Task‑UseCases → `TaskService`
  - [ ] Cross‑Aggregate‑UseCases → `TaskListsTaskOrchestrator`

---

## G. Persistenz

- [ ] `TaskListRepository` speichert nur `TaskList`‑Aggregate.
- [ ] `TaskRepository` speichert nur `Task`‑Aggregate.
- [ ] Repositories enthalten keine Business‑Logik.
- [ ] Cascade‑Konfiguration entspricht den Domain‑Regeln (nicht nur technischen Bequemlichkeiten).
