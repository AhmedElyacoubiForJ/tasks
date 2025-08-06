# 📚 HTMX Demo 3: Quiz-Komponente mit Fortschrittsleiste

## 🧩 Ziel
Eine interaktive Quiz-Komponente, die Fragen Schritt für Schritt lädt und den Fortschritt visuell darstellt – ideal für Trainingsmodule oder Step-by-Step-Formulare.

---

## ⚙️ Features

- **Frage anzeigen**  
  Eine einzelne Frage mit Antwortmöglichkeiten (Multiple Choice).

- **Antwort auswählen**  
  Benutzer klickt auf eine Option → Antwort wird verarbeitet.

- **Fortschrittsleiste aktualisieren**  
  Zeigt den Fortschritt, z. B. „Frage 3 von 10“.

- **Nächste Frage laden**  
  HTMX lädt die nächste Frage dynamisch ohne Seitenreload.

---

## 🗃️ Datenmanagement

- **Keine Datenbank erforderlich**  
  Um das Tasks-Projekt nicht zu belasten, verzichten wir auf DB-Zugriffe.

- **Fragen & Antworten in Listen**  
  Die Fragen werden als einfache Liste im Java-Backend verwaltet.

- **Session oder temporärer Speicher**  
  Fortschritt des Users wird z. B. in der Session oder einem temporären Speicher gehalten.

---

## 🧭 Integration in bestehende App

- Die Komponente wird später unter dem Package `htmxdemo` integriert.
- Die bestehende Spring Boot Umgebung der `tasks` App bleibt unberührt.
- Fokus liegt auf modularer Erweiterung ohne zusätzliche Persistenzschicht.

---

## ✅ Vorteile

- Kein Reload der Seite
- Kein Datenbank-Overhead
- Schnelle, interaktive UX
- Ideal für Lernmodule, Trainings, Formulare

---

## 📝 Nächste Schritte

- Fragenliste definieren
- HTMX-Endpunkte für Frage & Fortschritt erstellen
- Fortschrittslogik implementieren
- UI-Design für Quiz und Fortschrittsbalken
- Integration unter `htmxdemo` vorbereiten

---

**Let’s quiz it up! 🚀**
