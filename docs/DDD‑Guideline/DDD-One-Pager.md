# 🟩 **DDD – One‑Pager (Ultra‑Kurzfassung)**
*Die gesamte Architektur auf einer einzigen Seite.*

---

## **1. Domain**
- Enthält **alle** Business‑Regeln
- Schützt **alle** Invarianten
- Führt **alle** State‑Transitions aus
- Kennt **keine** DTOs, Controller, Services oder Frameworks
- Setter sind verboten

---

## **2. Aggregate**
- Definieren Konsistenzgrenzen
- Nur die Aggregate Root darf verändert werden
- Child‑Entities werden niemals direkt geladen oder gespeichert
- Repositories existieren nur für Aggregate Roots

---

## **3. Application Layer (Orchestrator)**
- Führt UseCases aus
- Lädt Aggregate
- Ruft Domain‑Methoden auf
- Speichert Aggregate
- Enthält **keine** Regeln, **keine** DTOs, **keine** Logik

---

## **4. Infrastructure Layer**
- Implementiert Repositories
- Kapselt Persistenz und externe Systeme
- Enthält **keine** Business‑Logik

---

## **5. API Layer (Controller)**
- Nimmt Requests entgegen
- Validiert technisch
- Ruft Orchestrator auf
- Baut Responses
- Enthält **keine** Regeln

---

## **6. DTOs**
- Nur Transportobjekte
- Nur in der API‑Schicht
- Keine Logik
- Niemals in Domain/Orchestrator/Service

---

## **7. Invarianten**
- Werden ausschließlich in der Domain geprüft
- Müssen atomar und transaktional durchgesetzt werden
- Dürfen nicht dupliziert werden

---

## **8. UseCases**
- Sind atomar
- Erfolg oder Exception
- Keine Check‑Endpoints für Business‑Regeln

---

## **9. Keine doppelte Logik**
- Jede Regel existiert genau einmal: in der Domain

---

## **10. Architekturverstöße sofort beheben**
- Regeln außerhalb der Domain
- DTOs außerhalb der API
- Setter in Entities
- TaskService im TaskList‑UseCase  
  → **sofort refactoren**

---

# 🟩 **One‑Sentence‑Summary**
**Domain entscheidet, Orchestrator orchestriert, Service speichert, Controller präsentiert — und Aggregate schützen alles.**