# 🟦 **DDD‑Guideline für Junior‑Entwickler**

Diese Guideline richtet sich an Entwickler, die bereits programmieren können, aber DDD noch nicht vollständig verinnerlicht haben.  
Sie erklärt **was du tun darfst**, **was du vermeiden musst**, und **warum**.

---

# 1. **Das Aggregat ist dein Zentrum – nicht die Datenbank**

In DDD ist ein Aggregat nicht einfach eine Tabelle oder ein Entity‑Objekt.  
Es ist eine **fachliche Einheit**, die Regeln schützt.

In deinem Projekt:

- **TaskList** ist das Aggregat
- **Tasks** gehören zur TaskList
- Niemand darf Tasks direkt verändern
- Alles läuft über die TaskList

👉 **Wenn du etwas an Tasks ändern willst, musst du über die TaskList gehen.**

---

# 2. **Domain‑Methoden sind die einzigen Orte für Business‑Regeln**

Beispiele für Regeln:

- „Eine TaskList darf nur archiviert werden, wenn alle Tasks abgeschlossen sind.“
- „Eine Task darf nicht von DONE zurück auf TODO gesetzt werden.“
- „Der Titel darf nicht leer sein.“

Diese Regeln gehören:

❌ nicht in den Controller  
❌ nicht in den Orchestrator  
❌ nicht in den Service  
❌ nicht in DTOs  
❌ nicht in die Datenbank

✔ **nur in die Domain**

👉 **Wenn du eine Regel irgendwo anders findest, ist es ein Fehler.**

---

# 3. **Der Orchestrator ist ein Use‑Case‑Manager, kein Logik‑Container**

Der Orchestrator:

✔ lädt das Aggregat  
✔ ruft Domain‑Methoden auf  
✔ speichert das Aggregat  
✔ baut den Ablauf des UseCases

Er macht NICHT:

❌ keine Regeln  
❌ keine Berechnungen  
❌ keine Validierungen  
❌ keine DTO‑Erstellung  
❌ keine Task‑Laderei  
❌ keine Status‑Prüfungen

👉 **Wenn du im Orchestrator eine Regel siehst, ist es falsch.**

---

# 4. **Der Service ist nur die Persistence‑Boundary**

Der Service:

✔ lädt Entities  
✔ speichert Entities  
✔ wirft NotFound‑Fehler

Er macht NICHT:

❌ keine Regeln  
❌ keine Entscheidungen  
❌ keine DTO‑Konvertierungen  
❌ keine UseCase‑Abläufe

👉 **Wenn du im Service Logik siehst, ist es falsch.**

---

# 5. **Der Controller ist nur die API‑Schicht**

Der Controller:

✔ nimmt Requests entgegen  
✔ ruft den Orchestrator auf  
✔ baut Responses  
✔ wandelt Domain → DTO um

Er macht NICHT:

❌ keine Regeln  
❌ keine Entscheidungen  
❌ keine Domain‑Arbeit  
❌ keine Datenbank‑Arbeit

👉 **Wenn du im Controller Business‑Logik siehst, ist es falsch.**

---

# 6. **DTOs sind nur Verpackungen**

DTOs sind:

- API‑Objekte
- leichtgewichtig
- ohne Logik

Sie gehören:

✔ in den Controller  
✔ in die API‑Schicht

Sie gehören NICHT:

❌ in die Domain  
❌ in den Orchestrator  
❌ in den Service

👉 **Wenn du DTOs in Domain/Orchestrator/Service siehst, ist es falsch.**

---

# 7. **Domain schützt ihre Invarianten**

Eine Invariante ist eine Regel, die IMMER gelten muss.

Beispiel:

> „Eine TaskList darf nur archiviert werden, wenn alle Tasks abgeschlossen sind.“

Diese Regel muss die Domain selbst schützen:

✔ in `archive()`  
✔ mit `private boolean isArchivable()`

Nicht:

❌ im Orchestrator  
❌ im Controller  
❌ im Service

👉 **Wenn die Domain nicht schützt, ist das Aggregat kaputt.**

---

# 8. **UseCases sind atomar**

Ein UseCase ist eine vollständige Aktion:

- „TaskList archivieren“
- „Task verschieben“
- „Task erstellen“

Ein UseCase ist:

✔ entweder erfolgreich  
❌ oder schlägt fehl

Es gibt keine Zwischenzustände.

👉 **Wenn du einen Check‑Endpoint brauchst, ist dein UseCase falsch modelliert.**

---

# 9. **Wenn du etwas zweimal prüfst, ist es falsch**

Beispiel:

- Controller prüft
- Orchestrator prüft
- Domain prüft

Das führt zu:

❌ doppelter Logik  
❌ Inkonsistenzen  
❌ Bugs

👉 **Nur die Domain prüft.**

---

# 10. **Wenn du TaskService im Orchestrator siehst, ist es falsch**

Warum?

Weil Tasks zur TaskList gehören.

Der Orchestrator darf:

✔ TaskListService verwenden  
❌ TaskService verwenden

👉 **Wenn du TaskService im Orchestrator siehst, ist es ein Aggregat‑Verstoß.**

---

# 🟦 **Kurzfassung für Junior‑Entwickler**

- **Domain** = Regeln
- **Orchestrator** = Ablauf
- **Service** = Laden/Speichern
- **Controller** = API
- **DTOs** = nur API
- **Regeln** = nur Domain
- **TaskList** = Aggregat
- **Tasks** = gehören zur TaskList
- **UseCases** = atomar
- **Keine doppelten Prüfungen**
- **Keine DTOs im Orchestrator**
- **Keine Regeln im Orchestrator**
- **Keine Regeln im Service**