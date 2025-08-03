# HTMX Demos

### 🔥 3 Demo-Vorschläge mit HTMX

1. **Live-Suche mit Dropdown**
    - Nutzer tippt Namen → dynamisch gefilterte Liste erscheint darunter
    - Nutzt `hx-get` beim Tippen + `hx-target` auf die Ergebnisliste
    - Perfekt für Kontakt- oder Produkt-Suche

2. **Inline-Bearbeitung von Tabellenzeilen**
    - Klick auf „Bearbeiten“-Button → Zeile verwandelt sich in ein Formular
    - HTMX für Partial-Rendering & Validierung
    - Zeigt, wie elegant Backend-Interaktionen sein können

3. **Quiz-Komponente mit Fortschrittsleiste**
    - Frage erscheint → Antwort auswählen → nächste Frage via HTMX laden
    - Fortschrittsbalken wird bei jeder Antwort aktualisiert
    - Top für „Step-by-step“-Formulare oder Trainingsmodule

---

🧠 Du kannst auch experimentieren mit:
- lazy loading (`hx-trigger="revealed"`)
- Server-sent events (`hx-sse`)
- „Undo“-Buttons mit temporärer Darstellung

Wenn du magst, können wir den Einstieg noch abrunden mit:

🧪 Validierung im handleSubmit() (mit BindingResult)

🔁 Wiederholbare Aktionen (z.B. zurück zur Liste oder Formular)

🌐 Internationalisierung mit messages.properties

📈 Logging für Demo-Zwecke
