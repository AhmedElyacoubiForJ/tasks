# 🧠 SQL-WORKSHOP: Task-Projekt  
_Dokumentation & Lernmodule für strukturierte SQL-Analyse_

---

## 📘 Projektbeschreibung

Dieses SQL-Toolkit begleitet das Task-Projekt mit modular aufgebauten Lern- und Analysebausteinen. Ziel ist es, typische SQL-Herausforderungen systematisch zu lösen — von Aggregationen über Datenvalidierung bis hin zur Testdaten-Simulation.

Die Module sind unabhängig voneinander nutzbar und eignen sich sowohl für Lernzwecke als auch für die Qualitätssicherung im Projekt.

---

## 🛠️ Setup-Hinweise

- PostgreSQL ≥ 14 empfohlen  
- pgAdmin4 oder DBeaver als GUI  
- `pgcrypto`-Extension aktivieren (für UUIDs):  
  ```sql
  CREATE EXTENSION IF NOT EXISTS pgcrypto;
  ```

- Tabellenstruktur:
    - `task_lists(id UUID, title TEXT, description TEXT)`
    - `tasks(task_id UUID, task_list_id UUID, title TEXT, status TEXT, created_at TIMESTAMP)`

---

## 📚 Modulübersicht

| Modul | Thema                              | Link                         |
|-------|------------------------------------|------------------------------|
| 1     | Aggregationen & GROUP BY           | [MODUL1](./moduls/MODUL1.md) |
| 2     | LEFT JOIN mit COUNT                | [MODUL2](./moduls/MODUL2.md)   |
| 3     | Subqueries                         | [MODUL3](./moduls/MODUL3.md)   |
| 4     | Window Functions                   | [MODUL4](./moduls/MODUL4.md)   |
| 5     | Datenqualität & Validierung        | [MODUL5](./moduls/MODUL5.md)   |
| 6     | Validierungs-Dashboard & Views     | [MODUL6](./moduls/MODUL6.md)   |
| 7     | Testdaten-Generierung & Simulation | [MODUL7](./moduls/MODUL7.md)   |

---

## 🧪 Nutzungshinweise

- Alle Module enthalten konkrete Aufgaben mit SQL-Lösungen
- Du kannst die Abfragen direkt im Container oder GUI testen
- Views aus Modul6 lassen sich in Frontend-Dashboards einbinden
- Modul7 bietet realistische und fehlerhafte Testdaten für Validierung

---

## 🚀 Erweiterungsideen

- Modul8: Performance-Tuning (z.B. Indexe, EXPLAIN)
- Modul9: Stored Procedures & Trigger
- Modul10: Sicherheit & Rollenmanagement

---

## 👤 Autor

> Dokumentiert von A. El-Yacoubi  
> SQL-Workshop für das Task-Projekt  
> Standort: Essen, Deutschland