# Überblick

> Die Regressionstests stellen sicher, dass die API stabil bleibt, auch wenn neue Features hinzukommen 
> oder bestehende Bereiche refaktoriert werden.

## Warum Regressionstests?

- Sie decken das gesamte API‑Verhalten ab.
- Sie simulieren echte Nutzeraktionen über HTTP.
- Sie funktionieren unabhängig vom Spring‑Profil.
- Sie ersetzen manuelle Swagger‑Tests.
- Sie sind die Grundlage für spätere CI‑Automatisierung.

## Profil‑Unabhängigkeit

Die Tests greifen **nicht** auf Spring‑Konfigurationen zu.  
Sie testen eine **bereits laufende Anwendung** über:

http://localhost:8080/api


Dadurch benötigen sie **keine**:
- `application-test.yml`
- `application-local-dev-test.yml`
- `application-compose-dev-test.yml`

RestAssured verhält sich wie ein externer Client (Browser, Swagger, Postman).

