# OpenAPI Export – CI Ready (TODO: GitHub Actions)

Diese kurze Dokumentation beschreibt, wie die OpenAPI-Spezifikation der Anwendung
exportiert und im Projekt abgelegt wird.  
Der Export funktioniert bereits manuell über Skripte.  
**Die Automatisierung in GitHub Actions ist als TODO markiert.**

---

## 📍 Speicherort der API-Spezifikation

Die exportierten Dateien liegen unter:

```
docs/swagger/
```

- `openapi.yaml`
- `openapi.json`

---

## 📤 Manuelle Exporte

### YAML exportieren

```bash
./scripts/swagger/export-openapi.sh
```

Direkter curl-Aufruf:

```bash
curl http://localhost:8080/v3/api-docs.yaml -o docs/swagger/openapi.yaml
```

---

### JSON exportieren

```bash
./scripts/swagger/export-openapi.json.sh
```

Direkter curl-Aufruf:

```bash
curl http://localhost:8080/v3/api-docs -o docs/swagger/openapi.json
```

---

## 🧭 Swagger UI

Die interaktive API-Dokumentation bleibt erreichbar unter:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📝 TODO: GitHub Actions (später automatisieren)

- [ ] YAML & JSON automatisch generieren
- [ ] Dateien in `docs/swagger/` aktualisieren
- [ ] Optional: Versionierung der API-Spezifikation
- [ ] Optional: Veröffentlichung (z.B. GitHub Pages)
- [ ] Optional: Client-Generierung (TypeScript, Java, Kotlin)