# ✅ Universelle Architektur‑Checkliste  
### Gilt für alle zukünftigen Backend‑Projekte

## A. Verantwortlichkeiten klären

- [ ] Welche Entitäten gibt es?
- [ ] Welche fachlichen Regeln gelten pro Entität?
- [ ] Welche UseCases betreffen nur eine Entität?
- [ ] Welche UseCases betreffen mehrere Entitäten?

---

## B. Services definieren

- [ ] Für jede Entität existiert ein eigener Entity‑Service.
- [ ] Ein Entity‑Service enthält nur Logik für genau diese Entität.
- [ ] Es gibt keine Cross‑Entity‑Logik in Entity‑Services.
- [ ] Domain‑Logik liegt nicht in technischen Schichten (z. B. Repositories).

---

## C. Orchestrator definieren

- [ ] Gibt es UseCases, die mehrere Entitäten betreffen?
- [ ] Werden Daten aus mehreren Repositories benötigt?
- [ ] Müssen mehrere Services koordiniert werden?
- [ ] Müssen Regeln über mehrere Aggregate/Entitäten geprüft werden?

**Wenn eine dieser Fragen „Ja“ ist → Orchestrator einführen oder erweitern.**

---

## D. Domain‑Logik prüfen

- [ ] Domain‑Methoden statt Setter verwenden.
- [ ] Invarianten werden im Aggregat/Domain‑Objekt geprüft.
- [ ] Zeitstempel (created/updated) werden in der Domain gesetzt, nicht in JPA‑Callbacks.
- [ ] Aggregat schützt seine Konsistenz (keine unkontrollierte Manipulation von außen).
- [ ] Keine JPA‑Magie wie `@PrePersist` oder `@PreUpdate` für fachliche Logik.

---

## E. Controller‑Design

- [ ] Controller enthält keine Business‑Logik.
- [ ] Controller delegiert ausschließlich an Services/Orchestrator.
- [ ] Subresource‑Design wird verwendet, wenn es fachlich sinnvoll ist.
- [ ] DTOs werden für Requests und Responses verwendet.

---

## F. Testbarkeit sicherstellen

- [ ] Entity‑Services sind isoliert testbar.
- [ ] Orchestrator ist testbar durch Mocking der Services.
- [ ] Domain‑Methoden werden als reine Unit‑Tests getestet.
- [ ] Es gibt keine versteckte Logik in JPA‑Callbacks oder technischen Schichten.

---

## G. Erweiterbarkeit prüfen

- [ ] Neue UseCases können hinzugefügt werden, ohne bestehende Services massiv zu verändern.
- [ ] Domain‑Regeln sind zentral im Aggregat/Domain‑Modell verankert.
- [ ] Orchestrator kapselt komplexe Abläufe.
- [ ] Services bleiben klein, fokussiert und verständlich.
