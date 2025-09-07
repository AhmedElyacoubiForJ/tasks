# 🧪 Modul5: Datenqualität & Validierung  
**Frage → Lösung**

> In diesem Modul lernst du, wie man fehlerhafte oder unvollständige Daten erkennt — z.B. ungültige Fremdschlüssel oder fehlende Inhalte — und wie man sie gezielt bereinigt.

---

### ❌ Aufgabe 1: Tasks ohne gültige Liste

**Frage:**  
Wie erkennt man Tasks, die auf keine existierende Liste verweisen?

**Lösung:**

```sql
SELECT *
FROM tasks t
LEFT JOIN task_lists l ON t.task_list_id = l.id
WHERE l.id IS NULL;
```

➡️ `LEFT JOIN` holt alle Tasks, auch wenn keine passende Liste existiert.  
`WHERE l.id IS NULL` filtert die Tasks heraus, deren `task_list_id` auf keine gültige Liste zeigt → **ungültige Referenz**.

---

### 📝 Aufgabe 2: Listen ohne Titel oder mit leerer Beschreibung

**Frage:**  
Wie erkennt man Listen, die keinen Titel haben oder deren Beschreibung leer ist?

**Lösung:**

```sql
SELECT *
FROM task_lists
WHERE title IS NULL
   OR TRIM(description) = '';
```

➡️ `title IS NULL` prüft auf fehlenden Titel.  
`TRIM(description) = ''` erkennt leere oder nur aus Leerzeichen bestehende Beschreibungen.

---

### 🛠️ Bonus: Datenqualität verbessern

**Frage:**  
Wie kann man leere Beschreibungen automatisch auffüllen?

**Lösung:**

```sql
UPDATE task_lists
SET description = 'Keine Beschreibung vorhanden'
WHERE TRIM(description) = '';
```

➡️ Füllt leere Beschreibungen mit einem Standardtext.

**Optional: Constraints beim Tabellendesign**

```sql
CREATE TABLE task_lists (
  id UUID PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT DEFAULT 'Keine Beschreibung vorhanden'
);
```

➡️ Verhindert fehlende Titel und setzt eine Standardbeschreibung beim Einfügen.

---

> ✅ Zurück zur [Übersicht](../SQL-WORKSHOP.md) oder weiter mit Modul6

[//]: # (Wenn du magst, kann ich dir auch ein kleines Validierungs-Dashboard entwerfen oder ein paar Testdaten generieren, um das Ganze interaktiv zu machen. Bereit für Modul6? 😄)