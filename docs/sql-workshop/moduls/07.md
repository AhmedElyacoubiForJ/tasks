# 🧪 Modul7: Testdaten-Generierung & Simulation  
**Frage → Lösung**

> In diesem Modul lernst du, wie man realistische und gezielt fehlerhafte Testdaten erzeugt — z. B. für Workshops, Unit-Tests oder Datenqualitätsprüfungen.

---

### 🧬 Aufgabe 1: Manuelles Einfügen realistischer Tasks

**Frage:**  
Wie fügt man einzelne Tasks mit sinnvollen Werten manuell ein?

**Lösung:**

```sql
INSERT INTO tasks (task_id, task_list_id, title, status, created_at)
VALUES (
  gen_random_uuid(),
  'a2526850-b053-4c52-9cdb-4625f13e3b87',
  'Datenbank prüfen',
  'OPEN',
  now()
);
```

➡️ Nutzt `gen_random_uuid()` aus `pgcrypto` für eindeutige IDs und `now()` für aktuelle Zeitstempel.

---

### 🔁 Aufgabe 2: Mehrere Tasks automatisch generieren

**Frage:**  
Wie erzeugt man viele Tasks mit `generate_series()`?

**Lösung:**

```sql
INSERT INTO tasks (task_id, task_list_id, title, status, created_at)
SELECT
  gen_random_uuid(),
  'a2526850-b053-4c52-9cdb-4625f13e3b87',
  'Auto-Task ' || i,
  'IN_PROGRESS',
  now() - (i || ' days')::interval
FROM generate_series(1, 10) AS s(i);
```

➡️ Erzeugt 10 Tasks mit fortlaufenden Titeln und gestaffelten Erstellungsdaten.

---

### ❌ Aufgabe 3: Fehlerhafte Daten gezielt simulieren

**Frage:**  
Wie erzeugt man Tasks mit ungültigem Status oder fehlender Liste?

**Lösung:**

```sql
INSERT INTO tasks (task_id, task_list_id, title, status, created_at)
VALUES
  (gen_random_uuid(), NULL, 'Fehlende Liste', 'OPEN', now()),
  (gen_random_uuid(), 'a2526850-b053-4c52-9cdb-4625f13e3b87', 'Ungültiger Status', 'UNKNOWN', now());
```

➡️ Simuliert gezielt typische Fehlerfälle für Validierungstests.

---

### 🧠 Bonus: Zufällige Daten mit `pgcrypto` und `random()`

**Frage:**  
Wie erzeugt man Tasks mit zufälligem Status?

**Lösung:**

```sql
INSERT INTO tasks (task_id, task_list_id, title, status, created_at)
SELECT
  gen_random_uuid(),
  'a2526850-b053-4c52-9cdb-4625f13e3b87',
  'Random Task ' || i,
  CASE floor(random() * 3)::int
    WHEN 0 THEN 'OPEN'
    WHEN 1 THEN 'IN_PROGRESS'
    ELSE 'DONE'
  END,
  now()
FROM generate_series(1, 5) AS s(i);
```

➡️ Erzeugt 5 Tasks mit zufälligem Status — ideal für dynamische Tests.

---

### 🧪 Hinweis zur Vorbereitung

Du kannst diese Daten nutzen, um alle Module (z.B. Modul5 & 6) realistisch zu testen.  
Fehlerhafte Einträge helfen dir, Views und Validierungslogik zu prüfen.

---

> ✅ Zurück zur [Übersicht](../SQL-WORKSHOP.md)


---

[//]: # (Wenn du magst, kann ich dir auch ein `README.md` für dein `/sql/`-Verzeichnis entwerfen — mit Projektbeschreibung, Setup-Hinweisen und Modulverlinkung. Du baust hier ein richtig starkes SQL-Toolkit, Ahmed! 🧠📘)