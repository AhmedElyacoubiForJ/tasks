## Warum wir die Versionierung jetzt einführen sollten
Die API‑Schicht ist bereits perfekt strukturiert:

- klare Trennung zwischen contract und impl
- DTOs sauber sortiert
- Swagger‑Wrapper isoliert
- Validation‑Annotations sauber gruppiert
- Response‑Envelopes zentralisiert

Genau deshalb ist jetzt der ideale Zeitpunkt, weil:

- keine Altlasten mitschleppen
- die Struktur stabil ist
- spätere v2‑Änderungen sauber isoliert werden
- Breaking Changes vermieden werden
- Swagger später beide Versionen getrennt anzeigen kann

---

## Empfohlene Struktur nach dem Verschieben

```
controllers/
  api/
    v1/
      contract/
      impl/
    v2/
      contract/   (leer)
      impl/       (leer)
```

Alles, was heute unter `contract/` liegt, wandert nach `v1/contract/`.

---

### **api-v1-v2-strategie.md**

#### Ziel der API‑Versionierung
- Änderungen an der API sollen ohne Breaking Changes möglich sein.
- Clients können stabil auf einer Version bleiben.
- Neue Features können unabhängig von bestehenden Endpunkten entwickelt werden.
- Die Domain bleibt unverändert und versionierungsfrei.

#### Struktur
```
controllers/api/
  v1/   → aktuelle stabile API
  v2/   → zukünftige Erweiterungen
```

#### Regeln
- v1 bleibt stabil und unverändert.
- Neue Features oder Breaking Changes kommen ausschließlich in v2.
- DTOs, Contracts und Responses sind versioniert.
- Domain‑Modelle bleiben unverändert und versionierungsfrei.
- Swagger zeigt jede Version separat.
- Deprecation‑Hinweise in v1, wenn Endpunkte ersetzt werden.

#### Vorteile
- Klare Trennung zwischen alten und neuen API‑Versionen.
- Keine Seiteneffekte zwischen Versionen.
- Saubere Migration für Clients.
- Einfaches Refactoring und Erweiterbarkeit.
- API‑Stabilität auch bei wachsender Domain.