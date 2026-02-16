# 🟥 **Die 10 Gebote der Domain‑Driven Design (DDD)**
*Die unverhandelbaren Regeln für saubere, langlebige Software.*

---

## **1. Du sollst deine Domain als Quelle der Wahrheit behandeln.**
Alle fachlichen Regeln, Invarianten und Entscheidungen gehören ausschließlich in die Domain.  
Niemals in Controller, Orchestrator, Service oder DTO.

---

## **2. Du sollst Aggregates als heilige Grenzen respektieren.**
Die Aggregate Root ist der einzige Zugangspunkt.  
Kein anderer Layer darf Child‑Entities direkt manipulieren.

---

## **3. Du sollst keine Business‑Regeln außerhalb der Domain duplizieren.**
Wenn eine Regel zweimal existiert, ist dein System bereits inkonsistent.

---

## **4. Du sollst Domain‑Methoden für alle State‑Transitions verwenden.**
Setter sind verboten.  
Statusänderungen passieren nur über Domain‑Operationen.

---

## **5. Du sollst den Orchestrator rein halten.**
Er orchestriert UseCases, aber entscheidet nichts.  
Keine Regeln. Keine DTOs. Keine Logik.

---

## **6. Du sollst Services nur für Persistence nutzen.**
Services laden und speichern Aggregate.  
Sie enthalten keine Regeln und keine UseCase‑Abläufe.

---

## **7. Du sollst Controller niemals mit Logik belasten.**
Controller sind reine API‑Adapter.  
Sie rufen Orchestrator auf und transformieren DTOs.

---

## **8. Du sollst DTOs niemals in Domain oder Orchestrator verwenden.**
DTOs sind Transportobjekte, keine Domain‑Modelle.  
Sie gehören ausschließlich in die API‑Schicht.

---

## **9. Du sollst UseCases atomar ausführen.**
Ein UseCase ist entweder erfolgreich oder schlägt fehl.  
Check‑Endpoints für Business‑Regeln sind ein Anti‑Pattern.

---

## **10. Du sollst Invarianten strikt und atomar schützen.**
Eine Domain‑Operation darf niemals einen ungültigen Zustand hinterlassen.  
Wenn eine Regel verletzt wird, muss die Domain sofort eine Exception werfen.

---

# 🟥 **Kurzfassung der 10 Gebote**

**Domain entscheidet.  
Aggregate schützen.  
Orchestrator orchestriert.  
Service speichert.  
Controller präsentiert.  
DTOs transportieren.  
Regeln existieren nur einmal.  
UseCases sind atomar.  
Keine Setter.  
Keine Abkürzungen.**