[//]: # (05-CI-BAR.md)

# 🍸 CI-Bar – Automatisch kochen, servieren & spülen

---

## 🥂 Willkommen in der CI-Bar

Hier wird nicht mehr manuell geschnippelt – hier wird automatisch gekocht.  
Sobald ein neues Rezept (Code) eintrifft, startet die Maschine:

- Zutaten prüfen (Tests)
- Gericht bauen (Build)
- Kellner losschicken (Deploy)

→ Willkommen bei **CI/CD mit GitHub Actions** – elegant, reproduzierbar und teamfähig.

---

## 🔥 Aufwärmen: Ist die Küche bereit?

```bash
minikube status
kubectl config current-context
kubectl get nodes
kubectl get namespaces
kubectl get pods -A
kubectl get deployments -A
kubectl get svc -A
```

Falls alles leer ist, richte das Rezept neu an:

```bash
kubectl apply -f CI-Bar/tasks-deployment.yaml
kubectl apply -f CI-Bar/tasks-service.yaml
```

---

## 🧠 Konzept: Was ist CI/CD?

- **CI (Continuous Integration)**: Neue Zutaten werden geprüft, bevor sie in die Küche kommen
- **CD (Continuous Deployment)**: Sobald das Rezept passt, wird automatisch gekocht und serviert

→ Du brauchst nur ein Git-Repository und ein Workflow-File – der Rest läuft wie von selbst.

---

## ⚠️ Struktur-Hinweis

> Deine produktive CI läuft in `tasks/.github/workflows/ci.yml` und testet deine Compose-Umgebung.  
> Diese CI-Bar ist ein **Trainingsbereich**, um CD mit Minikube zu verstehen.  
> Sie berührt dein echtes `ci.yml` **nicht**.

---

## 🛠️ Beispiel: Deployment deiner App „tasks-salat“

📁 `CI-Bar/tasks-deployment.yaml`  
📁 `CI-Bar/tasks-service.yaml`

→ Diese Dateien enthalten dein echtes Rezept – angepasst an deine App.

```bash
kubectl apply -f CI-Bar/tasks-deployment.yaml
kubectl apply -f CI-Bar/tasks-service.yaml
```

→ Danach kannst du mit `curl` oder `minikube service` prüfen, ob alles serviert wird.

---

## 📦 Beispiel-Manifest: `tasks-deployment.yaml`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: tasks-salat
  namespace: frontend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: tasks-salat
  template:
    metadata:
      labels:
        app: tasks-salat
    spec:
      containers:
      - name: tasks-app
        image: ghcr.io/dein-user/tasks-app:latest # ← dein echtes Image hier!
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: compose-dev
        - name: DB_HOST
          value: db
        - name: DB_PORT
          value: "5432"
        - name: DB_NAME
          value: tasks_dev_db
        - name: APP_DB_USER
          value: dev_user
        - name: APP_DB_PASSWORD
          value: dev_secret
```

---

## 🧑‍🍽 Service: `tasks-service.yaml`

```yaml
apiVersion: v1
kind: Service
metadata:
  name: tasks-service
  namespace: frontend
spec:
  type: NodePort
  selector:
    app: tasks-salat
  ports:
  - port: 8080
    targetPort: 8080
    nodePort: 30080
```

---

## 🧪 Übungsteil: Trigger & Rollout prüfen

```bash
git commit -am "Update Rezept"
git push origin main

kubectl rollout status deployment/tasks-salat -n frontend
kubectl get pods -n frontend
```

→ Du siehst, ob das neue Gericht serviert wurde.

---

## 🌐 Zugriff testen

```bash
minikube ip
kubectl get svc tasks-service -n frontend
curl http://$(minikube ip):30080/api/tasks
```

→ Du servierst deine App direkt aus Minikube – ganz ohne Umwege.

---

## 🧹 Cleanup bei Fehlern

```bash
kubectl delete deployment tasks-salat -n frontend
kubectl delete svc tasks-service -n frontend
```

→ Spülmaschine anwerfen.

---

## 🍽️ Serviervorschlag

Du hast jetzt:

- Ein echtes Rezept für deine App
- Einen Git-Trigger (simuliert oder echt)
- Einen Rollout in Minikube

→ Deine Küche ist bereit für Gäste, Releases und Monitoring!

---

> 👨‍🍳 Als Nächstes kannst du die Dessertstation einrichten in [`06-MONITORING.md`](../monitoring/06-MONITORING.md)  
> Dort wird geschaut, ob alles heiß genug ist 😄🍰


---

Wenn du willst, helfe ich dir beim Monitoring-Kapitel, beim Schreiben von Healthchecks oder beim Einbauen von Alerts. Sag einfach, wie du deinen tasks-salat weiter verfeinern willst 😄👨‍🍳