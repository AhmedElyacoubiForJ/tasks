# 🟫 **Domain‑Driven Design – Enterprise Architecture Guideline**
*Verbindliche Richtlinien für konsistente, skalierbare und auditierbare Geschäftsanwendungen.*

---

# 1. **Purpose and Scope**

Diese Richtlinie definiert verbindliche Architekturprinzipien für Systeme, die nach Domain‑Driven Design (DDD) modelliert werden.  
Sie stellt sicher, dass:

- fachliche Konsistenz gewährleistet ist
- Verantwortlichkeiten klar getrennt sind
- technische Schulden minimiert werden
- Erweiterbarkeit und Wartbarkeit langfristig gesichert sind

Diese Vorgaben sind **verpflichtend** und gelten für alle Module, Services und Teams.

---

# 2. **Aggregate Governance**

Ein Aggregat ist die **atomare Konsistenzgrenze** eines fachlichen Modells.

**Verbindliche Regeln:**

- Jede Änderung am Aggregat erfolgt ausschließlich über die Aggregate Root.
- Child‑Entities dürfen nicht direkt geladen, verändert oder gespeichert werden.
- Repositories existieren ausschließlich für Aggregate Roots.
- Aggregatgrenzen sind stabil und dürfen nur nach formaler Architekturprüfung geändert werden.

---

# 3. **Domain Layer Responsibilities**

Der Domain‑Layer ist die **einzige Instanz**, die fachliche Regeln definiert und durchsetzt.

**Der Domain‑Layer muss:**

- alle Business‑Regeln implementieren
- alle Invarianten schützen
- alle State‑Transitions kapseln
- fachliche Fehlerzustände über Exceptions signalisieren
- unabhängig von technischen Frameworks bleiben

**Der Domain‑Layer darf nicht:**

- DTOs kennen
- Controller‑ oder API‑Strukturen kennen
- Persistenztechnologien kennen
- externe Services aufrufen

---

# 4. **Application Layer (Orchestrator) Responsibilities**

Der Application‑Layer (Orchestrator) implementiert **UseCases**, nicht Business‑Logik.

**Der Application‑Layer muss:**

- Aggregate laden
- Domain‑Operationen ausführen
- Aggregate speichern
- Transaktionen initiieren
- technische Fehler behandeln

**Der Application‑Layer darf nicht:**

- Business‑Regeln implementieren
- Statusprüfungen durchführen
- DTOs verarbeiten
- Aggregat‑Strukturen manipulieren

---

# 5. **Infrastructure Layer Responsibilities**

Der Infrastructure‑Layer stellt technische Funktionalität bereit.

**Der Infrastructure‑Layer muss:**

- Repositories implementieren
- Persistenztechnologien kapseln
- externe Systeme anbinden
- technische Konfigurationen bereitstellen

**Der Infrastructure‑Layer darf nicht:**

- Business‑Regeln implementieren
- UseCases orchestrieren
- Domain‑Modelle verändern

---

# 6. **API Layer Responsibilities**

Der API‑Layer (Controller) ist die **Boundary** zwischen externen Clients und dem System.

**Der API‑Layer muss:**

- Requests entgegennehmen
- DTOs validieren
- Orchestrator aufrufen
- Responses generieren

**Der API‑Layer darf nicht:**

- Business‑Regeln implementieren
- Domain‑Modelle manipulieren
- Persistenzzugriffe durchführen

---

# 7. **DTO Governance**

DTOs sind **Transportobjekte** und dienen ausschließlich der API‑Kommunikation.

**DTOs dürfen nicht:**

- im Domain‑Layer verwendet werden
- im Application‑Layer verwendet werden
- Logik enthalten
- Aggregat‑Strukturen abbilden

---

# 8. **Invariant Enforcement**

Invarianten sind **nicht verhandelbare fachliche Regeln**, die jederzeit gültig sein müssen.

**Verbindliche Vorgaben:**

- Invarianten werden ausschließlich im Domain‑Layer geprüft.
- Verstöße führen zu sofortigen Exceptions.
- Invarianten dürfen nicht im Application‑ oder API‑Layer dupliziert werden.
- Invarianten müssen atomar innerhalb einer Transaktion durchgesetzt werden.

---

# 9. **UseCase Atomicity**

Ein UseCase ist eine **transaktionale Einheit**.

**Verbindliche Vorgaben:**

- Ein UseCase ist entweder erfolgreich oder schlägt fehl.
- Zwischenzustände sind unzulässig.
- Check‑Endpoints für Business‑Regeln sind untersagt.
- Domain‑Operationen müssen idempotent sein, sofern fachlich sinnvoll.

---

# 10. **Compliance and Enforcement**

Diese Richtlinie ist verbindlich.  
Abweichungen sind nur nach formaler Architekturfreigabe zulässig.

**Verstöße führen zu:**

- Architektur‑Review
- Refactoring‑Pflicht
- Dokumentationspflicht
- ggf. Eskalation an die technische Leitung

---

# 🟫 **Executive Summary**

- Domain = fachliche Autorität
- Application = UseCase‑Orchestrierung
- Infrastructure = technische Implementierung
- API = Kommunikationsgrenze
- Aggregate = Konsistenzgrenzen
- Invarianten = unverhandelbar
- DTOs = Transport, nicht Logik
- UseCases = atomar
- Regeln = ausschließlich Domain
- Architekturverstöße = sofort beheben

---

Wenn du möchtest, können wir jetzt:

👉 **Style 10: Ultra‑kurze „One‑Pager‑Version“**  
oder  
👉 **Ein Gesamt‑PDF‑Konzept aus allen Styles**  
oder  
👉 **Ein DDD‑Poster für dein Projekt**