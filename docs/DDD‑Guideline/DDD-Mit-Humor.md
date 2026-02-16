# 😂 **DDD – Die Humor‑Version**
*Weil Architektur auch Spaß machen darf.*

---

## **1. Du sollst keine anderen Götter neben der Domain haben.**
Wenn du eine Regel im Controller findest, lösche sie.  
Wenn du eine Regel im Orchestrator findest, lösche sie.  
Wenn du eine Regel im Service findest, lösche sie.  
Wenn du eine Regel im Frontend findest…  
…schrei kurz und lösche sie dann auch.

---

## **2. Aggregate Roots sind wie Türsteher im Club.**
Wenn du nicht auf der Liste stehst, kommst du nicht rein.  
Und Tasks stehen **nicht** auf der Liste.  
Nur die TaskList entscheidet, wer rein, raus oder archiviert wird.

---

## **3. Setter sind wie offene Haustüren.**
Jeder kann reinlaufen und Chaos machen.  
Mach sie zu.  
Benutz Domain‑Methoden.  
Danke.

---

## **4. Der Orchestrator ist ein Projektmanager.**
Er redet viel, macht aber nichts selbst.  
Er sagt nur:  
„TaskList, mach mal.“  
Und TaskList macht.

---

## **5. Der Controller ist ein Empfangsmitarbeiter.**
Er nimmt Pakete entgegen.  
Er gibt Pakete raus.  
Er macht keine Magie.  
Er macht keine Regeln.  
Er macht keine Entscheidungen.  
Er macht Kaffee. Vielleicht.

---

## **6. Services sind wie DHL.**
Sie liefern Dinge aus.  
Sie holen Dinge ab.  
Sie entscheiden nicht, ob du archivieren darfst.  
Sie bringen nur das Paket zur Domain und wieder zurück.

---

## **7. DTOs sind wie Geschenkpapier.**
Sie sehen hübsch aus.  
Sie schützen den Inhalt.  
Aber niemand benutzt Geschenkpapier, um ein Haus zu bauen.  
Also pack sie nicht in die Domain.

---

## **8. Invarianten sind wie Naturgesetze.**
Wenn du sie brichst, explodiert das Universum.  
Oder zumindest dein Projekt.  
Also:  
**Domain prüft. Immer.**

---

## **9. UseCases sind wie One‑Way‑Tickets.**
Du startest.  
Du kommst an.  
Oder du stürzt ab.  
Aber du fliegst nicht zwischendurch zurück, um zu fragen, ob du landen darfst.  
Check‑Endpoints sind verboten.

---

## **10. Wenn du eine Regel zweimal findest, ist sie schon kaputt.**
Doppelte Logik ist wie zwei Fernbedienungen für denselben Fernseher.  
Chaos.  
Streit.  
Und irgendjemand drückt immer die falsche Taste.

---

# 😂 **Kurzfassung der Humor‑Version**

- Domain = Chef
- Orchestrator = Projektmanager
- Controller = Empfang
- Service = DHL
- DTO = Geschenkpapier
- Aggregate = Türsteher
- Regeln = Naturgesetze
- Setter = offene Haustüren
- UseCases = One‑Way‑Tickets
- Doppelte Logik = Weltuntergang