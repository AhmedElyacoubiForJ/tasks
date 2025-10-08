# 🧠 Gedanken-Route zum PROD-Deployment

Dieses Dokument hält meine Architekturentscheidungen, Überlegungen und Alternativen fest — als Wegweiser für die spätere Veröffentlichung meiner App.

---

## ❓ Ausgangsfragen

- Soll ich in Minikube mit `container-dev` oder `compose-dev` weiterarbeiten?
- Was ist Best Practice für Minikube-Workflows?
- Ist `docker-compose` in Minikube sinnvoll?
- Wie deployen Entwickler ihre Apps später ins Netz?
- Welche Plattformen sind kostenlos und praktikabel?

---

## ✅ Aktuelle Entscheidung

Ich arbeite in Minikube mit dem Profil `container-dev`.  
Jede Komponente wird separat deployed (Postgres, App, später Redis etc.).  
Das entspricht dem Kubernetes-Workflow und bereitet mich auf spätere Cloud-Deployments vor.

---

## 🔍 Hintergrund: Compose vs Deployment

| Aspekt | Compose | Kubernetes Deployment |
|--------|--------|------------------------|
| Einfachheit | Schnell & lokal | Granular & robust |
| Kontrolle | Weniger | Vollständig |
| Minikube | Möglich, aber nicht empfohlen | Best Practice |
| Skalierung | Eingeschränkt | Voll integriert |
| Health Checks | Manuell | Automatisch |
| Cloud-Transfer | Gut für einfache Plattformen | Ideal für GKE, DO, Hetzner |

---

## 🌍 Später veröffentlichen: Optionen

| Plattform | Konzept | Compose möglich? | Kubernetes möglich? | Kostenlos? |
|----------|---------|------------------|----------------------|------------|
| Render | Dockerfile oder Compose | ✅ Ja | ❌ Nein | ✅ Ja |
| Railway | Auto-Deploy aus Git | ✅ Ja | ❌ Nein | ✅ Ja |
| Fly.io | Dockerfile | ✅ Ja | ❌ Nein | ✅ Ja |
| Google Cloud Run | Container | ❌ Nein | ✅ Ja (GKE) | ⚠️ Eingeschränkt |
| GitHub Actions + VPS | Manuell | ✅ Ja | ✅ Ja | ✅ Ja (VPS selbst) |

---

## 🧑‍🍳 Fazit

Ich bleibe bei `container-dev` in Minikube.  
Das erlaubt mir:

- Saubere Trennung der Komponenten  
- Nutzung echter Kubernetes-Ressourcen  
- Vorbereitung auf produktionsnahe Deployments  
- Späteres Hosting auf GKE, Hetzner oder VPS

---

## 📘 Nächste Schritte

→ Weiter mit `02-service-rezept.md`  
→ Dort wird die App über einen Kubernetes-Service erreichbar gemacht  
→ Danach folgt `03-app-anbindung.md` mit echter Datenbankverbindung
