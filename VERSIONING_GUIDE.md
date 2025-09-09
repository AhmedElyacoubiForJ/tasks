# 📘 VERSIONING_GUIDE.md
_Ein praktischer Leitfaden zur Versionsstrategie und Release-Workflow_

---

## 🧩 Warum Versionierung?

Versionierung ist mehr als nur eine Zahl – sie ist ein Werkzeug zur Kommunikation. Sie zeigt, wie weit deine Software ist, was sich verändert hat und ob sie stabil genug für den Einsatz ist.

Versionierung hilft dir bei:

- 📦 Release-Management
- 🔁 Rollbacks & Fehleranalyse
- 🚀 CI/CD & Deployment
- 📜 Dokumentation & Changelogs

---

## 🔢 Semantische Versionierung (SemVer)

Das Standardformat lautet: `MAJOR.MINOR.PATCH`

| Teil     | Bedeutung                                                                 |
|----------|---------------------------------------------------------------------------|
| `MAJOR`  | Bricht Kompatibilität (z. B. API-Änderung, Architekturwechsel)            |
| `MINOR`  | Neue Features, aber kompatibel (z. B. neue Use Cases, Erweiterungen)      |
| `PATCH`  | Fehlerbehebungen, kleine Verbesserungen, Refactoring                      |

### Beispielverlauf:

| Version     | Bedeutung                                      |
|-------------|------------------------------------------------|
| `0.1.0`     | Erste lauffähige Version mit minimalem Feature |
| `0.2.0`     | Neue Funktion: Taskliste löschen               |
| `0.2.1`     | Bugfix: UI-Fehler behoben                      |
| `1.0.0`     | Erste stabile Version                          |

---

## 🧪 Pre-Releases & Entwicklungsphasen

Wenn deine App noch nicht produktionsreif ist, kannst du mit **Pre-Release-Tags** arbeiten:

| Tag              | Bedeutung                                  |
|------------------|---------------------------------------------|
| `0.1.0-dev`      | Entwicklungsstand, noch instabil            |
| `0.1.0-alpha`    | Erste Tests, viele Bugs erwartet            |
| `0.1.0-beta`     | Fast fertig, aber noch nicht produktiv      |
| `1.0.0-rc.1`     | Release Candidate — fast stabil             |
| `1.0.0`          | Finaler Release                             |

➡️ Diese Tags helfen dir, Releases zu testen, ohne Stabilität zu versprechen.

---

## 🧭 Typischer Workflow für neue Features

Wenn du ein neues Feature entwickelst (z.B. „Task löschen“), kannst du so vorgehen:

1. `1.1.0-dev` → Du beginnst mit der Implementierung
2. `1.1.0-alpha` → Erste Tests, noch instabil
3. `1.1.0-beta` → Feature fast fertig, UI stabilisiert
4. `1.1.0-rc.1` → Release Candidate für finale Tests
5. `1.1.0-rc.2` → Optional, falls du nach RC.1 noch etwas korrigierst
6. `1.1.0` → Finaler Release

➡️ Man kann beliebig viele RCs machen (`rc.3`, `rc.4`…), bis man zufrieden ist.

---

## 🔖 Git-Tags setzen

Git-Tags sind wie Marker in deiner Projektgeschichte. Sie helfen dir, Releases zu identifizieren und später darauf zurückzugreifen.

```bash
git tag -a v0.1.0-dev -m "Erste lauffähige Version mit Task-Listen"
git push origin v0.1.0-dev
```

Später:

```bash
git tag -a v1.0.0-rc.1 -m "Feature vollständig, bereit für Release"
git push origin v1.0.0-rc.1
```

---

## 🛠️ Automatisierung mit Makefile

Du kannst Releases direkt über dein Makefile steuern:

```bash
make release VERSION=1.1.0-beta
```

➡️ Baut das Docker-Image, taggt es und setzt automatisch einen Git-Tag.

---

## 📌 Fazit

