# DDD Project Structure Overview

## Ziel
Die Projektstruktur trennt fachliche Logik, Anwendungslogik und technische Infrastruktur klar voneinander.
Dadurch bleibt das System wartbar, testbar und langfristig erweiterbar – unabhängig von Frameworks oder API‑Versionen.

## Domain (rein fachlich)
- Enthält ausschließlich fachliche Modelle (Entities, Value Objects).
- Enthält fachliche Regeln und Invarianten (Domain Services, Updater).
- Keine Framework‑Abhängigkeiten, keine DTOs, keine technischen Klassen.
- Domain bleibt stabil, auch wenn API, UI oder Datenbank sich ändern.

## Application (Use‑Cases, Orchestrierung)
- Enthält Anwendungslogik: Abläufe, Koordination, Geschäftsprozesse.
- Nutzt Domain‑Modelle, aber definiert selbst keine fachlichen Regeln.
- Definiert Ports (Interfaces), die von der Infrastruktur implementiert werden.
- Enthält keine technischen Details (keine JPA, keine PDF‑Libs, kein Logging).

## Infrastructure (technisch, Framework, DB, Export, Mapping)
- Implementiert Ports aus der Application‑Schicht.
- Enthält Datenbankzugriff, Repositories, Mappings, Logging, Interceptors, Export.
- Darf Frameworks nutzen (Spring, JPA, PDF‑Libs, Logging).
- Austauschbar, ohne Domain oder Application zu verändern.

## Vorteile der Struktur
- Klare Verantwortlichkeiten und weniger Kopplung.
- Domain bleibt stabil und unabhängig von Technik.
- API‑Versionierung möglich, ohne Domain anzufassen.
- Infrastruktur kann ersetzt werden (z.B. andere DB), ohne Logik zu brechen.
- Bessere Testbarkeit durch klare Schichten.

