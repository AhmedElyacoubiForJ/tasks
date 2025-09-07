# 🧮 Modul1: Aggregationen & GROUP BY  
**Frage → Lösung**

> In diesem Modul lernst du, wie man Daten gruppiert und zusammenfasst — z.B. Task-Zählung pro Liste oder Durchschnittswerte.

---

### 🧮 Aufgabe 1: Anzahl der Tasks pro Task-Liste

**Frage:**  
Wie viele Tasks gehören jeweils zu welcher Task-Liste?

**Lösung:**

```sql
SELECT task_list_id, COUNT(*) AS anzahl_tasks
FROM tasks
GROUP BY task_list_id;
```

➡️ Gruppiert alle Tasks nach ihrer zugehörigen Liste und zählt die Anzahl pro Gruppe.

---

### 🔍 Aufgabe 2: Nur Listen mit mehr als 3 Tasks anzeigen

**Frage:**  
Welche Task-Listen haben mehr als 3 Tasks?

**Lösung:**

```sql
SELECT task_list_id, COUNT(*) AS anzahl_tasks
FROM tasks
GROUP BY task_list_id
HAVING COUNT(*) > 3;
```

➡️ `HAVING` filtert die Gruppen nach Aggregatwerten — hier: nur Listen mit mehr als 3 Tasks.

---

### 📏 Aufgabe 3: Durchschnittliche Länge der Task-Titel pro Liste

**Frage:**  
Wie lang sind die Task-Titel im Durchschnitt — pro Liste?

**Lösung:**

```sql
SELECT task_list_id, AVG(LENGTH(title)) AS durchschnitt_titel_laenge
FROM tasks
GROUP BY task_list_id;
```

➡️ Misst die Länge jedes Titels mit `LENGTH(title)` und berechnet den Durchschnitt pro Liste.

---

### 🎯 Formatierung auf 2 Nachkommastellen

#### 🔹 Option 1: Numerisch mit `ROUND(...)`

```sql
SELECT task_list_id, ROUND(AVG(LENGTH(title)), 2) AS durchschnitt_titel_laenge
FROM tasks
GROUP BY task_list_id;
```

➡️ Rundet den Durchschnitt auf zwei Dezimalstellen — ideal für Berechnungen.

#### 🔹 Option 2: Textbasiert mit `TO_CHAR(...)`

```sql
SELECT task_list_id, TO_CHAR(AVG(LENGTH(title)), 'FM999999990.00') AS durchschnitt_titel_laenge
FROM tasks
GROUP BY task_list_id;
```

➡️ Gibt die Zahl als formatierten Text zurück — z.B. `"11.00"` — ideal für Reports oder UI-Darstellung.

---

### 🧠 Wann welche Variante?

| Funktion   | Typ        | Vorteil                         |
|------------|------------|----------------------------------|
| `ROUND()`  | numerisch  | Weiterverarbeitung möglich       |
| `TO_CHAR()`| text       | Schönes Format für Anzeige       |

---

> ✅ Weiter mit [Modul2: LEFT JOIN mit COUNT](MODUL2.md)