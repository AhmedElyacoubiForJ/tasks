Alles klar, Ahmed — hier kommt deine **komplette, professionelle, DDD‑konforme Test‑Checkliste** für das gesamte TaskList‑Modul.  
Sie ist so aufgebaut, dass du:

- **jede Schicht** testen kannst (API → Service → Domain)
- **jede Regel** überprüfst (Statuswechsel, Archivierung, PATCH/PUT)
- **jede Invariante** absicherst
- **jede Fehlersituation** abdeckst

Das ist die Art von Checkliste, die man in einem echten Enterprise‑Projekt in Confluence oder im QA‑Bereich ablegt.

---

# ⭐ **TEST‑CHECKLISTE: TaskList – CRUD, PATCH, PUT, Status, Domain‑Regeln**

## 1) **GET /tasklists – Alle Listen abrufen**
- [ ] Response 200
- [ ] `data` ist ein Array
- [ ] Jede TaskList enthält: id, title, description, created, updated, count, progress
- [ ] progress korrekt berechnet
- [ ] count korrekt

---

## 2) **GET /tasklists/{id} – Einzelne Liste abrufen**
### Erfolgsfall
- [ ] Response 200
- [ ] DTO korrekt gefüllt

### Fehlerfall
- [ ] Response 404 bei unbekannter UUID
- [ ] Fehlermeldung korrekt

---

# ⭐ 3) **POST /tasklists – Neue Liste erstellen**
### Erfolgsfall
- [ ] Response 201
- [ ] Title gesetzt
- [ ] Description optional
- [ ] Status = ACTIVE
- [ ] created/updated gesetzt

### Validierungsfehler
- [ ] Title leer → 400
- [ ] Title zu lang → 400
- [ ] Description zu lang → 400

---

# ⭐ 4) **PUT /tasklists/{id} – Vollständiges Update**
### Erfolgsfall
- [ ] Response 200
- [ ] Title aktualisiert
- [ ] Description aktualisiert
- [ ] Status aktualisiert (nur neutrale Wechsel)
- [ ] updated geändert

### Domain‑Regeln
- [ ] PUT darf **nicht** archivieren
- [ ] PUT darf **nicht** reaktivieren
- [ ] PUT darf **keine archivierte Liste ändern** → 409 DomainRuleViolation

### Fehlerfälle
- [ ] Unbekannte UUID → 404
- [ ] Ungültiger Status → 400
- [ ] Title leer → 400

---

# ⭐ 5) **PATCH /tasklists/{id} – Partielles Update**
### Erfolgsfall
- [ ] Response 200
- [ ] Nur gesetzte Felder werden geändert
- [ ] Null‑Felder werden ignoriert
- [ ] updated geändert

### Domain‑Regeln
- [ ] PATCH darf **nicht** archivieren
- [ ] PATCH darf **nicht** reaktivieren
- [ ] PATCH darf **keine archivierte Liste ändern** → 409

### Testfälle
- [ ] Nur title ändern
- [ ] Nur description ändern
- [ ] Nur status ändern (neutraler Wechsel)
- [ ] Kombination aus title + description
- [ ] PATCH mit leerem Body → keine Änderung

---

# ⭐ 6) **DELETE /tasklists/{id}**
### Erfolgsfall
- [ ] Response 200
- [ ] Liste wird gelöscht
- [ ] Tasks werden mit gelöscht (orphanRemoval)

### Fehlerfall
- [ ] Unbekannte UUID → 404

---

# ⭐ 7) **POST /tasklists/{id}/archive – Archivieren**
### Erfolgsfall
- [ ] Response 200
- [ ] Status = ARCHIVED
- [ ] updated geändert

### Domain‑Regeln
- [ ] Archivieren nur erlaubt, wenn **alle Tasks abgeschlossen**
- [ ] Sonst → 409 DomainRuleViolation

### Fehlerfälle
- [ ] Liste existiert nicht → 404
- [ ] Liste bereits archiviert → idempotent (200)

---

# ⭐ 8) **POST /tasklists/{id}/activate – Reaktivieren**
### Erfolgsfall
- [ ] Response 200
- [ ] Status = ACTIVE
- [ ] updated geändert

### Domain‑Regeln
- [ ] Nur archivierte Listen dürfen aktiviert werden
- [ ] Sonst → 409

---

# ⭐ 9) **Domain‑Regeln testen (direkt über API)**
### Archivierte Listen sind „frozen“
- [ ] PATCH auf archivierte Liste → 409
- [ ] PUT auf archivierte Liste → 409
- [ ] Task hinzufügen → 409
- [ ] Task löschen → 409
- [ ] Task‑Status ändern → 409

### Statuswechsel
- [ ] changeStatus(ARCHIVED) → verboten
- [ ] changeStatus(ACTIVE) bei ARCHIVED → verboten
- [ ] changeStatus(NEUTRAL) → erlaubt

---

# ⭐ 10) **TaskListUpdater testen**
### Full Update
- [ ] Nur geänderte Felder werden angewendet
- [ ] updated wird gesetzt
- [ ] Domain‑Methoden werden korrekt aufgerufen

### Patch Update
- [ ] Null‑Felder werden ignoriert
- [ ] Nur gesetzte Felder werden geändert
- [ ] updated wird gesetzt

---

# ⭐ 11) **Transformer testen**
- [ ] count korrekt
- [ ] progress korrekt
- [ ] created/updated korrekt
- [ ] Keine Tasks im DTO (Aggregation geschützt)

---

# ⭐ 12) **Swagger / OpenAPI testen**
- [ ] PATCH Endpoint sichtbar
- [ ] PUT Endpoint sichtbar
- [ ] DTO‑Schemas korrekt
- [ ] Beispiele korrekt
- [ ] Response‑Wrapper korrekt

---

# ⭐ 13) **Integrationstest‑Szenarien**
### Szenario 1: Liste erstellen → Task hinzufügen → archivieren
- [ ] Tasks abgeschlossen
- [ ] Archivierung erlaubt

### Szenario 2: Liste erstellen → Task offen lassen → archivieren
- [ ] Archivierung verboten

### Szenario 3: Archivierte Liste → PATCH versuchen
- [ ] 409

### Szenario 4: Archivierte Liste → activate → PATCH
- [ ] PATCH wieder erlaubt

---

# ⭐ Wenn du willst, kann ich dir jetzt zusätzlich erstellen:

👉 **Eine Postman‑Collection**  
👉 **Eine REST‑Assured Testklasse**  
👉 **JUnit‑Tests für Domain‑Regeln**  
👉 **Integrationstests für PATCH/PUT**  
👉 **Ein Test‑Daten‑Setup (SQL oder Java)**

Sag einfach, was du brauchst.