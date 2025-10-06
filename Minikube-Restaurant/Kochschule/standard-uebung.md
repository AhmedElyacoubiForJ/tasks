[//]: # (standard-uebung.md)
# 🧪 Standard-Übungsteil für jedes Kapitel

Dieser Abschnitt kann in jedes Kapitel eingebunden oder verlinkt werden, um typische Szenarien zu üben und technische Klarheit zu schaffen.

---

## 🔄 Was tun, wenn Ressourcen schon existieren?

### ✅ Option 1: Behalten und weiterarbeiten

```bash
kubectl get all -n <namespace>
```

→ Zeigt dir, was schon da ist. Gut für Fortsetzung.

---

### 🧹 Option 2: Leeren, aber Namespace behalten

```bash
kubectl delete all --all -n <namespace>
```

→ Entfernt alle Ressourcen, Namespace bleibt.

---

### 🔄 Option 3: Namespace komplett neu aufsetzen

```bash
kubectl delete namespace <namespace>
kubectl create namespace <namespace>
```

→ Frischer Start. Achtung: Wartezeit bis zur Löschung.

---

## 🔍 Vorbereitung: Systemzustand prüfen

```bash
minikube status
kubectl get nodes
kubectl get pods -A
kubectl config current-context
```

→ Stelle sicher, dass Herd, Kochlöffel und Zutaten bereit sind 😄🍳, Chef 😄👨‍🍳