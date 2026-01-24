# 🗂️ Task Management API

**Version:** v1.0-demo  
**Lizenz:** MIT License  
**Beschreibung:**  
Eine Demo-API zur Verwaltung von Aufgabenlisten und Tasks. Erstellt als Showcase für Arbeitgeber und zur Präsentation im Portfolio.

---

## 🚀 Features
- CRUD-Endpunkte für TaskLists
- Subresource-Endpunkte für Tasks innerhalb einer TaskList
- Konsistente APIResponse-Struktur mit Status, Message und Timestamp
- Vollständig dokumentiert mit Swagger (OpenAPI 3.0)

---

## 📖 Swagger-Dokumentation
Nach dem Start der Anwendung erreichbar unter:  
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🏗️ Architekturübersicht

```text
+-------------------+        +-------------------+
|   TaskLists API   |        |   Tasks API       |
| (CRUD Endpoints)  |        | (Subresource)     |
+---------+---------+        +---------+---------+
          |                            |
          v                            v
+-------------------+        +-------------------+
|   TaskList DTO    |        |     Task DTO      |
|   (List details)  |        | (Task details)    |
+---------+---------+        +---------+---------+
          |                            |
          +-------------+--------------+
                        v
              +-------------------+
              |   APIResponse     |
              | (Wrapper: status, |
              |  message, data,   |
              |  timestamp)       |
              +-------------------+
```

---

## 👤 Kontakt
- Autor: Ahmed

- GitHub: https://github.com/dein-github

- E-Mail: dein.email@portfolio.com

---

## 📜 Lizenz
Dieses Projekt steht unter der MIT License. Die Nutzung ist kostenlos und frei, auch für kommerzielle Zwecke.

---