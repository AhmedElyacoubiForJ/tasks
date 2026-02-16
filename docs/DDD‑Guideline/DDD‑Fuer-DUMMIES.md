# 🟢 **DDD‑Guideline für DUMMIES**
Super einfach. Super klar. Ohne Fachbegriffe. Ohne Theorie.  
So, dass es sogar jemand versteht, der noch nie DDD gehört hat.

---

# 🧠 **DDD für Dummies – Was du WIRKLICH wissen musst**

## 1. **Ein „Ding“ gehört immer jemandem**
In meinem Projekt:

- Eine **TaskList** ist der Chef
- Die **Tasks** gehören der TaskList
- Niemand darf an den Tasks vorbei direkt etwas tun
- Alles läuft über die TaskList

👉 **TaskList ist der Boss. Tasks sind Mitarbeiter.**

---

## 2. **Nur der Boss darf Regeln machen**
Beispiel:

„Eine TaskList darf nur archiviert werden, wenn alle Tasks fertig sind.“

Das entscheidet:

❌ nicht der Controller  
❌ nicht der Orchestrator  
❌ nicht der Service  
❌ nicht der Client  
❌ nicht irgendein DTO

👉 **Das entscheidet NUR die TaskList selbst.**

---

## 3. **Der Orchestrator ist nur der Projektmanager**
Er macht:

- TaskList laden
- TaskList sagen: „Mach deine Arbeit“
- TaskList speichern

Er macht NICHT:

❌ keine Regeln  
❌ keine Berechnungen  
❌ keine Entscheidungen  
❌ keine DTO‑Bastelei  
❌ keine Task‑Laderei

👉 **Er koordiniert nur.**

---

## 4. **Der Service ist nur der Daten-Lieferant**
Er macht:

- Laden
- Speichern

Er macht NICHT:

❌ keine Regeln  
❌ keine Entscheidungen  
❌ keine Logik  
❌ keine DTOs

👉 **Er ist nur der Typ, der zur Datenbank geht.**

---

## 5. **Der Controller ist nur der Türsteher**
Er macht:

- Anfrage reinlassen
- Orchestrator rufen
- Antwort zurückgeben

Er macht NICHT:

❌ keine Regeln  
❌ keine Entscheidungen  
❌ keine Domain‑Arbeit

👉 **Er sagt nur: „Hey Orchestrator, mach mal.“**

---

## 6. **DTOs sind nur Verpackungen**
Sie sind:

- hübsch
- leicht
- API‑freundlich

Aber:

❌ Sie dürfen NICHT in Domain  
❌ Sie dürfen NICHT in Orchestrator  
❌ Sie dürfen NICHT in Service

👉 **DTOs sind nur für Controller.**

---

## 7. **Domain ist der König**
Die Domain:

- entscheidet
- schützt Regeln
- kennt die Wahrheit
- weiß, was erlaubt ist
- weiß, was verboten ist

👉 **Domain = Gehirn des Systems.**

---

## 8. **Regeln gehören IMMER in die Domain**
Wenn du irgendwo eine Regel siehst wie:

- „Nur wenn alle Tasks fertig sind“
- „Titel darf nicht leer sein“
- „Status darf nicht zurückgesetzt werden“

Dann gehört das:

👉 **in die Domain**  
und NUR dort.

---

## 9. **Wenn du etwas zweimal prüfst, ist es falsch**
Beispiel:

- Controller prüft
- Orchestrator prüft
- Domain prüft

Das ist falsch.

👉 **Nur Domain prüft.**

---

## 10. **Wenn du DTOs im Orchestrator siehst, ist es falsch**
Ganz einfach:

- DTOs = API
- Domain = Regeln
- Orchestrator = Ablauf
- Service = Datenbank

Wenn du DTOs im Orchestrator siehst:

👉 **Fehler.**

---

# 🟢 **Kurzfassung für Dummies**

- **Domain** = entscheidet
- **Orchestrator** = koordiniert
- **Service** = lädt/speichert
- **Controller** = nimmt Requests an
- **DTOs** = nur für API
- **Regeln** = nur in Domain
- **Tasks** gehören zur TaskList
- **TaskList ist der Chef**
- **Alles läuft über den Chef**
