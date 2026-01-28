# 📘 **Inhalt der Datei (`rest-ddd-put-vs-patch-subresources.md`)**


# PUT vs. PATCH in Domain‑Driven Design  
## Best Practices für REST‑APIs mit Subresources wie `/tasklists/{taskListId}/tasks/{taskId}`

In REST gibt es zwei Methoden, um bestehende Ressourcen zu verändern: **PUT** und **PATCH**.  
Obwohl beide technisch funktionieren, haben sie sehr unterschiedliche Bedeutungen – und in Domain‑Driven Design (DDD) führt das zu klaren Empfehlungen.

---

## 🧠 Grundidee

- **PUT** ersetzt eine Ressource *vollständig*  
- **PATCH** ändert gezielt *einzelne Eigenschaften*  

In DDD werden Aggregate jedoch **nicht ersetzt**, sondern **über Verhalten verändert**.  
Deshalb ist PATCH in DDD fast immer die bessere Wahl.

---

## 🧩 Warum PUT in DDD selten sinnvoll ist

PUT bedeutet:

> „Hier ist der komplette neue Zustand. Ersetze den alten vollständig.“

Das widerspricht DDD, weil Aggregate:

- Invarianten besitzen  
- Verhalten ausdrücken  
- nicht als reine Datencontainer gedacht sind  
- nicht komplett überschrieben werden sollen  

PUT führt oft zu:

- Anämischen Domain‑Modellen  
- Verlust von Invarianten  
- unklaren Use‑Cases  
- „CRUD statt DDD“

---

## 🧠 Warum PATCH besser zu DDD passt

PATCH bedeutet:

> „Ändere nur das, was im Request steht.“

Das passt perfekt zu Domain‑Methoden wie:

- `rename(title)`
- `changeDescription(description)`
- `changeDueDate(date)`
- `markDone()`
- `reopen()`

PATCH unterstützt:

- explizite Use‑Cases  
- Domain‑Verhalten  
- Aggregate‑Regeln  
- Evolution der API  

---

## 🎯 Empfehlung für DDD‑basierte Systeme

### ✔ **PATCH verwenden**, wenn:
- ein Aggregate verändert wird  
- nur einzelne Felder geändert werden  
- Domain‑Methoden existieren  
- Use‑Cases klar abgegrenzt sind  

### ✔ **PUT verwenden**, wenn:
- eine Ressource vollständig ersetzt wird  
- keine Domain‑Regeln verletzt werden  
- das Objekt rein technisch ist (z. B. Settings, Configs)  

Für Tasks gilt:

👉 **PATCH ist die richtige Wahl**, besonders bei Subresources wie:

```
/tasklists/{taskListId}/tasks/{taskId}
```

---

## 🧱 Zwei mögliche PATCH‑Strategien

### **Option A — Generisches PATCH**
Eine Route:

```
PATCH /tasklists/{taskListId}/tasks/{taskId}
```

DTO enthält optionale Felder:

```json
{
  "title": "...",
  "description": "...",
  "status": "DONE"
}
```

Service:

```java
if (dto.title() != null) task.rename(dto.title());
if (dto.description() != null) task.changeDescription(dto.description());
if (dto.status() != null) task.changeStatus(dto.status());
```

---

### **Option B — Use‑Case‑spezifische PATCH‑Routen**
Maximal DDD‑konform:

```
PATCH /tasklists/{taskListId}/tasks/{taskId}/rename
PATCH /tasklists/{taskListId}/tasks/{taskId}/complete
PATCH /tasklists/{taskListId}/tasks/{taskId}/reopen
PATCH /tasklists/{taskListId}/tasks/{taskId}/reschedule
```

Jede Route entspricht einer Domain‑Methode.

---

## 🏁 Fazit

- PUT ist für vollständige Ersetzungen gedacht
- PATCH ist für gezielte Änderungen gedacht
- In DDD werden Aggregate **verändert**, nicht ersetzt
- PATCH ist daher die **Best Practice** für Aggregate wie Tasks oder TaskLists
- Besonders bei Subresources wie `/tasklists/{taskListId}/tasks/{taskId}` ist PATCH die semantisch korrekte Wahl

PATCH unterstützt Domain‑Methoden, Use‑Cases und saubere Architektur – und ist damit die bessere Wahl für DDD‑basierte REST‑APIs.