# 🟥 **DDD‑Guideline für Experten**
*Architekturprinzipien, Invarianten, Konsistenzgrenzen und Verantwortlichkeiten*

Diese Guideline richtet sich an erfahrene Entwickler und Architekten, die DDD nicht nur anwenden, sondern **konsequent durchsetzen** wollen.  
Sie beschreibt **harte Regeln**, keine Empfehlungen.

---

# 1. **Aggregates sind die einzige Quelle fachlicher Konsistenz**

Ein Aggregat definiert:

- **eine transaktionale Konsistenzgrenze**
- **eine fachliche Invariante**
- **einen einzigen Einstiegspunkt (Aggregate Root)**
- **einen unverletzbaren Zustand**

In deinem System:

- **TaskList** ist die Aggregate Root
- **Tasks** sind Teil des Aggregats
- **TaskList** ist der einzige Zugriffspunkt für alle Änderungen

**Konsequenz:**  
Kein anderer Layer darf Tasks direkt laden, verändern oder speichern.

---

# 2. **Invarianten müssen synchron und atomar durchgesetzt werden**

Eine Invariante ist eine Regel, die **niemals verletzt werden darf**.

Beispiele:

- „Eine TaskList darf nur archiviert werden, wenn alle Tasks abgeschlossen sind.“
- „Eine Task darf nicht rückwärts in der Statuspipeline gehen.“

Diese Regeln müssen:

- **innerhalb einer einzigen Transaktion**
- **innerhalb der Aggregate Root**
- **vor jeder Statusänderung**

durchgesetzt werden.

**Konsequenz:**  
Regeln dürfen nicht im Orchestrator, Controller oder Service dupliziert werden.

---

# 3. **Domain‑Methoden sind die einzigen Orte für State Transitions**

Jede Statusänderung muss über eine Domain‑Operation erfolgen:

- `archive()`
- `completeTask()`
- `moveTask()`
- `rename()`

Diese Methoden:

- prüfen Invarianten
- ändern den Zustand
- aktualisieren Lifecycle‑Informationen
- sind idempotent, wenn sinnvoll

**Konsequenz:**  
Setter sind verboten.  
Statusänderungen außerhalb der Domain sind verboten.

---

# 4. **Orchestrator implementiert UseCases, nicht Logik**

Der Orchestrator ist ein **Application‑Service**, der:

- Aggregate lädt
- Domain‑Operationen ausführt
- Aggregate speichert
- technische Fehler behandelt
- Transaktionen kapselt

Er enthält **keine**:

- Business‑Regeln
- Validierungen
- Statusprüfungen
- DTO‑Konvertierungen
- Aggregat‑Manipulationen

**Konsequenz:**  
Wenn der Orchestrator eine Regel kennt, ist die Architektur kompromittiert.

---

# 5. **Services sind reine Persistence‑Boundaries**

Ein Service:

- lädt Aggregate
- speichert Aggregate
- wirft NotFound‑Fehler

Er enthält **keine**:

- Business‑Regeln
- UseCase‑Abläufe
- DTO‑Konvertierungen
- Aggregat‑Manipulationen

**Konsequenz:**  
Ein Service darf niemals eine Domain‑Regel prüfen oder anwenden.

---

# 6. **Controller ist reine API‑Boundary**

Der Controller:

- nimmt Requests entgegen
- ruft Orchestrator auf
- wandelt Domain → DTO
- baut API‑Responses

Er enthält **keine**:

- Business‑Regeln
- Domain‑Operationen
- Datenbankzugriffe
- Aggregat‑Manipulationen

**Konsequenz:**  
Wenn ein Controller eine Regel kennt, ist die Domain verletzt.

---

# 7. **DTOs sind reine Transportobjekte**

DTOs:

- sind API‑spezifisch
- enthalten keine Logik
- sind flach
- sind serialisierbar

Sie gehören ausschließlich in die API‑Schicht.

**Konsequenz:**  
DTOs im Orchestrator oder Domain‑Layer sind ein Architekturfehler.

---

# 8. **Repositories speichern Aggregate, nicht Teilobjekte**

Ein Repository:

- lädt Aggregate
- speichert Aggregate

Es speichert **niemals**:

- einzelne Tasks
- Value Objects
- DTOs
- Teilzustände

**Konsequenz:**  
Ein TaskRepository im Orchestrator wäre ein schwerer Aggregat‑Verstoß.

---

# 9. **UseCases sind atomar und unverhandelbar**

Ein UseCase ist eine **vollständige fachliche Operation**, die:

- in einer Transaktion ausgeführt wird
- entweder erfolgreich ist
- oder fehlschlägt

Es gibt keine Zwischenzustände.

**Konsequenz:**  
Check‑Endpoints (z. B. `isArchivable`) sind ein Anti‑Pattern.

---

# 10. **Keine doppelte Logik — Single Source of Truth**

Eine Regel darf nur an einem Ort existieren:

✔ Domain

Nicht:

❌ Controller  
❌ Orchestrator  
❌ Service  
❌ Client  
❌ DTO  
❌ Frontend

**Konsequenz:**  
Wenn eine Regel zweimal existiert, ist das System inkonsistent.

---

# 🟥 **Kurzfassung für Experten**

- Aggregates definieren Konsistenzgrenzen
- Domain schützt Invarianten
- Domain führt State Transitions durch
- Orchestrator orchestriert UseCases
- Services sind Persistence‑Boundaries
- Controller ist API‑Boundary
- DTOs sind Transportobjekte
- Repositories speichern Aggregate
- UseCases sind atomar
- Regeln existieren nur in der Domain