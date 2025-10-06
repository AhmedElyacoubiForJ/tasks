[//]: # (konzept-kochschule.md)

# 📘 Konzept-Kochschule – Kubernetes in Küchensprache erklärt

---

## 🧭 Namespace = Restaurantbereich

Ein Namespace ist wie ein abgetrennter Bereich im Restaurant:

- **testkueche** = Probierküche
- **frontend** = Salatstation
- **backend** = Grillstation
- **monitoring** = Dessertstation

→ Du kannst Zutaten (Pods), Rezepte (Deployments) und Gäste (Services) getrennt verwalten.

---

## 🍳 Deployment = Rezeptkarte

Ein Deployment ist wie ein Rezept:

- Du gibst an, wie viele Portionen (Replicas) du brauchst
- Du definierst die Zutaten (Container-Image, Ports)
- Kubernetes sorgt dafür, dass das Gericht immer verfügbar bleibt

→ Beispiel: Ein NGINX-Deployment mit 3 Portionen Salat

---

## 🍽️ Service = Kellner

Ein Service ist wie ein Kellner, der das Gericht zum Gast bringt:

- Er kennt den Weg zur Station (Pod)
- Er hat eine feste Adresse (ClusterIP, NodePort, Ingress)
- Er sorgt dafür, dass Gäste nicht selbst in die Küche laufen müssen

→ Beispiel: Ein Service, der die Salatstation erreichbar macht

---

> 👨‍🍳 Diese Konzepte bilden die Grundlage deiner Minikube-Küche.  
> Wenn du sie beherrschst, kannst du jedes Gericht servieren – stabil und lecker 😄
