# 📦 Design von DTOs für Multi-Client-Architekturen

## 🎯 Warum unterschiedliche DTOs sinnvoll sind

Der Einsatz spezialisierter DTOs (Data Transfer Objects) pro Anwendungsfall verbessert Klarheit, Validierung, Performance und Flexibilität deiner API. Anstatt ein einziges generisches DTO zu verwenden, ermöglichen gezielt eingesetzte DTOs eine präzise Kommunikation mit verschiedenen Clients — ob Web, Mobile, Admin oder Drittanbieter.

---

## 🧠 Vorteile spezialisierter DTOs

| Kategorie               | Vorteil                                                                      |
|------------------------|-------------------------------------------------------------------------------|
| ✅ **Validierung**       | Jeder DTO kann kontextgerechte Regeln durchsetzen                            |
| 📚 **Dokumentation**     | APIs sind verständlicher und wartungsfreundlicher                            |
| 🔒 **Sicherheit**        | Vertrauliche Felder bleiben aus öffentlich zugänglichen DTOs ausgeschlossen  |
| 📱 **Effizienz**         | Mobile Clients erhalten schlanke Payloads → bessere Performance              |
| 🧩 **Flexibilität**      | Änderungen an einem DTO beeinflussen andere Clients nicht                    |
| 🔧 **Tests & Mapping**   | Spezifische DTOs erlauben gezieltes Testen und sauberes Mapping              |

---

## 🖥️ Typische Client-Szenarien

| Client            | Empfohlener DTO              | Begründung                                                      |
|-------------------|------------------------------|------------------------------------------------------------------|
| Web Frontend      | `TaskDetailDto`              | Benötigt viele Infos: Beschreibung, Fälligkeit, Priorität etc.  |
| Mobile App        | `TaskSummaryDto`             | Zeigt nur Kerninfos für Übersichtlichkeit                       |
| Admin-Oberfläche  | `AdminTaskDto`               | Kann Audit- oder systeminterne Felder enthalten                 |
| Externe APIs      | `PublicTaskDto`              | Liefert nur freigegebene Daten ohne sensible Felder             |

---

## 🧪 Beispielhafte DTOs

```java
public record CreateTaskDto(
    String title,
    String description,
    LocalDateTime dueDate,
    String status,
    String priority,
    UUID taskListId
) {}

public record TaskSummaryDto(
    UUID id,
    String title,
    String status
) {}

public record TaskDetailDto(
    UUID id,
    String title,
    String description,
    LocalDateTime dueDate,
    String status,
    String priority,
    LocalDateTime created,
    LocalDateTime updated
) {}
```

---

## 🔄 Mapping-Strategie

- Nutze z. B. **MapStruct**, **ModelMapper** oder manuelles Mapping in dedizierten `*Mapper`-Klassen
- Vermeide, Entitäten direkt ins API-Response zu geben
- Behalte strikte Trennung zwischen Domain und Transportebene

---

## ✅ Best Practices im Überblick

- Erstelle gezielte DTOs für Create, Update, View etc.
- Verwende keine Entitäten in Controller-Antworten
- Dokumentiere DTO-Felder klar (JavaDoc / Swagger)
- Nutze `record` für Immutable Payloads (Java 17+)
- Validierung durch `javax.validation` direkt in DTOs

---

💡 Diese Architektur erlaubt dir, dein System modular und zukunftssicher aufzubauen – besonders mit Blick auf verschiedene Clients oder Erweiterungen.
