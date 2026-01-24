# 💡 **Umstellung-Probleme auf DDD**

Wenn man ein bestehendes System auf DDD umbaut:

- brechen alte Schichten plötzlich auseinander
- Services hängen an Entities, die es nicht mehr gibt
- DTOs passen nicht mehr zur Domain
- Mapper verlieren Felder
- Controller erwarten alte Strukturen
- Repositories sind plötzlich falsch platziert
- Validierungen sitzen an der falschen Stelle

Es ist *normal*, dass man erst einmal „durchkommt“, indem man:

- Dinge auskommentiert
- temporäre Hacks einbaut
- alte Methoden stehen lässt
- TODOs sammelt
- Compiler‑Fehler wegdrückt, um weiterzukommen

Das Entscheidende ist:  
**Man hat jetzt zwei vollständige, saubere DDD‑Flows: CreateTask und UpdateTask.**  
Das ist der harte Teil. Der Rest wird jetzt viel leichter, weil du ein Muster hast.

---

# 🎯 Was jetzt sinnvoll wäre (wenn du weitermachen willst)

Du musst nicht alles auf einmal umstellen.  
Ich würde dir empfehlen, in dieser Reihenfolge weiterzugehen:

## 1. DeleteTask‑Flow
Der ist meist der einfachste:
- Task laden
- Zugehörigkeit prüfen
- Domain‑Methode `delete()` oder `archive()`
- Persistieren
- Response

## 2. GetTask / GetTasks‑Flows
Hier kannst du die neue Struktur nutzen:
- Orchestrator
- Services
- Transformer
- DTOs
- Keine Domain‑Änderungen

## 3. TaskList‑Flows (Create, Update, Delete)
Wenn du die Task‑Flows sauber hast, sind TaskLists fast schon Copy‑Paste‑DDD.

## 4. Validierungen und Fehlerbilder vereinheitlichen
Das macht das System stabil und vorhersehbar.

## 5. Auskommentierte Stellen systematisch abbauen
Jetzt, wo du Muster hast, kannst du die alten Baustellen sauber schließen.

---

# 🧠 Und das Wichtigste

Du musst nicht alles alleine entscheiden.  
Wenn du sagst:

> „Ich schau mal, was ich noch umstellen soll, dann sage ich dir Bescheid“

…dann bin ich bereit, mit dir **jede einzelne Stelle** durchzugehen:

- Was gehört in die Domain?
- Was bleibt im Service?
- Was kommt in den Orchestrator?
- Welche DTOs brauchen wir?
- Welche Mapper/Transformer sind sinnvoll?
- Welche Validierungen gehören wohin?
- Welche alten Stellen können weg?

Sag einfach, sobald du die nächste Baustelle gefunden hast — wir räumen sie gemeinsam auf.