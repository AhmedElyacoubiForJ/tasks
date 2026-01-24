# ✅ Checkliste: Domain‑Logik oder Orchestrator‑Logik?
### Entscheidungshilfe für jeden UseCase

Diese Checkliste hilft dabei zu entscheiden, ob ein UseCase in den  
**Aggregat‑/Domain‑Service** oder in den **Orchestrator‑Service** gehört.  
Sie ist universell einsetzbar und gilt für alle zukünftigen Projekte.

---

# 1. 🎯 Grundprinzip (DDD)

> **Betrifft der UseCase nur EIN Aggregat → Domain‑/Aggregat‑Service**  
> **Betrifft der UseCase MEHRERE Aggregate → Orchestrator**

Dieses Prinzip ist die wichtigste Leitlinie.  
Alle weiteren Punkte helfen, diese Entscheidung sauber zu treffen.

---

# 2. 🧩 Fragen zur Entscheidung

## A. Betrifft der UseCase nur EIN Aggregat?

- [ ] Wird nur ein einziges Aggregat geladen?
- [ ] Wird nur ein einziges Aggregat verändert?
- [ ] Muss nur eine einzige Domain‑Regel geprüft werden?
- [ ] Reicht ein einzelnes Repository aus?
- [ ] Gibt es keine Abhängigkeiten zu anderen Aggregaten?

**Wenn JA → Domain‑/Aggregat‑Service**

---

## B. Betrifft der UseCase MEHRERE Aggregate?

- [ ] Müssen mehrere Aggregate geladen werden?
- [ ] Müssen Regeln geprüft werden, die mehrere Aggregate betreffen?
- [ ] Müssen mehrere Services koordiniert werden?
- [ ] Müssen mehrere Repositories angesprochen werden?
- [ ] Müssen mehrere Aggregate gemeinsam verändert werden?
- [ ] Muss ein Aggregat auf den Zustand eines anderen reagieren?

**Wenn JA → Orchestrator**

---

# 3. 🧠 Typische Beispiele

## A. Gehört in die Domain (Single‑Aggregate)

- Statuswechsel eines Aggregats
- Validierungen, die nur dieses Aggregat betreffen
- Änderungen an Feldern (Titel, Beschreibung, Status)
- Aktivieren/Deaktivieren ohne Abhängigkeiten
- Zeitstempel setzen (`updated`)
- Invarianten prüfen

---

## B. Gehört in den Orchestrator (Cross‑Aggregate)

- Erstellen eines Child‑Aggregats in einem Parent‑Aggregat
- Verschieben eines Elements zwischen Aggregaten
- Archivieren eines Aggregats mit Prüfung der zugehörigen Child‑Aggregate
- Löschen eines Aggregats mit Folgeaktionen auf andere Aggregate
- Regeln wie:
  - „Nur wenn alle Tasks abgeschlossen sind“
  - „Nur wenn keine offenen Bestellungen existieren“
  - „Beim Aktivieren müssen abhängige Objekte aktualisiert werden“

---

# 4. 🚫 Anti‑Pattern (Was man vermeiden sollte)

- [ ] Keine Cross‑Aggregate‑Logik im Aggregat‑Service
- [ ] Keine Domain‑Logik im Orchestrator
- [ ] Keine Business‑Logik in Repositories
- [ ] Keine Setter‑basierten Mutationen
- [ ] Keine JPA‑Callbacks für fachliche Regeln

---

# 5. 🏁 Zusammenfassung

| Frage | Antwort | Service |
|-------|---------|---------|
| Betrifft nur EIN Aggregat? | Ja | **Domain‑/Aggregat‑Service** |
| Betrifft mehrere Aggregate? | Ja | **Orchestrator** |
| Muss ein Aggregat auf ein anderes reagieren? | Ja | **Orchestrator** |
| Ist es ein reiner Zustandswechsel? | Ja | **Domain‑Service** |
| Müssen mehrere Repositories angesprochen werden? | Ja | **Orchestrator** |

---

# 🧩 Entscheidungsmatrix: Domain‑Logik oder Orchestrator‑Logik?
### Universelle Entscheidungshilfe für jeden UseCase

Diese Matrix hilft dabei, jeden neuen UseCase eindeutig einzuordnen:  
→ **Gehört er in den Aggregat‑/Domain‑Service?**  
→ **Oder in den Orchestrator?**

---

# 1. 🎯 Grundprinzip (DDD)

