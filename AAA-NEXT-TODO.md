> Sobald man das Feedback vom Runner hat, kann man in compose‑dev‑CI (v4) einsteigen.
> Das wird der Moment, in dem die Tests nicht mehr gegen H2 laufen, sondern gegen eine echte Postgres‑Instanz 
> — und damit wird die Testumgebung 1:1 wie Produktion.


- Mini‑Docker‑Cheatsheet‑Seite


🚀 Nächster sinnvoller Schritt
Da compose‑dev + Tests jetzt stabil laufen, wäre der nächste logische Schritt:

compose‑dev‑CI (v4) vorbereiten, also:

- Tests im CI gegen compose‑dev laufen lassen

- DB im CI als Container starten

- App im CI als Container starten

- RestAssured Tests gegen CI‑Container laufen lassen

- Logs und Artefakte archivieren

- Das ist der Schritt, der deine lokale Stabilität in eine automatisierte Pipeline überführt.
