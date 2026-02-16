# 🟪 **DDD‑Guideline für Fortgeschrittene**

Diese Guideline richtet sich an Entwickler, die bereits mit Schichtenarchitektur, Entities, Services und REST vertraut sind und nun verstehen wollen, **wie DDD die Architektur strukturiert und schützt**.

---

# 1. **Aggregates definieren Grenzen, nicht Entities**

Ein Aggregat ist eine **konsistente fachliche Einheit**, die:

- eine **Root** hat (Aggregate Root)
- interne Objekte besitzt (Entities, Value Objects)
- **Invarianten** schützt
- **Transaktionen** kapselt

In deinem Projekt:

- **TaskList** ist die Aggregate Root
- **Tasks** sind Teil des Aggregats
- **TaskService** darf NICHT verwendet werden, um Tasks zu manipulieren
- **TaskList** ist die einzige Instanz, die Tasks verändern darf

👉 **Wenn du Tasks außerhalb der TaskList veränderst, verletzt du das Aggregat.**

---

# 2. **Domain‑Model ist der einzige Ort für Business‑Regeln**

Business‑Regeln gehören ausschließlich in die Domain:

- Statuswechsel
- Validierungen
- Invarianten
- Berechnungen
- Erlaubnis‑Checks

Beispiele:

- „Eine TaskList darf nur archiviert werden, wenn alle Tasks abgeschlossen sind.“
- „Eine Task darf nicht von DONE zurück auf TODO gesetzt werden.“

Diese Regeln gehören:

✔ in Domain‑Methoden  
✔ in private Helper‑Methoden  
✔ in Value Objects

Sie gehören NICHT:

❌ in Controller  
❌ in Orchestrator  
❌ in Service  
❌ in DTOs  
❌ in Repositories

👉 **Wenn du eine Regel außerhalb der Domain findest, ist das ein Architekturfehler.**

---

# 3. **Orchestrator implementiert UseCases, nicht Logik**

Der Orchestrator ist ein **Application‑Service**, kein Domain‑Service.

Er:

- lädt Aggregate
- ruft Domain‑Methoden auf
- speichert Aggregate
- orchestriert Abläufe
- behandelt technische Fehler

Er macht NICHT:

❌ keine Business‑Regeln  
❌ keine Validierungen  
❌ keine Statusprüfungen  
❌ keine DTO‑Konvertierungen  
❌ keine Task‑Laderei  
❌ keine Aggregat‑Manipulation

👉 **Wenn der Orchestrator Regeln enthält, ist das Domain‑Leakage.**

---

# 4. **Services sind technische Boundary‑Layer**

Der Service ist die **Persistence‑Boundary**:

- lädt Aggregate
- speichert Aggregate
- wirft NotFound‑Fehler

Er macht NICHT:

❌ keine Regeln  
❌ keine UseCase‑Abläufe  
❌ keine DTO‑Konvertierungen  
❌ keine Aggregat‑Manipulation

👉 **Wenn ein Service Logik enthält, ist das ein Schichtenverstoß.**

---

# 5. **Controller ist reine API‑Schicht**

Der Controller:

- nimmt Requests entgegen
- ruft Orchestrator auf
- wandelt Domain → DTO
- baut API‑Responses

Er macht NICHT:

❌ keine Regeln  
❌ keine Entscheidungen  
❌ keine Domain‑Arbeit  
❌ keine Datenbank‑Arbeit

👉 **Wenn ein Controller Logik enthält, ist das ein Anti‑Pattern.**

---

# 6. **DTOs sind reine Transportobjekte**

DTOs:

- enthalten keine Logik
- sind API‑spezifisch
- sind flach
- sind serialisierbar

Sie gehören:

✔ in Controller  
✔ in API‑Schicht

Sie gehören NICHT:

❌ in Domain  
❌ in Orchestrator  
❌ in Service  
❌ in Repositories

👉 **Wenn DTOs in Domain/Orchestrator/Service auftauchen, ist das ein Architekturfehler.**

---

# 7. **Domain schützt ihre Invarianten**

Eine Invariante ist eine Regel, die IMMER gelten muss.

Beispiele:

- „Eine TaskList darf nur archiviert werden, wenn alle Tasks abgeschlossen sind.“
- „Eine Task darf nicht rückwärts in der Statuspipeline gehen.“

Diese Regeln müssen:

✔ in Domain‑Methoden geprüft werden  
✔ private Helper‑Methoden nutzen  
✔ Exceptions werfen, wenn verletzt

👉 **Wenn die Domain ihre Invarianten nicht schützt, ist das Aggregat inkonsistent.**

---

# 8. **UseCases sind atomar und transaktional**

Ein UseCase ist eine **einzige fachliche Operation**:

- „TaskList archivieren“
- „Task verschieben“
- „Task erstellen“

Ein UseCase ist:

✔ entweder erfolgreich  
❌ oder schlägt fehl

Es gibt keine Zwischenzustände.

👉 **Wenn du einen Check‑Endpoint brauchst, ist dein UseCase falsch modelliert.**

---

# 9. **Keine doppelte Logik**

Wenn du eine Regel zweimal prüfst:

- einmal im Controller
- einmal im Orchestrator
- einmal in der Domain

dann ist das ein Fehler.

👉 **Nur die Domain prüft.**

---

# 10. **Repositories speichern Aggregate, nicht Teilobjekte**

Ein Repository:

- lädt Aggregate
- speichert Aggregate

Es speichert NICHT:

❌ einzelne Tasks  
❌ Value Objects  
❌ DTOs

👉 **Wenn du TaskRepository im Orchestrator siehst, ist das ein Aggregat‑Verstoß.**

---

# 🟪 **Kurzfassung für Fortgeschrittene**

- **Domain** schützt Regeln und Invarianten
- **Orchestrator** orchestriert UseCases
- **Service** ist Persistence‑Boundary
- **Controller** ist API‑Boundary
- **DTOs** sind API‑Transportobjekte
- **Aggregate** definieren Konsistenzgrenzen
- **UseCases** sind atomar
- **Regeln** gehören ausschließlich in die Domain
- **Keine doppelte Logik**
- **Keine DTOs außerhalb der API‑Schicht**
- **Keine Task‑Manipulation außerhalb der TaskList**