> **Ein Aggregat → Domain‑/Aggregat‑Service**  
> **Mehrere Aggregate → Orchestrator**

---

# 2. 🧩 Entscheidungsmatrix

| Frage / Kriterium                                           | Antwort → Service-Typ                     | Erklärung |
|-------------------------------------------------------------|-------------------------------------------|-----------|
| Betrifft der UseCase nur EIN Aggregat?                      | **Domain‑/Aggregat‑Service**              | Keine Abhängigkeiten zu anderen Aggregaten. |
| Betrifft der UseCase MEHRERE Aggregate?                     | **Orchestrator**                          | Cross‑Aggregate‑Regeln oder Koordination nötig. |
| Muss nur EIN Repository angesprochen werden?                | **Domain‑Service**                        | Reiner Aggregat‑Zugriff. |
| Müssen mehrere Repositories angesprochen werden?            | **Orchestrator**                          | Mehrere Aggregate müssen geladen/verändert werden. |
| Wird nur EIN Aggregat verändert?                            | **Domain‑Service**                        | Reiner Zustandswechsel. |
| Müssen mehrere Aggregate gemeinsam verändert werden?        | **Orchestrator**                          | Gemeinsame Konsistenzregeln. |
| Gibt es eine Domain‑Regel, die mehrere Aggregate betrifft?  | **Orchestrator**                          | Beispiel: „Nur wenn alle Tasks abgeschlossen sind“. |
| Ist es ein reiner Status‑ oder Feldwechsel?                 | **Domain‑Service**                        | Aggregat‑interne Logik. |
| Muss ein Aggregat auf den Zustand eines anderen reagieren?  | **Orchestrator**                          | Beispiel: TaskList reagiert auf Tasks. |
| Ist der UseCase technisch, aber nicht fachlich komplex?     | **Domain‑Service**                        | Kein Cross‑Aggregate‑Bezug. |
| Ist der UseCase fachlich komplex und übergreifend?          | **Orchestrator**                          | Mehrere Aggregate + Regeln. |
| Wird ein Child‑Objekt in einem Parent‑Aggregat erzeugt?     | **Orchestrator**                          | Beispiel: Task in TaskList erstellen. |
| Wird ein Aggregat verschoben oder umgehängt?                | **Orchestrator**                          | Beispiel: Task von Liste A nach B. |
| Wird ein Aggregat gelöscht ohne Folgeaktionen?              | **Domain‑Service**                        | Reiner Löschvorgang. |
| Wird ein Aggregat gelöscht MIT Folgeaktionen?               | **Orchestrator**                          | Beispiel: Tasks müssen vorher geprüft/verschoben werden. |

---

# 3. 🧠 Typische Beispiele

## A. Gehört in die Domain (Single‑Aggregate)
- Statuswechsel eines Aggregats
- Validierungen, die nur dieses Aggregat betreffen
- Änderungen an Feldern (Titel, Beschreibung, Status)
- Aktivieren/Deaktivieren ohne Abhängigkeiten
- Zeitstempel setzen
- Invarianten prüfen

## B. Gehört in den Orchestrator (Cross‑Aggregate)
- Erstellen eines Child‑Aggregats in einem Parent‑Aggregat
- Verschieben eines Elements zwischen Aggregaten
- Archivieren eines Aggregats mit Prüfung der zugehörigen Child‑Aggregate
- Löschen eines Aggregats mit Folgeaktionen
- Regeln wie:
  - „Nur wenn alle Tasks abgeschlossen sind“
  - „Nur wenn keine offenen Bestellungen existieren“
  - „Beim Aktivieren müssen abhängige Objekte aktualisiert werden“

---

# 4. 🚫 Anti‑Pattern (Was unbedingt vermieden werden muss)

- Keine Cross‑Aggregate‑Logik im Aggregat‑Service
- Keine Domain‑Logik im Orchestrator
- Keine Business‑Logik in Repositories
- Keine Setter‑basierten Mutationen
- Keine JPA‑Callbacks für fachliche Regeln

---

# 5. 🏁 Zusammenfassung (Merksatz)

> **Ein Aggregat → Domain‑Service**  
> **Mehrere Aggregate → Orchestrator**  
> **Regeln über mehrere Aggregate → Orchestrator**  
> **Reiner Zustandswechsel → Domain‑Service**

Diese Matrix dient als schnelles, zuverlässiges Werkzeug vor jeder Implementierung.
