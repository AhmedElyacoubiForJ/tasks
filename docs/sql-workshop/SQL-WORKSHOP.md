# 🧠 SQL-WORKSHOP: Task-Projekt

> Willkommen zum SQL-Workshop für dein Task-Projekt.
> Jedes Modul behandelt ein zentrales Thema und ist über einen Link direkt erreichbar.

---

### 🧮 1. Aggregationen & GROUP BY → [MODUL1](moduls/MODUL1.md)
- Zähle Tasks pro Liste
- Zeige nur Listen mit mehr als X Tasks
- Durchschnittliche Länge von Task-Titeln pro Liste

---

### 🔍 2. LEFT JOIN mit COUNT → [MODUL2](moduls/MODUL2.md)
- Alle Listen inklusive Task-Anzahl (auch 0)
- Sortiere nach Anzahl der Tasks absteigend
- Listen mit null Tasks gezielt anzeigen

---

### 🧠 3. Subqueries → [MODUL3](moduls/MODUL3.md)
- Listen mit mindestens einer offenen Task
- Tasks aus der Liste mit den meisten Tasks
- Listen, deren Tasks alle den Status 'DONE' haben

---

### 🪜 4. Window Functions → [MODUL4](moduls/MODUL4.md)
- Rangiere Tasks innerhalb ihrer Liste nach Erstellungsdatum
- Zeige die „neueste Task“ pro Liste

---

### 🧪 5. Datenqualität & Validierung → [MODUL5](moduls/MODUL5.md)
- Tasks ohne gültige Liste
- Listen ohne Titel oder mit leerer Beschreibung

### ✅ Modul6 separat:
> Wenn du dich auf Validierungs-Dashboards konzentrieren willst — also z.B. SQL-Views, Reports, oder UI-Tabellen, die Datenqualität visualisieren — dann verdient das ein eigenes Modul. Du kannst dort z. B. zeigen:

- Listen mit fehlenden Feldern
- Tasks mit ungültigem Status
- Anzahl fehlerhafter Einträge pro Typ
- SQL-Views zur automatisierten Prüfung

> Das ist ein praktisches Anwendungsmodul, das auf Modul5 aufbaut — aber mit Fokus auf Visualisierung und Automatisierung.

### ✅ Modul7 separat:
> Wenn du später Testdaten generieren willst — z.B. mit INSERT, generate_series(), oder sogar pgcrypto für UUIDs — dann ist das ein eigener Bereich. Du kannst dort zeigen:

- Wie man realistische Daten erzeugt
- Wie man gezielt fehlerhafte Daten simuliert
- Wie man Daten für Workshops oder Unit-Tests vorbereitet

> Das ist ein technisches Setup-Modul, das unabhängig von den Validierungsstrategien funktioniert.

### 📘 Fazit
- Modul6 → Validierungs-Dashboard & Views
- Modul7 → Testdaten-Generierung & Simulation

> Beide sind sinnvoll getrennt, weil sie unterschiedliche Ziele verfolgen: Analyse vs. Datenbasis

## 📁 Strukturhinweis

> Alle Module liegen im Verzeichnis `/sql/` und sind als einzelne `.md`-Dateien abgelegt.
> Man kannst sie direkt bearbeiten, erweitern oder mit Beispieldaten versehen.

---

## ✅ Nächste Schritte

- [ ] Modul 6 hinzufügen?
- [ ] Beispiel-Datenbank-Schema dokumentieren?
- [ ] SQL-Testdaten generieren?