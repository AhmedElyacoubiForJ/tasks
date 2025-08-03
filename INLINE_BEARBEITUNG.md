# 📘 Workshop: Inline-Bearbeitung mit HTMX

## 🎯 Ziel

Wir entwickeln eine Komponente zur Inline-Bearbeitung einzelner Tabellenzeilen – ganz ohne Seitenreload oder JavaScript. Ein Klick auf „Bearbeiten“ ersetzt die Tabellenzeile durch ein Formular. HTMX kümmert sich um alle magischen Übergänge.

---

## 🔧 Features

- Tabellenansicht mit „Bearbeiten“-Button pro Zeile
- HTMX lädt Formular als Fragment zur jeweiligen Zeile
- POST-Request zum Speichern → Rückgabe der aktualisierten Zeile
- Optional: „Abbrechen“-Button zum Rückbau in Ursprungsansicht

---

## 🧭 Workflow der Inline-Bearbeitung

| Schritt | Komponente               | Beschreibung                                                                  |
|--------:|--------------------------|-------------------------------------------------------------------------------|
| ①       | 📋 Tabellenanzeige        | Liste von Personen wird als Tabelle angezeigt                                 |
| ②       | 🖱️ Bearbeiten-Button      | Nutzer klickt auf „Bearbeiten“ in gewünschter Zeile                           |
| ③       | 🌐 GET Request            | HTMX sendet GET an `/inlineEdit/form?id=...`                                  |
| ④       | 🎛️ Controller-Endpunkt    | Empfängt Anfrage, liefert HTML-Fragment mit Formular                          |
| ⑤       | 🧩 Fragment-Template      | Rückgabe eines bearbeitbaren Formulars für die Zeile                          |
| ⑥       | 🔁 UI-Replacement         | HTMX ersetzt die Tabellenzeile mit dem Formular-Fragment                      |
| ⑦       | 💾 Submit-Button          | POST an `/inlineEdit/update` mit eingegebenen Daten                          |
| ⑧       | 🧠 Validierung & Update   | Controller verarbeitet & prüft die Daten, aktualisiert das Modell             |
| ⑨       | 🔄 Austausch im UI        | HTMX ersetzt Formular durch neue Tabellenzeile mit aktualisierten Werten     |

---

## 📍 Beteiligte Komponenten

| Kategorie          | Name / Rolle                                  |
|--------------------|------------------------------------------------|
| Frontend           | `table.html` (Darstellung + Edit-Trigger)     |
| HTMX               | `hx-get`, `hx-post`, `hx-target`, `hx-swap`   |
| Backend            | `InlineEditController.java` (`GET` & `POST`)  |
| Datenmodell        | `Person.java` (`id`, `name`, `email`)         |
| Fragment-Template  | `rowEditFragment.html` (Formular)             |
| Rückgabe-Fragment  | `tableRow.html` (aktualisierte Zeile)         |
| Endpunkte          | `/inlineEdit/form`, `/inlineEdit/update`      |

---

## 🚀 Fokus

- Minimales Setup für schlanke Bearbeitung direkt im UI
- Keine JavaScript-Logik notwendig – HTMX übernimmt Kommunikation
- Ideal für überschaubare Datenmodelle in Mini-Projekten