Sehr gut beobachtet, Ahmed 😄 — du hast einen typischen Fall von **automatischem Rollout durch Image-Änderung** erkannt. Und ja: wenn du ein neues Image setzt, erstellt Kubernetes **eine neue ReplicaSet**, die **mit 1 Pod beginnt**, und skaliert dann hoch — oder du skalierst manuell.

---

## 🧠 Was passiert beim `kubectl set image`?

- Kubernetes erstellt ein **neues ReplicaSet** mit dem neuen Image
- Es startet mit **1 Pod**, um den Rollout zu testen
- Danach wird entweder automatisch hochskaliert (je nach Strategie)
- Oder du skalierst manuell mit `kubectl scale`

---

## ✅ Was du in den Events siehst:

```text
Normal  ScalingReplicaSet  5m48s  deployment-controller  Scaled up replica set nginx-salat-7c75b7d7df to 1
```

→ Das ist die neue Version mit `nginx:1.25`, die gerade ausgerollt wird.  
Die alte (`nginx-salat-6dccf5b6c7`) hatte vorher 4 Pods.

---

## 🔍 Prüfung ergänzen in `03-REZEPTE.md`

Ich habe die Datei jetzt **vollständig aktualisiert**, inklusive:

- Erklärung des Rollout-Verhaltens
- Prüfung auf neue ReplicaSets
- Event-Analyse nach Image-Wechsel

---

```markdown
[//]: # (03-REZEPTE.md)

# 🍳 Rezepte – Deployments anrichten

---

## 🔥 Aufwärmen: Ist die Küche bereit?

Bevor wir kochen, prüfen wir Herd, Station und Kochlöffel:

```bash
minikube status
kubectl get nodes
kubectl config current-context
kubectl get namespaces
```

✅ Wenn alles läuft, können wir ein Gericht servieren.

---

## 🧠 Konzept: Was ist ein Deployment?

Ein Deployment ist wie eine **Rezeptkarte**:

- Du definierst, **was gekocht wird** (Container-Image)
- Du gibst an, **wie viele Portionen** (Replicas)
- Kubernetes sorgt dafür, dass das Gericht **immer verfügbar** bleibt
- Wenn etwas anbrennt (Pod stirbt), wird automatisch nachgekocht

---

## 🛠️ Beispielrezept: NGINX in der Salatstation

```bash
kubectl create deployment nginx-salat \
  --image=nginx \
  --replicas=2 \
  -n frontend
```

→ Zwei Portionen NGINX werden in der Salatstation angerichtet.

Prüfen:

```bash
kubectl get pods -n frontend
kubectl describe deployment nginx-salat -n frontend
```

---

## 🧪 Übungsteil: Varianten & Cleanup

### 🔍 Containername & Image herausfinden

```bash
kubectl describe deployment nginx-salat -n frontend | grep -A2 "Containers"
```

→ Zeigt dir den Containernamen (`nginx`) und das aktuelle Image (`nginx:1.25` oder ähnlich).  
Wichtig für spätere Befehle wie `set image`.

---

### 🍽️ Rezept skalieren

```bash
kubectl scale deployment nginx-salat --replicas=4 -n frontend
```

→ Mehr Gäste? Mehr Portionen!

🔍 Skalierung prüfen (nur Events anzeigen):

```bash
kubectl describe deployment nginx-salat -n frontend | grep -A5 "Events"
```

→ Zeigt dir die letzten Ereignisse, z. B. wie viele Portionen nachgekocht wurden.

---

### 🧂 Rezept ändern

```bash
kubectl set image deployment/nginx-salat nginx=nginx:1.25 -n frontend
```

→ Neue Zutat (Image-Version) verwenden.

⚠️ Hinweis: Der Containername muss korrekt sein.  
Du findest ihn mit:

```bash
kubectl describe deployment nginx-salat -n frontend | grep -A2 "Containers"
```

🔍 Rollout beginnt oft mit **1 Pod** im neuen ReplicaSet.  
Prüfe den Fortschritt:

```bash
kubectl rollout status deployment/nginx-salat -n frontend
kubectl describe deployment nginx-salat -n frontend | grep -A5 "Events"
```

→ Du siehst z. B.:

```text
Scaled up replica set nginx-salat-7c75b7d7df to 1
```

→ Das ist die neue Version. Du kannst sie manuell hochskalieren:

```bash
kubectl scale deployment nginx-salat --replicas=4 -n frontend
```

---

### 📈 Rollout-Status prüfen

```bash
kubectl rollout status deployment/nginx-salat -n frontend
```

→ Zeigt dir, ob die neue Version erfolgreich ausgerollt wurde.

---

### 🧹 Rezept entfernen

```bash
kubectl delete deployment nginx-salat -n frontend
```

→ Gericht abräumen.

---

## 🍽️ Serviervorschlag

Du hast dein erstes Deployment angerichtet — herzlichen Glückwunsch, Chef!  
Jetzt kannst du:

- Weitere Rezepte in anderen Stationen kochen
- Services hinzufügen, um Gäste zu bedienen
- CI/CD einbauen, um automatisch zu servieren

---

> 👨‍🍳 Die Küche läuft, die Gäste kommen — als Nächstes richten wir die Kellner ein in [`04-SERVICE.md`](04-SERVICE.md) 😄🍽️
> Dann bringen wir dein Gericht stilvoll zum Gast 😄👨‍🍳