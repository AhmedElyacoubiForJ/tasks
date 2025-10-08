[//]: # (docs/Minikube-Restaurant/Kochschule/logistik/docker-kuechen-wechsel.md)
# 🧑‍🍳 Docker-Küchen-Wechsel: Lokal ↔ Minikube

📍 Ort: `Minikube-Restaurant/Kochschule/kuechen-Logistik/docker-küchen-wechsel.md`

---

## 🍽️ Ziel
Wir wollen flexibel zwischen zwei Docker-Welten wechseln:

- **Lokale Küche** → dein normales Docker auf dem Host  
- **Minikube-Küche** → Docker-Engine innerhalb von Minikube

So kannst du gezielt Images bauen, testen und deployen — je nach Umgebung.

---

## 🧾 Zutaten

- Minikube ist installiert und gestartet
- Shell mit Zugriff auf `eval` (z.B. Bash, Zsh)
- Docker ist lokal installiert

---

## 🔄 Methode A: Vollständiger Wechsel mit `-p minikube`

```bash
eval $(minikube -p minikube docker-env)
```

✅ Danach wirken alle Docker-Befehle auf die Minikube-Engine:

```bash
docker images         # zeigt nur Minikube-Images
docker build ...      # landet direkt in Minikube
docker ps             # zeigt Minikube-Container
```

🧼 Zurück zur lokalen Docker-Küche:

```bash
eval $(minikube -p minikube docker-env --unset)
```

---

## 🔄 Methode B: Vereinfachter Wechsel (Standard-Profil)

```bash
eval $(minikube docker-env)
```

✅ Funktioniert genauso — **wenn du nur ein Minikube-Profil** verwendest  
→ Greift automatisch auf das aktive Profil zu (meist `minikube`)

🧼 Zurücksetzen:

```bash
eval $(minikube docker-env --unset)
```

---

## ⚖️ Vergleich der Methoden

| Merkmal                     | Methode A (`-p minikube`)       | Methode B (vereinfacht)         |
|----------------------------|----------------------------------|----------------------------------|
| Profil explizit            | ✅ Ja                            | ❌ Nein                          |
| Mehrere Minikube-Profile   | ✅ Unterstützt                   | ⚠️ Nicht empfohlen               |
| Klarheit & Kontrolle       | ✅ Höher                         | ✅ Schnell, aber weniger präzise |
| Empfehlung für Skripte     | ✅ Methode A                     | ⚠️ Methode B nur bei 1 Profil    |

---

## 🧪 Test: Küchenwechsel sichtbar machen

```bash
docker images | grep mein-app
```

→ Wenn du das Image **nur in einer Umgebung siehst**, ist der Wechsel erfolgreich

---

## 🧩 Aufklappbarer Abschnitt: Tasks lokal bauen & servieren

<details>
<summary>🐳 Lokale Docker-Umgebung starten & testen</summary>

### 1️⃣ App servieren

```bash
make docker-dev-up
```

→ Danach im Browser öffnen:

```
http://localhost:8080/task-lists/summary
```

✅ Wenn du eine JSON-Antwort bekommst: Die lokale Küche läuft!

---

### 2️⃣ App stoppen

```bash
make docker-dev-down
```

→ Beendet alle laufenden Container und räumt auf.

---

### 3️⃣ App neustarten

```bash
make docker-dev-restart
```

→ Stoppt und startet die Umgebung neu.

---

### 4️⃣ Logs anzeigen

```bash
make docker-dev-logs
```

→ Zeigt dir die aktuellen Logs der App und Datenbank.

</details>
