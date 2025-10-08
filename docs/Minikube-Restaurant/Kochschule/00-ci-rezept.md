[//]: # (docs/Minikube-Restaurant/Kochschule/00-ci-rezept.md)
# 🍸 CI-Rezept – Dev Compose automatisch prüfen

---

## 🧠 Ziel

Dieses Rezept zeigt, wie ich mit GitHub Actions meine `dev-compose`-Umgebung automatisch prüfe.  
Es ist Teil der CI-Bar und liefert mir Feedback, bevor ich mit CD beginne.

---

## 📋 Zutaten (Environment Variablen)

```yaml
POSTGRES_USER: admin
POSTGRES_DB: tasks_dev_db
POSTGRES_PASSWORD: adminpass
APP_DB_USER: dev_user
APP_DB_PASSWORD: dev_secret
DB_NAME: tasks_dev_db
DB_HOST: db
SPRING_PROFILES_ACTIVE: compose-dev
DB_PORT: 5432
APP_PORT: 8080
```

→ Diese Variablen werden im CI-Job gesetzt und steuern die Umgebung.

---

## 🔪 Zubereitungsschritte (CI-Workflow)

```yaml
on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]
```

→ Der CI-Job startet bei jedem Push oder Pull Request auf `main`.

---

## 🧪 Ablauf: Dev Compose prüfen

| Schritt                                  | Beschreibung                                  |
|------------------------------------------|-----------------------------------------------|
| 📥 Checkout Code                         | Holt den aktuellen Stand aus dem Repository   |
| 🐳 Set up Docker                         | Bereitet Docker für Compose vor               |
| 🧪 Starte Compose Umgebung               | Führt `make ci-dev-up` aus                    |
| 🌐 Verbindung zur App testen             | Führt `make ci-dev-debug` aus                 |
| 🐙 Überwache Compose-Container           | Zeigt `docker compose ps -a` mehrfach         |
| 📜 Zeige App-Logs                        | Zeigt Logs der App                            |
| 🧪 Healthcheck & Tasks-Endpoint prüfen   | Führt `make ci-dev-verify` aus                |
| 📜 Zeige App-Logs (erneut)               | Zweiter Blick in die Logs                     |
| 🧹 Stoppe Umgebung                       | Führt `make ci-dev-down` aus                  |

→ Jeder Schritt ist ein Gang im CI-Menü — modular, prüfbar, reproduzierbar.

---

## 🧠 Konzept: Warum CI vor CD?

Ich prüfe zuerst, ob alles funktioniert:

- Ist die App erreichbar?
- Sind die Container gesund?
- Gibt es Logs und Tasks?

✅ Erst, wenn alles im grünen Bereich ist, beginne ich mit CD.  
→ Siehe [`06-CD-SERVICE.md`](../Minikube-Kueche/06-CD-SERVICE.md)

---

## 🍽️ Serviervorschlag

Dieses Rezept ist die Grundlage für automatisiertes Feedback.  
Es läuft in der CI-Bar und prüft, ob die Küche bereit ist.

> 👨‍🍳 Für die vollständige CI-Bar siehe [`05-CI-BAR.md`](../Minikube-Kueche/05-CI-BAR.md)

---

> 👨‍🍳 Dieses Rezept wird laufend verfeinert — wie ein gutes Gericht.  
> Feedback willkommen 😄🍲
