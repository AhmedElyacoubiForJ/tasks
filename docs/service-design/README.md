
# 📘 Service‑Design Architektur
### *Grundlagen für skalierbare, wartbare und testbare Backend‑Systeme*

Dieses Dokument beschreibt das Service‑Design, das in diesem Projekt verwendet wird.  
Es dient als Leitfaden für zukünftige Projekte und stellt sicher, dass Services klar getrennt, testbar und fachlich sauber strukturiert bleiben.

---

# 1. 🎯 Allgemeines Service‑Design (ohne DDD‑Bezug)

## 1.1 Zielsetzung
Das Ziel dieser Architektur ist es, Verantwortlichkeiten klar zu trennen und komplexe UseCases sauber zu strukturieren.  
Sie basiert auf drei Schichten:

- **Entity‑Services**
- **Orchestrator‑Services**
- **Controller‑Schicht**

Diese Struktur ist unabhängig von Domain‑Driven Design und eignet sich für jedes mehrschichtige Backend‑Projekt.

---

## 1.2 Entity‑Services
Ein Entity‑Service ist ausschließlich für **CRUD‑Operationen und fachliche Logik einer einzelnen Entität** zuständig.

### Aufgaben eines Entity‑Services
- Entität laden
- Entität erstellen
- Entität aktualisieren
- Entität löschen
- Validierungen, die nur diese Entität betreffen

### Vorteile
- klare Verantwortlichkeiten
- hohe Testbarkeit
- geringe Kopplung
- einfache Wiederverwendbarkeit

---

## 1.3 Orchestrator‑Services
Ein Orchestrator‑Service führt **UseCases aus, die mehrere Entitäten betreffen**.

### Aufgaben eines Orchestrators
- Koordination zwischen mehreren Entity‑Services
- Ausführung komplexer Geschäftsabläufe
- Validierungen über mehrere Entitäten hinweg
- Transaktionale Abläufe

### Vorteile
- Entity‑Services bleiben schlank
- Cross‑Entity‑Logik ist zentralisiert
- sehr gut testbar (Mocks der Entity‑Services)
- hohe Wartbarkeit

---

## 1.4 Warum dieses Design?
- **Single Responsibility Principle**  
  Jede Klasse hat genau eine Aufgabe.

- **Hohe Testbarkeit**  
  Entity‑Services und Orchestrator können separat getestet werden.

- **Wartbarkeit**  
  Änderungen an einem UseCase betreffen nur den Orchestrator, nicht die Entity‑Services.

- **Erweiterbarkeit**  
  Neue UseCases können hinzugefügt werden, ohne bestehende Services zu verändern.

---

# 2. 📘 Service‑Design nach Domain‑Driven Design (DDD)

## 2.1 Zielsetzung
In der DDD‑Variante orientiert sich die Architektur an **Aggregaten** und deren fachlichen Regeln.  
Die zentrale Frage lautet:

> **Betrifft ein UseCase nur EIN Aggregat oder mehrere?**

---

## 2.2 Aggregat‑Services
Ein Aggregat‑Service ist für **Operationen eines einzelnen Aggregats** zuständig.

### Regel
> **Alles, was nur EIN Aggregat betrifft → gehört in den Aggregat‑Service.**

### Beispiele
#### Beispiel A — Archivieren betrifft nur TaskList
Domain‑Regel:  
„Eine TaskList kann jederzeit archiviert werden.“

Dann ist es eine reine Aggregat‑Operation:

- `TaskList.status` ändern
- `updated` setzen
- speichern

➡️ **Gehört in TaskListService**

#### Beispiel B — Task aktualisieren
- Titel ändern
- Beschreibung ändern
- Status ändern

➡️ **Gehört in TaskService**

---

## 2.3 Orchestrator‑Services
Ein Orchestrator führt UseCases aus, die **mehrere Aggregate** betreffen.

### Regel
> **Alles, was MEHRERE Aggregate betrifft → gehört in den Orchestrator.**

### Beispiele
#### Beispiel A — Archivieren betrifft TaskList + Tasks
Domain‑Regel:  
„Eine TaskList darf nur archiviert werden, wenn alle Tasks abgeschlossen sind.“

Der Orchestrator muss:

1. TaskList laden
2. Tasks laden
3. prüfen, ob alle abgeschlossen sind
4. ggf. Tasks archivieren
5. TaskList archivieren

➡️ **Gehört in den Orchestrator**

#### Beispiel B — Task erstellen in TaskList
1. TaskList laden
2. prüfen, ob Liste aktiv ist
3. Task erzeugen
4. Task der Liste zuordnen
5. speichern

➡️ **Gehört in den Orchestrator**

---

## 2.4 Vorteile der DDD‑Variante
- Aggregat‑Regeln bleiben im Aggregat
- Services bleiben klein und fokussiert
- Orchestrator kapselt komplexe UseCases
- Domain‑Logik ist klar strukturiert
- Keine Cross‑Aggregate‑Regeln in Aggregat‑Services
- Hohe Testbarkeit durch klare Schnittstellen

---

# 3. 🏁 Zusammenfassung

## Ohne DDD
- Entity‑Services → CRUD + Logik für eine Entität
- Orchestrator → UseCases über mehrere Entitäten
- Vorteile: SRP, Testbarkeit, Wartbarkeit

## Mit DDD
- Aggregat‑Services → Operationen für EIN Aggregat
- Orchestrator → Cross‑Aggregate‑UseCases
- klare Regeln:
    - **1 Aggregat → Aggregat‑Service**
    - **2+ Aggregate → Orchestrator**
- Domain‑Regeln bestimmen, wohin eine Methode gehört

---

[//]: # (Wenn du möchtest, kann ich dir zusätzlich:)

[//]: # ()
[//]: # (- ein Architekturdiagramm &#40;ASCII&#41;)

[//]: # (- eine Version für Confluence)

[//]: # (- oder eine erweiterte DDD‑Guideline)

[//]: # ()
[//]: # (erstellen.)