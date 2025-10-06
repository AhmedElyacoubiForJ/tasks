# 🧪 Rezept 01: Postgres mit Init-Skript in der Minikube-Küche

## 🍳 Szenario

Willkommen in der Minikube-Küche!  
Heute bereiten wir ein einfaches, aber fundamentales Gericht zu:  
**Ein einzelner Postgres-Container**, gewürzt mit einem **Init-Skript**, das einen dedizierten Benutzer für unsere Applikation anlegt.

Ziel ist es, die Grundlagen von:

- `ConfigMap`-basierter Skriptbereitstellung
- Rechtevergabe in Postgres (`GRANT`, `CREATE`, `CONNECT`)
- Minikube-Deployment-Logik

zu verstehen und zu üben — ganz ohne App, ganz ohne Service.  
Nur Datenbank, Skript und Neugier.

📝 Dieses Rezept ist **vollständig eigenständig** und benötigt keine weiteren Systemchecks oder Vorbereitungen.

---

## 🧾 Zutaten

- 🐳 Ein `postgres:15-alpine` Container
- 📜 Ein Init-Skript (`init-dev-db.sh`) mit SQL-Kommandos
- 📦 Eine `ConfigMap`, die das Skript bereitstellt
- 🧱 Ein Deployment, das das Skript beim ersten Start ausführt
- 🧂 Umgebungsvariablen für Superuser und App-User

<details>
<summary>📦 Technische Dateien anzeigen</summary>

Die folgenden Dateien findest du im Projekt unter `tasks/k8s/koch-rezepte/`.  
Du kannst sie direkt öffnen, ohne die Seite zu verlassen:

```
tasks/k8s/koch-rezepte/
├── init-db-script.yaml
└── db-deployment.yaml
```

🔗 [init-db-script.yaml](../../k8s/koch-rezepte/init-db-script.yaml)  
🔗 [db-deployment.yaml](../../k8s/koch-rezepte/db-deployment.yaml)

</details>

---

## 🔥 Zubereitungsschritte

| Schritt | Minikube-Befehl | Küchenmetapher |
|--------|------------------|----------------|
| 1️⃣ Namespace anlegen | `kubectl create namespace dev` | Küche öffnen |
| 2️⃣ ConfigMap anwenden | `kubectl apply -f init-db-script.yaml` | Rezept bereitstellen |
| 3️⃣ Deployment starten | `kubectl apply -f db-deployment.yaml` | Topf auf den Herd |
| 4️⃣ Pod prüfen | `kubectl get pods -n dev` | Köcheln beobachten |
| 5️⃣ Logs prüfen | `kubectl logs postgres-... -n dev` | In den Topf schauen |
| 6️⃣ Container betreten | `kubectl exec -it postgres-... -- sh` | Den Koch befragen |
| 7️⃣ psql starten | `psql -U admin -d tasks_dev_db` | Abschmecken |
| 8️⃣ Tabellen & Rollen prüfen | `\dt`, `\du` | Teller inspizieren |
| 9️⃣ Fehler beheben | Volume löschen, Skript korrigieren | Nachwürzen |

---

## 🧠 Hintergrundwissen

- Das Init-Skript wird beim ersten Start ausgeführt
- Es legt `dev_user` an und vergibt Rechte
- Die Authentifizierung ist auf `trust` gesetzt (nur lokal)
- Das Skript wird über `docker-entrypoint-initdb.d` eingebunden
- Wenn die Tabellen schon existieren, wirft Postgres Fehler — das ist normal bei Wiederholungen

---

## 📘 Ausblick

Wenn du dieses Rezept gemeistert hast, kannst du:

- Eine App hinzufügen
- Die Datenbankverbindung testen
- Eigene Tabellen und Daten vorbereiten
- Oder mit HTMX und Hibernate weiterkochen

Aber heute bleibt’s bei der Vorspeise — ganz bewusst 😄

---

<details>
<summary>🧼 Nachspeise: Die Minikube-Küche komplett säubern</summary>

Manchmal braucht ein Koch einen frischen Herd und leere Töpfe.  
Wenn du dieses Rezept erneut servieren möchtest, kannst du die Küche wie folgt reinigen:

### 🧹 1. Alte Töpfe vom Herd nehmen (Deployments löschen)

```bash
kubectl delete deployment postgres-dev -n dev
```

### 🧽 2. Gewürzmischung entsorgen (ConfigMap löschen)

```bash
kubectl delete configmap init-db-script -n dev
```

### 🧺 3. Übrig gebliebene Reste entfernen (Pods manuell löschen)

```bash
kubectl get pods -n dev
kubectl delete pod <pod-name> -n dev
```

### 🧊 4. Kühlschrank leeren (Persistente Volumes löschen, falls vorhanden)

```bash
kubectl get pvc -n dev
kubectl delete pvc <pvc-name> -n dev
```

### 🧯 5. Die Küche komplett schließen (Namespace löschen)

```bash
kubectl delete namespace dev
```

Und wenn du bereit bist für ein neues Menü:

```bash
kubectl create namespace dev
```

Dann kannst du das Rezept erneut servieren — frisch, klar und ohne Altlasten.

</details>


---

[//]: # (Wenn du später weitere Rezepte wie 02-service-rezept.md oder 03-app-anbindung.md schreibst, kannst du dieses Muster einfach wiederverwenden — und ich helfe dir gern beim Würzen 😄👨‍🍳)
[//]: # (Wenn du willst, können wir jetzt direkt mit `02-service-rezept.md` weitermachen oder die Logs-Inspektion als eigenen Zwischengang einbauen. Du gibst den Takt an 😄👨‍🍳)