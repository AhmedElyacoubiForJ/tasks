# 🧠 Modul3: Subqueries  
**Frage → Lösung**

> In diesem Modul lernt man, wie man mit Unterabfragen komplexe Bedingungen formuliert — z.B. Listen mit bestimmten Task-Zuständen oder Tasks aus besonders aktiven Listen.

---

### 🔍 Aufgabe 1: Listen mit mindestens ein offener Task

**Frage:**  
Wie zeigt man nur die Task-Listen, die mindestens ein Task mit Status `'OPEN'` enthalten?

**Lösung:**

```sql
SELECT *
FROM task_lists
WHERE id IN (
  SELECT task_list_id
  FROM tasks
  WHERE status = 'OPEN'
);
```

➡️ Die Subquery sucht alle `task_list_id`, die mindestens ein offener Task haben.  
Die Hauptabfrage zeigt dann die passenden Listen.

---

#### 🧠 Was macht diese Abfrage wirklich?

```sql
SELECT *
FROM task_lists
WHERE id IN (
  SELECT task_list_id
  FROM tasks
  WHERE status = 'OPEN'
);
```

🔍 Subquery:
```sql
SELECT task_list_id
FROM tasks
WHERE status = 'OPEN';
```

➡️ Diese liefert alle `task_list_id`, bei denen mindestens ein Task den Status `'OPEN'` hat.  
Wenn mehrere Tasks zur selben Liste gehören, erscheint die `task_list_id` mehrfach — aber das ist kein Problem, denn:

✅ Warum funktioniert das trotzdem?

1. Die `IN (...)`-Klausel prüft nur, ob `id` in der Ergebnismenge enthalten ist — egal wie oft.
2. PostgreSQL erkennt Duplikate automatisch bei der Prüfung mit `IN`.
3. Das bedeutet: Sobald ein einziger Task mit `'OPEN'` existiert, wird die zugehörige Liste ausgewählt.

➡️ Also: „mindestens eine“ ist erfüllt, weil die Subquery alle passenden `task_list_id` liefert — auch mehrfach.

🧪 Bonus: Duplikate vermeiden (optional)

```sql
SELECT DISTINCT task_list_id
FROM tasks
WHERE status = 'OPEN';
```

→ Das ändert nichts am Ergebnis der Hauptabfrage, aber macht die Subquery sauberer.

---

### 🧠 Aufgabe 2: Tasks, die zur Liste mit den meisten Tasks gehören

**Frage:**  
Wie zeigt man alle Tasks, die zur Liste gehören, die die meisten Tasks enthält?

**Lösung:**

```sql
SELECT *
FROM tasks
WHERE task_list_id = (
  SELECT task_list_id
  FROM tasks
  GROUP BY task_list_id
  ORDER BY COUNT(*) DESC
  LIMIT 1
);
```

➡️ Die Subquery ermittelt die `task_list_id` mit den meisten Tasks.  
Die Hauptabfrage zeigt alle Tasks dieser Liste.

---

### 🧪 Aufgabe 3: Listen, deren Tasks alle den Status `'DONE'` haben

**Frage:**  
Wie zeigt man nur die Task-Listen, bei denen **alle zugehörigen Tasks** den Status `'DONE'` haben?

**Lösung:**

```sql
SELECT *
FROM task_lists
WHERE id NOT IN (
  SELECT task_list_id
  FROM tasks
  WHERE status != 'DONE'
);
```

➡️ Die Subquery sucht Listen, die mindestens ein Task mit anderem Status haben.  
Die Hauptabfrage schließt diese aus — übrig bleiben Listen mit ausschließlich `'DONE'`-Tasks.

---

> ✅ Weiter mit [Modul4: Window Functions](MODUL4.md)