[//]: # (01-KUECHE.md)
# 🏠 Kücheneinrichtung – Minikube-Küche à la „Dev-Küche“

Willkommen in der Küche!  
Bevor wir kochen, richten wir die Arbeitsfläche ein: Herd, Werkzeuge, Zutatenlager.  
In DevOps-Sprache heißt das: Minikube, Docker, kubectl.

---

## 🧪 Zutatencheck (Systemanalyse)

Bevor du loslegst, prüfe dein Küchengerüst — sonst wackelt der Herd später:

```bash
uname -m                      # Architektur prüfen → x86_64
lscpu                         # CPU-Modus & Vendor checken
dpkg --print-architecture     # Paketarchitektur → amd64
```

🔍 Ziel: Eine stabile, kompatible Umgebung für Minikube.  
Wenn du z.B. auf ARM64 kochst, brauchst du andere Zutaten (Images, Treiber).

---

## 🔧 Minikube installieren – der Herd kommt rein

```bash
curl -LO https://storage.googleapis.com/minikube/releases/v1.32.0/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
which minikube                # → /usr/local/bin/minikube
minikube version              # Version prüfen
```

🛠️ Du installierst den Herd — Minikube ist Deine Kochplattform.  
Version 1.32.0 ist stabil und kompatibel mit modernen Docker-Setups.

---

## 🚀 Minikube starten – Herd anschalten

```bash
minikube start --driver=docker
minikube status
```

🔥 Jetzt wird’s heiß: Der Herd läuft, die Küche brodelt.  
Der Docker-Driver ist wie dein Gasanschluss — zuverlässig und lokal steuerbar.

---

## 🛠️ kubectl installieren – Dein Kochlöffel

```bash
sudo snap install kubectl --classic
kubectl version --client
kubectl get pods -A
```

🥄 `kubectl` ist dein Kochlöffel — damit rührst du im Cluster, prüfst Zutaten und richtest an.  
Snap-Installation ist schnell und sauber, ideal für Ubuntu-Küchen.

---

## 🍽️ Serviervorschlag

Minikube läuft, kubectl spricht, und der Cluster brodelt wie ein gutes Curry.  
Jetzt kannst du:

- Deployments anrichten
- Services garnieren
- CI/CD würzen
- Und Gäste (Arbeitgeber) einladen, dein Menü zu kosten

---

> 🧑‍🍳 Die Messer sind geschärft, die Pfannen heiß — Zeit, die Stationen zu eröffnen!  
> Wir gehen jetzt zu [`./02-STATIONEN.md`](02-STATIONEN.md) und richten unsere Restaurantbereiche ein.  
> Achtung: In der Grillstation wird’s heiß 😄🔥
