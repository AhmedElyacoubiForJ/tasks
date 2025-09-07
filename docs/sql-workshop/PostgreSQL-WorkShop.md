> PostgreSQL-Workshop — die Wichtigsten feststellungen zur Indexverwendung bei kleinen Datenmengen.
> Direkt auf den Punkt 💡

## 🧑‍💻 PostgreSQL Workshop: Tabellenstruktur, Indexe & Scan-Typen analysieren

---

### 🟢 1. Zugriff auf den PostgreSQL-Container

```bash
docker exec -it postgres-db psql -U postgres -d tasks_db
```

Du bist jetzt im `psql`-Prompt:  
`tasks_db=#`

---

### 📋 2. Tabellen anzeigen

```sql
\dt
```

→ Zeigt alle Tabellen im aktuellen Schema.

---

### 🔍 3. Struktur einer Tabelle prüfen

```sql
\x
\d tasks
```

→ Zeigt Spalten, Datentypen, Constraints & vorhandene Indexe.

---

### 🧱 4. Index erstellen

```sql
CREATE INDEX idx_tasks_task_list_id ON tasks(task_list_id);
```

→ Erstellt einen B-Tree-Index auf die Spalte `task_list_id`.

---

### 📊 5. Index-Verwendung analysieren

```sql
EXPLAIN ANALYZE SELECT * FROM tasks WHERE task_list_id = 'deine-id';
```

---

### 🔎 6. Scan-Typen verstehen

PostgreSQL entscheidet automatisch, wie es Daten scannt. Die wichtigsten Typen:

| Scan-Typ         | Beschreibung                                                                 |
|------------------|------------------------------------------------------------------------------|
| **Seq Scan**     | Sequentielles Durchlaufen der gesamten Tabelle. Schnell bei kleinen Daten.  |
| **Index Scan**   | Nutzt einen vorhandenen Index, um gezielt zu suchen. Effizient bei großen Datenmengen. |
| **Bitmap Index Scan** | Kombiniert mehrere Indexe, um komplexe Bedingungen effizient zu erfüllen. |
| **Tid Scan**     | Greift direkt auf eine bestimmte Zeile per Tuple-ID zu. Selten verwendet.   |

---

### ⚠️ 7. Wichtige Feststellung zur Index-Nutzung

Auch wenn du einen Index erstellt hast, heißt das **nicht automatisch**, dass PostgreSQL ihn verwendet.  
➡️ Bei kleinen Tabellen bevorzugt PostgreSQL oft einen **Seq Scan**, weil der Overhead für den Indexzugriff sich nicht lohnt.

---

### 🧪 8. Index-Nutzung erzwingen (nur zu Testzwecken!)

```sql
SET enable_seqscan = OFF;
EXPLAIN ANALYZE SELECT * FROM tasks WHERE task_list_id = 'deine-id';
```

→ Jetzt wird der Index verwendet (`Index Scan ...`)  
✅ Du hast festgestellt, dass der Index tatsächlich greift — aber:

⚠️ **Nicht in Produktion verwenden!**  
Das Deaktivieren von `enable_seqscan` kann zu schlechter Performance führen.

🔄 Danach den Default wiederherstellen:

```sql
SET enable_seqscan = ON;
```

---

### 🧠 9. Erweiterte Index-Tipps

- **Partial Index**
  ```sql
  CREATE INDEX idx_open_tasks ON tasks(task_list_id) WHERE status = 'OPEN';
  ```

- **Kombinierter Index**
  ```sql
  CREATE INDEX idx_tasklist_status ON tasks(task_list_id, status);
  ```

- **Statistik prüfen**
  ```sql
  SELECT * FROM pg_stat_user_indexes WHERE relname = 'tasks';
  ```

- **Physisch sortieren nach Index**
  ```sql
  CLUSTER tasks USING idx_tasks_task_list_id;
  ```

- **Statistiken aktualisieren**
  ```sql
  ANALYZE tasks;
  ```

---

## ✅ Fazit

Man hat jetzt:

- Zugriff auf die Datenbank im Container
- Struktur- und Indexanalyse im Griff
- Alle relevanten Scan-Typen verstanden
- Gelernt, wie PostgreSQL Indexe entscheidet und wie man das Verhalten testet
- Tools zur Optimierung der Queries
