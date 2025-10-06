# 🧪 Rezept 02: Kubernetes-Service für unsere App

## 🍳 Szenario

Willkommen zurück in der Minikube-Küche!  
Heute öffnen wir die Tür zur Küche — mit einem **Kubernetes-Service**, der unsere App erreichbar macht.

Ziel ist es, die Grundlagen von:

- `Service`-Definitionen in Kubernetes  
- Verbindung zu einem Deployment  
- Port-Mapping und Namensgebung

zu verstehen und zu üben — ganz ohne Ingress, ganz ohne LoadBalancer.  
Nur ein einfacher Service, der unsere App intern und extern sichtbar macht.

---

## 🧾 Zutaten

- 🧱 Ein bestehendes Deployment (`tasks-app`)  
- 🚪 Ein Kubernetes-Service (`NodePort`)  
- 🔢 Ein Port für die App (z.B. `8080`)  
- 🧂 Labels zur Verbindung mit dem Deployment

<details>
<summary>📦 Technische Datei anzeigen</summary>

Die Service-Definition findest du unter `k8s/koch-rezepte/tasks-app-service.yaml`:

```
k8s/koch-rezepte/
└── tasks-app-service.yaml
```

🔗 [tasks-app-service.yaml](../../k8s/koch-rezepte/tasks-app-service.yaml)

</details>

---

## 🔥 Zubereitungsschritte

| Schritt | Minikube-Befehl | Küchenmetapher |
|--------|------------------|----------------|
| 1️⃣ Service anwenden | `kubectl apply -f tasks-app-service.yaml` | Tür zur Küche einbauen |
| 2️⃣ Service prüfen | `kubectl get svc -n dev` | Klingel testen |
| 3️⃣ Port prüfen | `kubectl describe svc tasks-app -n dev` | Türschild lesen |
| 4️⃣ Verbindung testen | `curl http://<minikube-ip>:<nodeport>` | Gast klopft an |
| 5️⃣ Fehler beheben | Labels, Ports, Targets prüfen | Tür richtig einhängen |

---

## 🧠 Hintergrundwissen

- Ein `Service` verbindet sich über Labels mit einem Deployment  
- `ClusterIP` ist nur intern sichtbar  
- `NodePort` erlaubt Zugriff von außen (z. B. via Minikube-IP)  
- Du kannst später `Ingress` oder `LoadBalancer` ergänzen  
- Services sind stabil — auch wenn Pods sich ändern

---

## 📘 Ausblick

Wenn du dieses Rezept gemeistert hast, kannst du:

- Die App im Browser öffnen  
- Einen Ingress hinzufügen  
- HTTPS und Domains einrichten  
- Oder die App in ein echtes Netzwerk bringen

Aber heute bleibt’s bei der Tür — ganz bewusst 😄

---

<details>
<summary>🧼 Nachspeise: Service entfernen</summary>

Wenn du den Service später wieder entfernen willst:

```bash
kubectl delete svc tasks-app -n dev
```

</details>


---

[//]: # (Wenn du willst, schreibe ich dir jetzt die passende `tasks-app-service.yaml` — oder wir gehen direkt weiter zu `03-app-anbindung.md`, wo die App mit Postgres spricht.  )

[//]: # (Du gibst den Takt an 😄👨‍🍳)