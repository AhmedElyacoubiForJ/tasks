[//]: # (docs/swagger/OpenApi-Export-CI-Ready.md)
# OpenAPI‑Export – CI‑Automatisierung (Roadmap)

## Ziel
> Die OpenAPI‑Spezifikation soll zukünftig automatisch generiert und im Ordner `docs/swagger/` abgelegt werden. Der manuelle Export funktioniert bereits über Skripte; die CI‑Automatisierung folgt später.

## Speicherort der Spezifikation
```
docs/swagger/
  openapi.yaml
  openapi.json
```

## Manuelle Exporte
Die Skripte für lokale Exporte liegen unter:

```
scripts/swagger/
  export-openapi.sh
  export-openapi.json.sh
```

## CI‑TODOs
- YAML und JSON automatisch generieren
- Dateien in `docs/swagger/` aktualisieren

[//]: # (- Optional: Versionierung der API‑Spezifikation)

[//]: # (- Optional: Veröffentlichung &#40;z.B. GitHub Pages&#41;)

[//]: # (- Optional: Client‑Generierung &#40;TypeScript, Java, Kotlin&#41;)
