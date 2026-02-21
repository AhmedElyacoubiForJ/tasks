## Wie man Domain-Regeln komprimiert?

---

# ⭐ 1. Dein Ausgangspunkt: 3 verbotene Transitionen

Du hattest:

```java
if (old == COMPLETED && new == OPEN) ...
if (old == COMPLETED && new == IN_PROGRESS) ...
if (old == IN_PROGRESS && new == OPEN) ...
```

Das ist korrekt, aber redundant.

---

# ⭐ 2. Wie man solche Regeln mathematisch zusammenfasst

Wir nutzen drei einfache Werkzeuge:

---

# ⭐ Werkzeug A — Mengen (Sets)

Du kannst Statusgruppen definieren:

```text
ENDZUSTÄNDE = { COMPLETED }
NICHT_ERLAUBTE_ZIELE = { OPEN, IN_PROGRESS }
```

Dann ist die Regel:

```text
Wenn old ∈ ENDZUSTÄNDE und new ∈ NICHT_ERLAUBTE_ZIELE → verboten
```

Das ist exakt deine Regel 1 + 2.

---

# ⭐ Werkzeug B — Ordnungsrelation (Partial Order)

Du kannst Status als **geordnet** betrachten:

```text
OPEN < IN_PROGRESS < COMPLETED
```

Dann gilt:

```text
Ein Statuswechsel darf nicht rückwärts gehen.
```

Mathematisch:

```text
if (newStatus < oldStatus) → verboten
```

Das ist die **kompakteste** Form überhaupt.

---

# ⭐ Werkzeug C — Endzustand definieren

Wenn ein Zustand ein **Terminal State** ist (Endzustand), dann gilt:

```text
COMPLETED ist ein Endzustand → keine Transition außer zu sich selbst
```

Mathematisch:

```text
if (old == COMPLETED && new != COMPLETED) → verboten
```

Das deckt zwei deiner Regeln ab.

---

# ⭐ 3. Wie wir deine 3 Regeln damit zusammengefasst haben

## Deine Regeln:

1. COMPLETED → OPEN verboten
2. COMPLETED → IN_PROGRESS verboten
3. IN_PROGRESS → OPEN verboten

---

## Zusammenfassung mit Werkzeug C:

```java
if (old == COMPLETED && new != COMPLETED) → verboten
```

Das deckt Regel 1 und 2 ab.

---

## Zusammenfassung mit Werkzeug A:

```java
if (old == IN_PROGRESS && new == OPEN) → verboten
```

Das ist Regel 3.

---

# ⭐ 4. Wie du selbst solche Regeln komprimieren kannst (Schritt‑für‑Schritt‑Methode)

Ich gebe dir jetzt eine **allgemeine Methode**, die du immer anwenden kannst.

---

## ✔ Schritt 1 — Schreibe alle Regeln als Tupel

Beispiel:

```text
(COMPLETED → OPEN)
(COMPLETED → IN_PROGRESS)
(IN_PROGRESS → OPEN)
```

---

## ✔ Schritt 2 — Gruppiere nach „oldStatus“

```text
old = COMPLETED → { OPEN, IN_PROGRESS }
old = IN_PROGRESS → { OPEN }
```

---

## ✔ Schritt 3 — Prüfe, ob die Zielmenge eine logische Gruppe ist

Für COMPLETED:

```text
{ OPEN, IN_PROGRESS } = alle Status außer COMPLETED
```

→ Das ist ein Muster.

---

## ✔ Schritt 4 — Formuliere die Regel als Mengenoperation

```java
if (old == COMPLETED && new != COMPLETED)
```

---

## ✔ Schritt 5 — Prüfe, ob die übrigen Regeln eigene Bedeutung haben

Für IN_PROGRESS:

```text
{ OPEN } = nur OPEN
```

→ eigene Regel, bleibt bestehen.

---

# ⭐ 5. Ergebnis: Deine kompakte Version ist mathematisch korrekt

Und du kannst sie jetzt **selbst** herleiten.

---

# ⭐ 6. Bonus: Die mathematisch sauberste Form (wenn du willst)

Wenn du Status als Zahlen definierst:

```java
OPEN = 1
IN_PROGRESS = 2
COMPLETED = 3
```

Dann gilt:

```java
if (new.ordinal() < old.ordinal()) → verboten
```

Das ist die **komplett generische** Form einer Statusmaschine.

---

# ⭐ TODO:

👉 eine generische Statusmaschine bauen  
👉 eine mathematische DSL für Statusübergänge geben  
👉 eine Utility‑Klasse für Transition‑Validation erstellen  
👉 oder deine Task‑Statusregeln in ein Transition‑Diagramm umwandeln