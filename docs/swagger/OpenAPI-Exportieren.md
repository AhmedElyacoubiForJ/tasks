# 📘 **OpenAPI‑YAML Export – Mini‑Dokumentation**

## 📍 Speicherort der API‑Definition

Die generierte OpenAPI‑Spezifikation wird im Projekt unter folgendem Pfad abgelegt:

```
docs/swagger/openapi.yaml
```

Dieser Ordner dient als zentrale Stelle für API‑Dokumentation und kann versioniert werden.

---

## 📤 **OpenAPI‑YAML manuell exportieren**

Starte die Anwendung und führe folgenden Befehl aus:

```bash
curl http://localhost:8080/v3/api-docs.yaml -o docs/swagger/openapi.yaml
```

Dieser Befehl:

- ruft die vollständige OpenAPI‑Definition ab
- speichert sie als `openapi.yaml`
- legt sie automatisch im Ordner `docs/swagger/` ab

---

## 🧭 **Alternative: JSON‑Version**

Falls du die JSON‑Variante brauchst:

```bash
curl http://localhost:8080/v3/api-docs -o docs/swagger/openapi.json
```

---

## 🔍 **Swagger‑UI im Browser**

Swagger‑UI bleibt weiterhin erreichbar unter:

```
http://localhost:8080/swagger-ui/index.html
```

---

# 🎉 Fertig — klein, klar, wertvoll

Wenn du willst, kann ich dir jetzt:

- ein kleines Shell‑Skript `export-openapi.sh` erstellen
- eine GitHub‑Action bauen, die die YAML automatisch generiert
- oder die README um diesen Abschnitt erweitern

Sag einfach, womit wir weitermachen.