# 📘 Workshop: Live-Suche mit HTMX

## 🎯 Ziel

Wir bauen eine interaktive Suchkomponente, die Nutzereingaben live verarbeitet und passende Ergebnisse direkt im Interface anzeigt – ganz ohne JavaScript. HTMX übernimmt die komplette Magie des Request-Response-Zyklus.

---

## 🔧 Features

- Automatische GET-Anfragen bei Eingabe durch HTMX (`keyup`, `changed`, `delay`)
- Backend filtert Daten basierend auf Nutzereingabe
- Rückgabe als HTML-Fragment und dynamische Anzeige
- Optional: Klick auf Ergebnis zeigt Detailansicht

---

## 🧭 Workflow der Live-Suche

| Schritt | Komponente             | Beschreibung                                                                |
|--------:|------------------------|-----------------------------------------------------------------------------|
| ①       | 🧑 **Benutzereingabe**  | Nutzer tippt Suchbegriff ins Eingabefeld                                    |
| ②       | 🔗 **HTMX-Trigger**     | HTMX erkennt Eingabe via `keyup` + `changed` + `delay`                      |
| ③       | 🌐 **HTTP GET Request** | Anfrage wird an den Endpunkt `/lifeSearch/search/results?query=...` gesendet |
| ④       | 🎛️ **Controller-Endpunkt** | Spring Boot Controller empfängt die Anfrage und verarbeitet den `query`     |
| ⑤       | 🧮 **Filter-Logik**     | Daten (z.B. Personen) werden serverseitig nach dem Suchbegriff gefiltert    |
| ⑥       | 🧩 **HTML-Fragment**    | Gefilterte Ergebnisse werden in ein Fragment gerendert (`searchResults.html`) |
| ⑦       | 🔁 **UI-Update**        | HTMX ersetzt Zielcontainer (`hx-target="#results"`) mit neuem Fragment      |
| ⑧       | ✅ **Anzeige im UI**    | Ergebnisse erscheinen live unter dem Eingabefeld                            |

---

## 🧪 Beteiligte Komponenten

| Kategorie         | Name / Rolle                             |
|-------------------|-------------------------------------------|
| Frontend          | `search.html` (Template mit Eingabe + Ergebnis-Container) |
| HTMX              | `hx-get`, `hx-trigger`, `hx-target`, `delay`              |
| Controller        | `LiveSearchController.java` (`@GetMapping`)               |
| Datenmodell       | `Person.java` (z. B. `name`, `email`)                     |
| Template Fragment | `searchResults.html` (`th:fragment="results"`)           |
| Endpunkt          | `/lifeSearch/search/results`                             |

---

## 🚀 Optional: Erweiterungen

- Klickbare Ergebnisse → Details via HTMX nachladen
- Autovervollständigung oder Highlighting von Begriffen
- Anbindung echter Datenquellen (Datenbank, REST-API, JSON-Dateien)

