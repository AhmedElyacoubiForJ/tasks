[//]: # (docs/api/versionierung/api-versioning-strategy.md)
## **Optimierte Version: api-versioning-strategy.md**

### Zweck der Versionierung
> Die API‑Versionierung stellt sicher, dass neue Funktionen oder Breaking Changes eingeführt werden können, ohne bestehende Clients zu beeinträchtigen. Jede Version bleibt stabil und unabhängig von Domain‑ oder Infrastrukturänderungen.

### Struktur der Versionen
```
controllers/api/
  v1/   → stabile, produktive API
  v2/   → zukünftige Erweiterungen oder Breaking Changes
```

### Aufbau der API‑Versionen
- **contract/** enthält den API‑Vertrag (DTOs, Interfaces, Validation, Responses).
- **impl/** enthält die Controller‑Implementierungen.
- Jede Version besitzt ihre eigene Contract‑ und Implementierungsstruktur.
- Domain und Application bleiben unverändert und versionierungsfrei.

### Regeln für neue Versionen
- v1 bleibt stabil; keine Breaking Changes.
- Neue Features oder Änderungen, die inkompatibel wären, kommen in v2.
- DTOs, Contracts und Responses werden pro Version kopiert oder erweitert.
- Swagger zeigt jede Version separat an.
- Deprecation‑Hinweise in v1, wenn Endpunkte ersetzt werden.

### Vorteile der Versionierung
- Klare Trennung zwischen alten und neuen API‑Versionen.
- Keine Seiteneffekte zwischen Versionen.
- Saubere Migration für Clients.
- API kann wachsen, ohne Domain oder Application zu verändern.
- Stabilität auch bei komplexeren Erweiterungen.
