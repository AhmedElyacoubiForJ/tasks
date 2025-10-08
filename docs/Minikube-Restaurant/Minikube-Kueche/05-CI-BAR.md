[//]: # (docs/Minikube-Restaurant/Minikube-Kueche/05-CI-BAR.md)
# 🍸 CI-Bar – GitHub Actions als Küchenautomatisierung

---

## 🧠 Konzept: Was passiert in der CI-Bar?

Die CI-Bar ist der Ort, an dem automatisch gekocht, serviert und gespült wird.  
Hier zeige ich, wie mein `dev-compose`-Gericht durch GitHub Actions zubereitet wird — Schritt für Schritt.

🔗 [CI-Lauf ansehen](https://github.com/AhmedElyacoubiForJ/tasks/actions/runs/18111814843/job/51539572311)

---

## 🍳 Ablauf des Gerichts – CI-Schritte

| Schritt                         | Dauer   | Beschreibung                              |
|---------------------------------|---------|-------------------------------------------|
| 🧃 Set up job                   | 1s      | Vorbereitung der Küche                    |
| 📦 Checkout Code               | 1s      | Zutaten holen                             |
| 🐳 Set up Docker               | 7s      | Herd anschließen                          |
| 🧬 Starte Compose Umgebung     | 1m 45s  | Gericht wird gekocht                      |
| 🧪 Überwache Compose-Container | 51s     | Qualitätssicherung                        |
| 📜 Zeige App-Logs              | 0s      | Blick in den Topf                         |
| 📋 Kombinierter Check          | 0s      | Healthcheck & Task-Endpoint               |
| 📜 Zeige App-Logs              | 0s      | Zweiter Blick in den Topf                 |
| 📂 Logdateien im Container     | 0s      | Optionaler Blick in die Speisekammer      |
| 🛑 Stoppe Umgebung             | 1s      | Herd ausschalten                          |
| 🐳 Post Docker Setup           | 1s      | Aufräumen                                 |
| 📦 Post Checkout               | 0s      | Zutaten zurücklegen                       |
| ✅ Complete job                | 0s      | Gericht serviert                          |

---

## 📸 Screenshots & Logs

Ich werde einzelne Schritte mit Screenshots dokumentieren — z.B.:

- Compose-Start
- Healthcheck
- App-Logs
- Erfolgreicher Abschluss

---

## 🍽️ Serviervorschlag

Die CI-Bar ist aktiv — sie kocht zuverlässig, reproduzierbar und mit Liebe zum Detail.  
Ab jetzt werden **tasks-Gerichte** automatisch zubereitet.

> 👨‍🍳 Willkommen in der neuen CI-Bar — hier wird nicht nur gekocht, sondern auch gespült 😄🍸
> 🧑‍🍳 Die Küche ist segmentiert, die Gäste können kommen.  
> Als Nächstes servieren wir die tasks Gerichte in [`ci-rezept.md`](../Kochschule/ci-rezept.md) 😄🍲