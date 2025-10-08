# 🔍 Modul2: LEFT JOIN mit COUNT  
**Frage → Lösung**

> In diesem Modul lernst du, wie man Task-Listen mit ihren zugehörigen Tasks verknüpft — inklusive leerer Listen — und wie man die Anzahl der Tasks pro Liste analysiert.

---

### 🔍 Aufgabe 1: Alle Listen inklusive Task-Anzahl (auch 0)

**Frage:**  
Wie zeigt man alle Task-Listen — auch die ohne Tasks — mit der Anzahl der zugehörigen Tasks?

**Lösung:**

```sql
SELECT tl.id, tl.title, COUNT(t.id) AS anzahl_tasks
FROM task_lists tl
LEFT JOIN tasks t ON tl.id = t.task_list_id
GROUP BY tl.id, tl.title;
```

➡️ `LEFT JOIN` sorgt dafür, dass auch Listen ohne Tasks erscheinen.  
`COUNT(t.id)` zählt nur vorhandene Tasks — bei leeren Listen ergibt das `0`.

---

### 📊 Aufgabe 2: Listen sortiert nach Task-Anzahl (absteigend)

**Frage:**  
Wie zeigt man alle Listen mit Task-Zähler, sortiert von „voll“ nach „leer“?

**Lösung:**

```sql
SELECT tl.id, tl.title, COUNT(t.id) AS anzahl_tasks
FROM task_lists tl
LEFT JOIN tasks t ON tl.id = t.task_list_id
GROUP BY tl.id, tl.title
ORDER BY anzahl_tasks DESC;
```

➡️ Die `ORDER BY`-Klausel sortiert die Ausgabe nach Anzahl der Tasks — höchste zuerst.

---

### 🧪 Aufgabe 3: Listen mit null Tasks gezielt anzeigen

**Frage:**  
Wie zeigt man nur die Task-Listen, die keine Tasks enthalten?

**Lösung:**

```sql
SELECT tl.id, tl.title
FROM task_lists tl
LEFT JOIN tasks t ON tl.id = t.task_list_id
WHERE t.id IS NULL;
```

➡️ `LEFT JOIN` sorgt dafür, dass alle Listen erscheinen — auch ohne Tasks.  
Die Bedingung `WHERE t.id IS NULL` filtert genau die Listen, bei denen kein einziger Task zugeordnet ist.

---

### 🧪 Hinweis zum Testen

Du kannst diese Abfragen direkt im Container ausführen und die Ergebnisse in pgAdmin4 vergleichen — z. B. ob deine „🧪 Testliste ohne Tasks“ korrekt mit `0` erscheint.

---

> ✅ Weiter mit [Modul 3: Subqueries](MODUL3.md)
