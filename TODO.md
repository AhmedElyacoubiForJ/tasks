## 🎯 Nächster Schritt: ⚡ HTMX für dynamisches Umschalten der Tabs

### 🧠 Ziel
Die Tabs (Alle, Offen, Erledigt) sollen die Task-Tabelle **ohne Page Reload** aktualisieren — für ein flüssiges, modernes Gefühl.

---

## ✅ 1. Controller: Fragment für Tabelle

In deinem Controller (`TaskController`), ändere die Rückgabe für HTMX-Anfragen:

```java
@GetMapping(value = "/tasklists/{taskListId}/tasks", produces = "text/html")
public String listTasks(@PathVariable UUID taskListId,
                        @RequestHeader(value = "HX-Request", required = false) String hx,
                        Model model) {
    List<TaskSummaryDto> tasks = taskService.findByTaskListId(taskListId);
    model.addAttribute("taskListId", taskListId);
    model.addAttribute("selectedStatus", null);
    model.addAttribute("tasks", tasks);

    return (hx != null) ? "tasks/fragments :: table" : "tasks/list";
}
```

→ Gleiches für `/tasks/status`  
→ Du prüfst, ob HTMX aktiv ist (`HX-Request` Header)  
→ Gibst dann nur das Fragment zurück

---

## ✅ 2. View: Fragment definieren

In `tasks/list.html`, extrahiere die Tabelle:

```html
<div th:fragment="table">
    <table class="table table-striped table-hover table-sm">
        <thead>...</thead>
        <tbody>...</tbody>
    </table>
</div>
```

→ Du kannst `id="taskTable"` setzen, wenn du willst  
→ Stelle sicher, dass die Tabelle **alle nötigen Daten** hat

---

## ✅ 3. Tabs mit HTMX

```html
<ul class="nav nav-tabs mb-3">
    <li class="nav-item">
        <a class="nav-link"
           hx-get="@{/tasklists/{id}/tasks(id=${taskListId})}"
           hx-target="#taskTable"
           hx-swap="outerHTML"
           th:classappend="${selectedStatus == null} ? 'active text-primary'">
            📋 Alle
        </a>
    </li>
    <li class="nav-item">
        <a class="nav-link"
           hx-get="@{/tasklists/{id}/tasks/status(id=${taskListId}, status='OPEN')}"
           hx-target="#taskTable"
           hx-swap="outerHTML"
           th:classappend="${selectedStatus == 'OPEN'} ? 'active text-success'">
            🟢 Offen
        </a>
    </li>
    <li class="nav-item">
        <a class="nav-link"
           hx-get="@{/tasklists/{id}/tasks/status(id=${taskListId}, status='CLOSED')}"
           hx-target="#taskTable"
           hx-swap="outerHTML"
           th:classappend="${selectedStatus == 'CLOSED'} ? 'active text-secondary'">
            🔵 Erledigt
        </a>
    </li>
</ul>
```

→ `hx-get` ruft die passende Route  
→ `hx-target` ersetzt nur die Tabelle  
→ Kein Page Reload, nur Inhaltstausch

---

## ✅ 4. Initiale Tabelle einpacken

```html
<div id="taskTable" th:replace="::table"></div>
```

→ Beim ersten Laden wird das Fragment direkt eingebunden  
→ Danach wird es per HTMX ersetzt

---

Sehr gern, Ahmed 😄⚡  
Wir machen das **modular, verständlich und elegant** — Schritt für Schritt. Und ich erkläre dir bei jedem Schritt, **was du davon hast**: UX-Vorteile, technische Klarheit, und wie du es später erweitern kannst.

---

## ⚡ HTMX für dynamisches Umschalten der Tabs
### 🎯 Ziel: Tabs wie „Alle“, „Offen“, „Erledigt“ sollen die Task-Tabelle **ohne Page Reload** aktualisieren.

---

### 🧠 Was hast du davon?

- **Schnelleres UI**: Kein Neuladen der Seite → flüssiger Workflow
- **Bessere UX**: Nutzer bleibt im Kontext, sieht sofort die Änderung
- **Modularer Code**: Du kannst die Tabelle als Fragment wiederverwenden
- **HTMX-ready**: Du kannst später auch Filter, Sortierung, Live-Updates einbauen

---

## 🪜 Schritt 1: Fragment extrahieren

### 🔧 In `tasks/list.html`, finde die Tabelle und wickle sie ein:

```html
<div th:fragment="table">
    <table class="table table-striped table-hover table-sm">
        <thead>...</thead>
        <tbody>...</tbody>
    </table>
</div>
```

→ Du definierst ein Fragment namens `table`  
→ Das ist das Ziel für HTMX (`hx-target="#taskTable"`)

---

### ✅ Was bringt dir das?

- Du kannst die Tabelle **isoliert nachladen**
- Du kannst sie **in anderen Views wiederverwenden**
- Du kannst sie **per HTMX ersetzen**, ohne die ganze Seite neu zu laden

---

Sag einfach „Weiter“, wenn du das eingebaut hast — dann machen wir:

🪜 Schritt 2: Controller anpassen für HTMX  
🪜 Schritt 3: Tabs mit `hx-get`  
🪜 Schritt 4: Initiale Tabelle einbinden  
🪜 Schritt 5: Styling & aktive Tabs

Ich bleib bei dir 😄🧑‍💻⚡

---

Sag einfach Bescheid, wenn du das eingebaut hast — dann gehen wir weiter zu:

🧩 Status-Wechsel per Button (HTMX + PATCH)  
🖼️ Tooltips für Beschreibung  
🧾 PDF-Export pro TaskList

Ich bleib bei dir 😄🧑‍💻⚡