- Du kannst jederzeit taggen – auch mitten in der Entwicklung
- Pre-Releases sind perfekt für Tests und interne Deployments
- SemVer gibt dir Struktur und Klarheit
- Git-Tags sind deine Ankerpunkte für Releases und Changelogs
- Du entscheidest, wann ein Feature „reif“ für ein Release ist

---

[//]: # (Ahmed, du hast das Konzept **fast perfekt** verstanden — und du denkst bereits wie jemand, der Releases strategisch plant. Ich helfe dir, die letzten Feinheiten zu klären, damit du mit voller Sicherheit versionieren kannst.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## ✅ Deine Annahme: Feature fertig → `1.0.0-rc.1`)

[//]: # ()
[//]: # (**Ja, genau!**  )

[//]: # (Wenn dein erstes Feature vollständig ist &#40;z. B. Task-Listen anzeigen + erstellen&#41;, kannst du sagen:)

[//]: # (> „Ich bin kurz vor dem ersten stabilen Release.“)

[//]: # ()
[//]: # (Dann ist `1.0.0-rc.1` &#40;Release Candidate 1&#41; **genau richtig**.  )

[//]: # (Wenn du keine Bugs findest, kannst du daraus `1.0.0` machen.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 🔄 Neue Features → `1.1.0-dev` bis `1.1.0-rc.2`)

[//]: # ()
[//]: # (Auch das hast du richtig gedacht. Hier ist der typische Ablauf:)

[//]: # ()
[//]: # (| Version         | Bedeutung                                               |)

[//]: # (|-----------------|----------------------------------------------------------|)

[//]: # (| `1.1.0-dev`     | Du beginnst mit neuen Features, noch instabil            |)

[//]: # (| `1.1.0-alpha`   | Erste Tests, viele Dinge sind noch unfertig              |)

[//]: # (| `1.1.0-beta`    | Fast fertig, aber noch nicht produktionsreif             |)

[//]: # (| `1.1.0-rc.1`    | Release Candidate – bereit für finale Tests              |)

[//]: # (| `1.1.0-rc.2`    | Zweiter RC, falls du nach RC.1 noch etwas korrigierst    |)

[//]: # (| `1.1.0`         | Finaler Release mit neuen Features                       |)

[//]: # ()
[//]: # (➡️ Du kannst beliebig viele RCs machen &#40;`rc.3`, `rc.4`…&#41;, bis du zufrieden bist.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 🧠 Bonus-Tipp: Wann ist ein „richtiger“ Release sinnvoll?)

[//]: # ()
[//]: # (- Wenn du **keine offenen Bugs** hast)

[//]: # (- Wenn du **alle geplanten Features** für diese Version fertig hast)

[//]: # (- Wenn du **bereit bist, es produktiv einzusetzen oder zu veröffentlichen**)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 📘 Beispiel für deinen Fall)

[//]: # ()
[//]: # (Du hast:)

[//]: # (- ✅ Task-Listen anzeigen)

[//]: # (- ✅ Task-Listen erstellen)

[//]: # (- ❌ Noch kein Bearbeiten/Löschen)

[//]: # (- ❌ Refactoring steht aus)

[//]: # ()
[//]: # (➡️ Du könntest jetzt:)

[//]: # (```bash)

[//]: # (git tag -a v0.1.0-dev -m "Erste lauffähige Version mit Task-Listen")

[//]: # (```)

[//]: # ()
[//]: # (Und später:)

[//]: # (```bash)

[//]: # (git tag -a v1.0.0-rc.1 -m "Feature vollständig, bereit für Release")

[//]: # (```)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (Du hast das Prinzip verstanden, Ahmed — und du setzt es **strategisch und sauber** um. Sag Bescheid, wenn du eine Vorlage für `CHANGELOG.md` willst oder ein `make changelog`-Target, das automatisch die Unterschiede zwischen zwei Tags zeigt. Du baust hier ein Release-System, das nicht nur funktioniert — sondern Vertrauen schafft 💪🧠)