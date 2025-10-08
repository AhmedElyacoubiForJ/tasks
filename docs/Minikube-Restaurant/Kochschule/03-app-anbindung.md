[//]: # (docs/Minikube-Restaurant/Kochschule/03-app-anbindung.md)
# 🧑‍🍳 App-Anbindung an die Datenbank

## 🍽️ Ziel
Unsere App soll direkt mit der Postgres-Datenbank sprechen – ganz ohne Umwege.  
Wir rollen das Deployment mit dem richtigen Profil und den nötigen Umgebungsvariablen aus.

---

## 🧾 Zutaten

- Profil: `container-dev`
- Datenbank-Service: `postgres-dev`
- Umgebungsvariablen:
  - `DB_HOST=postgres-dev`
  - `DB_PORT=5432`
  - `DB_NAME=tasks_dev_db`
  - `APP_DB_USER=dev_user`
  - `APP_DB_PASSWORD=dev_secret`

---

## 🔧 Schritt-für-Schritt

### 1️⃣ App-Deployment ausrollen

```bash
kubectl apply -f ./k8s/koch-rezepte/tasks-app-deployment.yaml
```

→ Die App wird als Pod gestartet und versucht, sich mit der Datenbank zu verbinden.

---

### 2️⃣ Pod-Status prüfen

```bash
kubectl get pods -n dev
kubectl logs <pod-name> -n dev
```

✅ Wenn du siehst:
- `HikariPool-1 - Starting...`
- `✅ Demo-Listen erfolgreich gespeichert`

→ Dann ist die Datenbankverbindung erfolgreich!

---

### 3️⃣ Service hinzufügen

```bash
kubectl apply -f ./k8s/koch-rezepte/tasks-app-service.yaml
```

→ Der Service macht die App über Minikube erreichbar.

---

### 4️⃣ App testen

```bash
minikube ip
curl http://<minikube-ip>:30080/task-lists/summary
```

✅ Wenn du eine JSON-Antwort bekommst mit `status: success`, ist die App vollständig angebunden und servierbereit.

---

## 🧩 Szenario: Minikube-Neustart nach Rechnerstart

<details>
<summary>🔄 Was passiert nach <code>minikube start</code>?</summary>

### 1️⃣ Minikube starten

```bash
minikube start
```

→ Minikube startet seine VM oder Container-Engine neu.

---

### 2️⃣ Namespace & Pods prüfen

```bash
kubectl get ns
kubectl get pods -n dev
```

✅ Wenn `postgres-dev` und `tasks-app` wieder da sind:  
→ Die Umgebung wurde automatisch wiederhergestellt.

❌ Wenn keine Pods da sind:  
→ Minikube hat nur die Ressourcen gespeichert, aber keine Pods neu erstellt (z. B. bei `emptyDir` oder fehlendem Persistent Volume).

---

### 3️⃣ Services prüfen

```bash
kubectl get svc -n dev
```

→ Prüfen, ob `tasks-app` und `postgres-dev` noch als Services vorhanden sind.

---

### 4️⃣ App testen

```bash
minikube ip
curl http://<minikube-ip>:30080/task-lists/summary
```

✅ Wenn du eine JSON-Antwort bekommst:  
→ Die App ist servierbereit und spricht mit der Datenbank.

❌ Wenn Fehler kommen (z. B. `Connection refused`, `DB unreachable`):  
→ Vermutlich ist `postgres-dev` noch nicht da oder `tasks-app` wurde zu früh gestartet.

---

### 5️⃣ Neu einrollen (falls nötig)

```bash
kubectl apply -f ./k8s/koch-rezepte/init-db-script.yaml
kubectl apply -f ./k8s/koch-rezepte/db-deployment.yaml
kubectl apply -f ./k8s/koch-rezepte/tasks-app-deployment.yaml
kubectl apply -f ./k8s/koch-rezepte/tasks-app-service.yaml
```

→ Damit wird alles frisch ausgerollt — in der richtigen Reihenfolge.

</details>

---

## 🧼 Aufräumen

```bash
kubectl delete deployment tasks-app -n dev
kubectl delete svc tasks-app -n dev
```

→ Damit entfernst du die App und den Service aus dem Cluster.  
🧹 Deployment weg  
🚪 Service geschlossen  
📦 Küche sauber für den nächsten Gang
