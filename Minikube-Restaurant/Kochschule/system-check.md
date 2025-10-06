
[//]: # (system-check.md)

> 🧑‍🍳 Manchmal muss man die Küche schließen, um sie besser zu machen.
> Diese Rezepte helfen dir, sauber und kontrolliert neu zu beginnen 😄

# 🔍 System-Check – ist die Küche bereit?

---

## 🧪 Minikube prüfen

```bash
minikube status
```

→ Ist der Herd an?

---

## 🧪 Kubernetes prüfen

```bash
kubectl get nodes
kubectl get pods -A
kubectl config current-context
```

→ Ist der Küchenserver bereit?  
→ Sind die Zutaten aktiv?  
→ Arbeitet der Koch im richtigen Bereich?

---

## 🧪 Architektur & Umgebung prüfen

```bash
uname -m
lscpu
dpkg --print-architecture
```

→ Ist die Küche kompatibel mit den Rezepten?

---

> 🧑‍🍳 Vor jedem Gang: Herd prüfen, Zutaten checken, Kochlöffel bereithalten.  
> So wird jedes Gericht ein Erfolg 😄🍲
