[//]: # (04-SERVICE.md)

# 🍽️ Service – der Kellner bringt das Gericht

---

## 🔥 Aufwärmen: Ist die Küche bereit?

```bash
kubectl get pods -n frontend
kubectl get deployments -n frontend
kubectl get svc -n frontend
```

Wenn kein Deployment vorhanden ist, richten wir es direkt neu an:

```bash
kubectl create deployment nginx-salat \
  --image=nginx \
  --replicas=2 \
  -n frontend
```

→ Erklärung dazu findest du in [`03-REZEPTE.md`](03-REZEPTE.md)

---

## 🧠 Konzept: Was ist ein Service?

Ein **Service** ist wie ein **Kellner**:

- Er kennt den Weg zur Küche (Pod)
- Er hat eine feste Adresse (ClusterIP, NodePort, Ingress)
- Er sorgt dafür, dass Gäste nicht selbst in die Küche laufen müssen
- Er kann auch draußen servieren (externe IP)

---

## 🛠️ Beispiel: NGINX-Service in der Salatstation

```bash
kubectl expose deployment nginx-salat \
  --port=80 \
  --target-port=80 \
  --type=NodePort \
  -n frontend
```

→ Der Kellner serviert NGINX über Port 80, erreichbar von außen.

Prüfen:

```bash
kubectl get svc -n frontend
```

→ Du siehst den Service mit einem `NodePort`, z.B. `30080`.

---

## 🧪 Übungsteil: Service-Details & Zugriff

### 🔍 Service-Details anzeigen

```bash
kubectl describe svc nginx-salat -n frontend
```

→ Zeigt dir Ports, Endpoints, Typ und mehr.

---

### 🌐 Zugriff testen (lokal)

```bash
minikube service nginx-salat -n frontend
```

→ Öffnet den Service im Browser.

> ⚠️ **Hinweis für Linux-Nutzer mit Docker-Driver:**  
> Minikube kann kein Browserfenster öffnen.  
> Nutze stattdessen:

```bash
minikube ip
kubectl get svc nginx-salat -n frontend
curl http://$(minikube ip):<NodePort>
```

→ Beispiel:

```bash
curl http://$(minikube ip):30080
```

→ Damit testest du den Kellner direkt per Kommandozeile.

---

### 🧹 Service entfernen

```bash
kubectl delete svc nginx-salat -n frontend
```

→ Kellner verabschieden.

---

## 🍽️ Serviervorschlag

Du hast jetzt:

- Ein Gericht (Deployment)
- Einen Kellner (Service)
- Einen funktionierenden Restaurantbereich (Namespace)

→ Bereit für CI/CD in der `CI-Bar`, Monitoring in der `Dessertstation`, und Routing im `Ingress-Garten`.

---

> 👨‍🍳 Als Nächstes bauen wir die CI-Bar auf in [`05-CI-BAR.md`](../CI-Bar/MENUE.md)  
> Dort wird automatisch gekocht, serviert und gespült 😄🍸