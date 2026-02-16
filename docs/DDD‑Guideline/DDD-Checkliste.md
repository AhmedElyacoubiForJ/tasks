# 🟨 **DDD‑Checkliste (Ultra‑Kompakt)**
*Die 20 Regeln, die du NIEMALS brechen darfst.*

---

## 🧱 **Aggregat**
- [ ] Aggregate Root ist der einzige Einstiegspunkt
- [ ] Keine Manipulation von Child‑Entities außerhalb des Aggregats
- [ ] Keine Repositories für Child‑Entities
- [ ] Keine Services, die Child‑Entities laden

---

## 🧠 **Domain**
- [ ] Alle Business‑Regeln in der Domain
- [ ] Domain schützt ihre Invarianten
- [ ] Domain wirft Exceptions bei Regelverstößen
- [ ] Domain führt alle State‑Transitions aus
- [ ] Keine Setter
- [ ] Keine DTOs
- [ ] Keine technischen Abhängigkeiten

---

## 🎛 **Orchestrator (Application Layer)**
- [ ] Orchestrator lädt Aggregate
- [ ] Orchestrator ruft Domain‑Methoden auf
- [ ] Orchestrator speichert Aggregate
- [ ] Keine Business‑Regeln
- [ ] Keine DTO‑Erstellung
- [ ] Keine TaskService‑Aufrufe (bei TaskList‑Aggregat)
- [ ] Keine Statusprüfungen

---

## 🗄 **Service (Persistence Boundary)**
- [ ] Nur load/save
- [ ] Keine Regeln
- [ ] Keine UseCase‑Logik
- [ ] Keine DTOs

---

## 🌐 **Controller (API Layer)**
- [ ] Keine Regeln
- [ ] Keine Domain‑Logik
- [ ] Nur Orchestrator aufrufen
- [ ] Domain → DTO transformieren
- [ ] API‑Response bauen

---

## 📦 **DTOs**
- [ ] Nur in der API‑Schicht
- [ ] Keine Logik
- [ ] Keine Verwendung in Domain/Orchestrator/Service

---

## 🔒 **Invarianten**
- [ ] Werden ausschließlich in Domain‑Methoden geprüft
- [ ] Werden atomar durchgesetzt
- [ ] Werden niemals außerhalb der Domain dupliziert

---

## ⚙️ **UseCases**
- [ ] Jeder UseCase ist atomar
- [ ] Kein Check‑Endpoint für Business‑Regeln
- [ ] Erfolg oder Exception — nichts dazwischen

---

## 🚫 **Anti‑Patterns (sofort stoppen!)**
- [ ] Domain‑Regeln im Orchestrator
- [ ] DTOs im Orchestrator
- [ ] TaskService im TaskList‑UseCase
- [ ] Setter in Domain‑Entities
- [ ] Doppelte Regelprüfungen
- [ ] Public‑Methoden für interne Regeln

---

# 🟨 **Kurzfassung der Kurzfassung**
**Domain entscheidet.  
Orchestrator orchestriert.  
Service speichert.  
Controller präsentiert.  
DTOs transportieren.  
Aggregate schützen.**
