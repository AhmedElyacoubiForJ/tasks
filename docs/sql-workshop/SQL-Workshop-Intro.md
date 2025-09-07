> Du kannst mir jederzeit konkrete Fragen stellen wie:

> „Warum ist NOT EXISTS hier besser als LEFT JOIN ... IS NULL?“

> „Wie würde man das mit Indexen noch weiter optimieren?“

> „Kannst du mir ein Beispiel mit WINDOW FUNCTIONS zeigen?“

> Oder du sagst einfach: „Ich verstehe diesen Teil nicht ganz“



# Ein vollständiger **SQL-Workshop**.
> Der Fokus liegt auf **JOIN-Abfragen mit echten Use Cases**,
> jeweils in einer nicht optimierten und einer optimierten Variante — inklusive kurzer, verständlicher Erklärungen.

---

# 🧠 PostgreSQL SQL-Workshop: JOIN-Abfragen mit Use Cases

**Ziel:** Verständnis für sinnvolle JOINs entwickeln, typische Fehler erkennen und bessere Varianten schreiben.  
**Tabellen:** `tasks`, `task_lists`  
**Hinweis:** Es wurden bewusst keine Indexe verwendet, um den Fokus auf die Struktur und Logik der Abfragen zu legen.

---

## 🔹 Use Case 1: Alle Tasks mit ihrer Liste anzeigen

**❌ Nicht optimiert**
```sql
SELECT * 
FROM tasks 
JOIN task_lists ON tasks.task_list_id = task_lists.id;
```

**✅ Optimiert**
```sql
SELECT 
  tasks.title AS task_title,
  tasks.status,
  task_lists.title AS list_title
FROM tasks 
INNER JOIN task_lists ON tasks.task_list_id = task_lists.id
WHERE tasks.status = 'OPEN'
ORDER BY task_lists.title, tasks.created;
```

🧾 *Erklärung:*  
Die optimierte Version zeigt nur relevante Spalten, filtert nach Status und sortiert sinnvoll.

---

## 🔹 Use Case 2: Alle Listen mit ihren Tasks (auch wenn keine Tasks existieren)

**❌ Nicht optimiert**
```sql
SELECT * 
FROM task_lists 
LEFT JOIN tasks ON task_lists.id = tasks.task_list_id;
```

**✅ Optimiert**
```sql
SELECT 
  task_lists.title AS list_title,
  tasks.title AS task_title,
  tasks.status
FROM task_lists 
LEFT JOIN tasks ON task_lists.id = tasks.task_list_id
ORDER BY task_lists.title, tasks.created;
```

🧾 *Erklärung:*  
LEFT JOIN zeigt auch Listen ohne Tasks. Die optimierte Version zeigt nur relevante Infos und sortiert nach Listenname und Task-Erstellung.

---

## 🔹 Use Case 3: Listen mit Anzahl ihrer Tasks

**❌ Nicht optimiert**
```sql
SELECT task_lists.title, 
       (SELECT COUNT(*) FROM tasks WHERE tasks.task_list_id = task_lists.id) AS task_count
FROM task_lists;
```

**✅ Optimiert**
```sql
SELECT 
  task_lists.title,
  COUNT(tasks.id) AS task_count
FROM task_lists
LEFT JOIN tasks ON task_lists.id = tasks.task_list_id
GROUP BY task_lists.title
ORDER BY task_count DESC;
```

🧾 *Erklärung:*  
Subqueries sind oft langsamer. Die optimierte Variante nutzt GROUP BY und JOIN direkt — effizienter und klarer.

---

## 🔹 Use Case 4: Listen ohne Tasks finden

**❌ Nicht optimiert**
```sql
SELECT task_lists.title 
FROM task_lists 
LEFT JOIN tasks ON task_lists.id = tasks.task_list_id
WHERE tasks.id IS NULL;
```

**✅ Optimiert**
```sql
SELECT title 
FROM task_lists 
WHERE NOT EXISTS (
  SELECT 1 FROM tasks WHERE tasks.task_list_id = task_lists.id
);
```

🧾 *Erklärung:*  
`NOT EXISTS` ist oft performanter als `LEFT JOIN ... IS NULL`, besonders bei großen Datenmengen.

---

## 🔹 Use Case 5: Tasks mit hoher Priorität und Listenname anzeigen

**❌ Nicht optimiert**
```sql
SELECT * 
FROM tasks 
INNER JOIN task_lists ON tasks.task_list_id = task_lists.id
WHERE tasks.priority = 'HIGH';
```

**✅ Optimiert**
```sql
SELECT 
  tasks.title AS task_title,
  task_lists.title AS list_title,
  tasks.due_date
FROM tasks 
INNER JOIN task_lists ON tasks.task_list_id = task_lists.id
WHERE tasks.priority = 'HIGH'
ORDER BY tasks.due_date;
```

🧾 *Erklärung:*  
Nur relevante Spalten, sinnvolle Sortierung nach Fälligkeitsdatum — ideal für Priorisierung.

---

## 📘 Zusammenfassung

| Use Case                            | Join-Typ     | Fokus                         |
|------------------------------------|--------------|-------------------------------|
| Tasks mit Listen                   | INNER JOIN   | Relevante Spalten & Filter    |
| Listen mit Tasks (auch leere)      | LEFT JOIN    | Vollständigkeit               |
| Task-Zählung pro Liste             | LEFT JOIN + GROUP BY | Aggregation           |
| Listen ohne Tasks                  | NOT EXISTS   | Negation effizient lösen      |
| High-Priority Tasks mit Listenname | INNER JOIN   | Filter + Sortierung           |

