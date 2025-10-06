[//]: # (02-STATIONEN.md)

# 🧭 Stationen einrichten – Namespaces als Restaurantbereiche

---

## 🔥 Aufwärmen: Ist die Küche bereit?

Bevor wir neue Stationen eröffnen, prüfen wir, ob die Küche stabil läuft:

```bash
minikube status               # Herd an?
kubectl get nodes             # Küchenserver bereit?
kubectl get pods -A           # Systemzutaten aktiv?
kubectl config current-context # Koch arbeitet im richtigen Kontext?
```

✅ Wenn alles grün ist, können wir neue Bereiche eröffnen.  
❌ Wenn nicht, bitte zurück zu `01-KUECHE.md` und Herd neu starten.

---

## 🧠 Konzept: Was ist ein Namespace?

Ein **Namespace** ist wie ein **Restaurantbereich** oder eine **Kochstation**:

- Du kannst Zutaten (Pods, Services) getrennt lagern
- Du vermeidest Chaos in der Küche
- Du kannst Rechte und Rollen pro Station vergeben
- Du kannst mehrere Menüs gleichzeitig betreiben

Beispiele:

| Namespace     | Analogie             |
|---------------|----------------------|
| `testkueche`  | Probierküche         |
| `frontend`    | Salatstation         |
| `backend`     | Grillstation         |
| `monitoring`  | Dessertstation       |

---

## 🛠️ Stationen eröffnen

```bash
kubectl create namespace testkueche
kubectl create namespace frontend
kubectl create namespace backend
kubectl create namespace monitoring
```

Du kannst prüfen, ob die Stationen stehen:

```bash
kubectl get namespaces
```

---

## 🧂 Zutaten pro Station prüfen

```bash
kubectl get pods -n testkueche
kubectl get pods -n frontend
kubectl get pods -n backend
kubectl get pods -n monitoring
```

Noch sind die Töpfe leer — aber die Stationen stehen bereit.

---

## 🍽️ Serviervorschlag

Die Stationen sind eröffnet, die Köche können loslegen.  
Ab jetzt kannst du:

- Deployments gezielt pro Bereich anrichten
- Services stationenweise garnieren
- Rollen und Rezepte sauber trennen

---

> 🧑‍🍳 Die Küche ist segmentiert, die Gäste können kommen.  
> Als Nächstes servieren wir die ersten Gerichte in [`03-REZEPTE.md`](03-REZEPTE.md) 😄🍲

---

> 🧑‍🍳 Du willst üben, wie man mit bestehenden Stationen umgeht?  
> Dann besuche die [Kochschule – Standard-Übungsteil](../../Kochschule/standard-uebung.md) 😄  
> Oder vertiefe Dein Wissen in der [Konzept-Kochschule](../../Kochschule/konzept-kochschule.md)

