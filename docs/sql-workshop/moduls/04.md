# 🪜 Modul4: Window Functions  
**Frage → Lösung**

> In diesem Modul lernst du, wie man Tasks innerhalb ihrer jeweiligen Liste analysiert — z.B. durch Ranking oder Auswahl der neuesten Einträge.

---

### 📊 Aufgabe 1: Ranking der Tasks nach Erstellungsdatum

**Frage:**  
Wie kann man Tasks innerhalb ihrer Liste nach dem Erstellungsdatum sortieren und ihnen einen Rang geben?

**Lösung:**

```sql
SELECT
  task_id,
  list_id,
  created_at,
  ROW_NUMBER() OVER (
    PARTITION BY list_id
    ORDER BY created_at ASC
  ) AS task_rank
FROM tasks;
```

➡️ `ROW_NUMBER()` vergibt eine eindeutige Rangnummer pro Task innerhalb jeder Liste.  
`PARTITION BY list_id` gruppiert die Tasks nach Liste, `ORDER BY created_at ASC` sortiert sie chronologisch.

---

### 🏆 Aufgabe 2: Neueste Task pro Liste anzeigen

**Frage:**  
Wie zeigt man pro Liste nur die jeweils neueste Task?

**Lösung:**

```sql
WITH ranked_tasks AS (
  SELECT
    task_id,
    list_id,
    created_at,
    ROW_NUMBER() OVER (
      PARTITION BY list_id
      ORDER BY created_at DESC
    ) AS rn
  FROM tasks
)
SELECT *
FROM ranked_tasks
WHERE rn = 1;
```

➡️ Die CTE (`WITH`) erstellt eine Rangliste pro Liste, sortiert nach Erstellungsdatum absteigend.  
`WHERE rn = 1` filtert die jeweils neueste Task pro Liste heraus.

---

### 🧠 Bonus: Alternative mit `MAX()`

**Frage:**  
Wie zeigt man nur das Datum des neuesten Tasks pro Liste — ohne Window Function?

**Lösung:**

```sql
SELECT
  list_id,
  MAX(created_at) AS latest_task_date
FROM tasks
GROUP BY list_id;
```

➡️ Diese Variante zeigt nur das Datum, nicht die vollständig Task.  
Für komplexere Anforderungen sind Window Functions deutlich flexibler.

---

> ✅ Weiter mit [Modul5: Datenqualität & Validierung](MODUL5.md)