---

Sehr gerne, Ahmed! Hier ist die Übersicht zu den SQL-Join-Typen als sauber strukturierter **Markdown-Abschnitt**, den du direkt in deinen `SQL-Workshop.md` einfügen kannst:

---

```markdown
## 🔗 Übersicht: SQL JOIN-Typen im Kontext von `tasks` und `task_lists`

Zwei Tabellen:

- `task_lists(id, title)`
- `tasks(id, title, task_list_id, status)`

---

### 🧠 JOIN-Typen im Vergleich

| JOIN-Typ     | Beschreibung                                           | Beispiel-Ergebnis                         |
|--------------|--------------------------------------------------------|-------------------------------------------|
| `INNER JOIN` | Zeigt nur Datensätze mit passender Verbindung         | Nur Tasks, die einer Liste zugeordnet sind |
| `LEFT JOIN`  | Zeigt alle Listen, auch wenn keine Tasks vorhanden    | Listen mit oder ohne Tasks                |
| `RIGHT JOIN` | Zeigt alle Tasks, auch wenn keine Liste vorhanden     | Tasks mit oder ohne zugehörige Liste      |
| `FULL JOIN`  | Zeigt alles aus beiden Tabellen, auch ohne Verbindung | Alle Listen & Tasks, auch ohne Match      |
| `CROSS JOIN` | Kombiniert jede Liste mit jeder Task (kartesisches Produkt) | Alle möglichen Kombinationen         |

---

### 🔍 Beispiele

#### `INNER JOIN` – Nur verbundene Datensätze

```sql
SELECT tasks.title, task_lists.title
FROM tasks
INNER JOIN task_lists ON tasks.task_list_id = task_lists.id;
```

➡️ Zeigt nur Tasks, die einer existierenden Liste zugeordnet sind.

---

#### `LEFT JOIN` – Alle Listen, auch ohne Tasks

```sql
SELECT task_lists.title, tasks.title
FROM task_lists
LEFT JOIN tasks ON task_lists.id = tasks.task_list_id;
```

➡️ Zeigt alle Listen, auch wenn keine Tasks vorhanden sind. Tasks-Spalten sind dann `NULL`.

---

#### `RIGHT JOIN` – Alle Tasks, auch ohne Liste

```sql
SELECT tasks.title, task_lists.title
FROM tasks
RIGHT JOIN task_lists ON tasks.task_list_id = task_lists.id;
```

➡️ Zeigt alle Tasks, auch wenn keine zugehörige Liste existiert. Listen-Spalten sind dann `NULL`.

---

#### `FULL JOIN` – Alles, was da ist

```sql
SELECT tasks.title, task_lists.title
FROM tasks
FULL JOIN task_lists ON tasks.task_list_id = task_lists.id;
```

➡️ Zeigt alle Tasks und alle Listen, auch wenn keine Verbindung besteht.

---

#### `CROSS JOIN` – Jede Kombination

```sql
SELECT tasks.title, task_lists.title
FROM tasks
CROSS JOIN task_lists;
```

➡️ Kombiniert jede Task mit jeder Liste. Ergebnis = `tasks.count × task_lists.count`

---

### 📘 Zusammenfassung

| JOIN-Typ     | Zeigt auch ohne Match | Typischer Use Case                   |
|--------------|------------------------|--------------------------------------|
| `INNER JOIN` | ❌ Nein                | Nur gültige Beziehungen              |
| `LEFT JOIN`  | ✅ Listen ohne Tasks   | Leere Listen finden                  |
| `RIGHT JOIN` | ✅ Tasks ohne Listen   | Selten sinnvoll                      |
| `FULL JOIN`  | ✅ Beide Seiten        | Vollständige Datenübersicht          |
| `CROSS JOIN` | ❌ Alle Kombinationen  | Statistische oder Testzwecke         |

---



Wenn du magst, kann ich dir später auch ein paar Übungsaufgaben zu den JOIN-Typen formulieren — mit kleinen Datenbeispielen und Lösungen. Aber jetzt hast du erstmal eine solide Übersicht, die du jederzeit erweitern kannst. Viel Spaß beim Weiterarbeiten!



### 🛠️ Hinweis: Weitere Optimierungsmöglichkeiten

> Neben der Projektion (Auswahl der Spalten) gibt es zahlreiche weitere Möglichkeiten, SQL-Abfragen zu optimieren.
> Diese wurden im Rahmen dieses Workshops **bewusst ausgeklammert**,
> um den Fokus auf Verständlichkeit und Praxisnähe zu legen.
> Für späteres Vertiefen könnten folgende Ansätze interessant sein:
>
> - **Indexierung**: Beschleunigung von Suchvorgängen durch gezielte Indexe
> - **Join-Strategien**: Wahl effizienter Join-Typen (z.B. Hash Join, Merge Join)
> - **Filteroptimierung**: Frühzeitiges Anwenden von WHERE-Bedingungen
> - **Subqueries vs. CTEs**: Strukturierung komplexer Abfragen
> - **Window Functions**: Erweiterte Analysen über Datenreihen
> - **Materialisierte Views**: Zwischenspeichern von Ergebnissen
> - **Query Caching**: Wiederverwendung von häufigen Abfragen
>
> Diese Themen können bei Bedarf später ergänzt oder in einem separaten Modul behandelt werden.

---

> Bis dahin: volle Konzentration auf das, was wirklich zählt 💪

> Später werden wir Workshops zu **Query-Tuning**, **Subquery-Strategien** oder **Window Functions** bauen.