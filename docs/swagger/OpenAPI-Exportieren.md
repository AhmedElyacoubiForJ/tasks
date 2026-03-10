[//]: # (docs/swagger/OpenAPI-Exportieren.md)
# 📘 OpenAPI‑Export (YAML/JSON)

## Speicherort der API‑Definition
Die generierte OpenAPI‑Spezifikation wird im Projekt unter folgendem Pfad abgelegt:

```
docs/swagger/openapi.yaml
```

Dieser Ordner dient als zentrale Stelle für API‑Dokumentation und kann versioniert werden.

---

## OpenAPI‑YAML manuell exportieren
Anwendung starten und folgenden Befehl ausführen:

```bash
curl http://localhost:8080/v3/api-docs.yaml -o docs/swagger/openapi.yaml
```

Der Befehl:

- ruft die vollständige OpenAPI‑Definition ab
- speichert sie als `openapi.yaml`
- legt sie im Ordner `docs/swagger/` ab

---

## JSON‑Export (optional)

```bash
curl http://localhost:8080/v3/api-docs -o docs/swagger/openapi.json
```

---

## Swagger‑UI im Browser

```
http://localhost:8080/swagger-ui/index.html
```

---

## Skripte für automatisierten Export
Unter `scripts/swagger/` liegen zwei Hilfsskripte:

- `export-openapi.sh` (YAML)
- `export-openapi.json.sh` (JSON)

Sie erleichtern den Export für lokale Entwicklung oder CI‑Pipelines.