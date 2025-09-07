# 📊 Modul6: Validierungs-Dashboard & Views  
**Frage → Lösung**

> In diesem Modul lernst du, wie man die Datenqualität systematisch sichtbar macht — z.B. durch SQL-Views, Reports oder UI-Tabellen, die fehlerhafte oder unvollständige Daten aufspüren.

---

### 🧪 Aufgabe 1: View für Listen mit fehlenden Feldern

**Frage:**  
Wie erstellt man eine View, die alle Task-Listen ohne Titel oder mit leerer Beschreibung zeigt?

**Lösung:**

```sql
CREATE OR REPLACE VIEW view_leere_listen AS
SELECT *
FROM task_lists
WHERE title IS NULL
   OR TRIM(description) = '';
```

➡️ Diese View zeigt alle Listen mit fehlendem Titel oder leerer Beschreibung.  
Sie kann regelmäßig abgefragt oder in einem UI-Dashboard eingebunden werden.

---

### ❌ Aufgabe 2: View für Tasks mit ungültigem Status

**Frage:**  
Wie zeigt man Tasks, deren Status nicht zu den erlaubten Werten gehört?

**Lösung:**

```sql
CREATE OR REPLACE VIEW view_ungueltige_tasks AS
SELECT *
FROM tasks
WHERE status NOT IN ('OPEN', 'IN_PROGRESS', 'DONE');
```

➡️ Diese View zeigt Tasks mit fehlerhaftem Status — z.B. durch Tippfehler oder falsche Eingabe.

---

### 📊 Aufgabe 3: Übersicht der Fehleranzahl pro Typ

**Frage:**  
Wie zeigt man die Anzahl fehlerhafter Einträge pro Kategorie?

**Lösung:**

```sql
SELECT 'Leere Listen' AS fehler_typ, COUNT(*) AS anzahl
FROM task_lists
WHERE title IS NULL OR TRIM(description) = ''

UNION ALL

SELECT 'Ungültige Tasks', COUNT(*)
FROM tasks
WHERE status NOT IN ('OPEN', 'IN_PROGRESS', 'DONE');
```

➡️ Diese Abfrage liefert eine kompakte Übersicht über die Fehleranzahl — ideal für Dashboards oder Monitoring.

---

### 🧠 Bonus: Automatisierte Prüfung mit UI-Integration

Wenn du ein Frontend hast (z.B. Spring Boot + Thymeleaf oder React), kannst du diese Views direkt einbinden:

- `view_leere_listen` → Tab „Unvollständige Listen“
- `view_ungueltige_tasks` → Tab „Fehlerhafte Tasks“
- Fehleranzahl → Badge oder Alert-Komponente

➡️ So wird Datenqualität nicht nur geprüft, sondern **sichtbar gemacht**.

---

> ✅ Weiter mit [Modul7: Testdaten-Generierung & Simulation](MODUL7.md)