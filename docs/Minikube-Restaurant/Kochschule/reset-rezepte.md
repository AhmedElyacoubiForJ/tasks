[//]: # (docs/Minikube-Restaurant/Kochschule/reset-rezepte.md)
# 🧹 Reset-Rezepte – Sauber aufräumen & frisch starten

---

## 🔄 Namespace komplett löschen

```bash
kubectl delete namespace <name>
```

→ Entfernt alle Zutaten, Rezepte und Kellner aus dem Bereich.

---

## 🧼 Einzelne Ressourcen löschen

```bash
kubectl delete deployment <name> -n <namespace>
kubectl delete service <name> -n <namespace>
kubectl delete pod <name> -n <namespace>
```

→ Entfernt gezielt einzelne Gerichte oder Kellner.

---

## 🧽 Alles in einem Namespace löschen

```bash
kubectl delete all --all -n <namespace>
```

→ Leert die Station, aber lässt sie stehen.

---

## 🔁 Minikube neu starten

```bash
minikube stop
minikube delete
minikube start --driver=docker
```

→ Herd ausschalten, Küche putzen, neu starten.

---

> 🧑‍🍳 Manchmal muss man die Küche schließen, um sie besser zu machen.  
> Diese Rezepte helfen dir, sauber und kontrolliert neu zu beginnen 😄


