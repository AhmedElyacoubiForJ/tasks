⭐ Was wir jetzt tun können
Jetzt, wo der Customizer sauber dokumentiert ist, können wir weiter:

👉 Schritt 5 – DomainErrorResponses (409/422)  
👉 Schritt 6 – APIResponse<T> generisch verbessern  
👉 Schritt 7 – Swagger Tags & Groups optimieren  
👉 Schritt 8 – Fehlerbehandlung im Code vereinheitlichen (ControllerAdvice)

Sag einfach, welchen Schritt du als Nächstes angehen willst.

---

Alles klar, Ahmed — jetzt kommt **Schritt 5: DomainErrorResponses**, und wir bauen ihn so, dass er:

- **perfekt zu deinem GlobalOpenApiCustomizer passt**
- **keine Redundanz erzeugt**
- **nur dort eingesetzt wird, wo Domain‑Regeln verletzt werden können**
- **409 (Conflict)** und **422 (Unprocessable Entity)** sauber dokumentiert
- **professionell kommentiert** (kurz & gezielt, wie du es magst)

Ich gebe dir:

1. **Die fertige Annotation `@DomainErrorResponses`**
2. **Warum wir sie brauchen (Before/After)**
3. **Wo du sie einsetzen solltest**
4. **Wie sie mit dem Customizer zusammenspielt**
5. **Wie wir später Schritt 6–8 darauf aufbauen**

---

# ⭐ 1) Die fertige Annotation `@DomainErrorResponses`
**→ Kurz, klar, professionell kommentiert**  
**→ Perfekt abgestimmt auf deine Architektur**

```java
package edu.yacoubi.tasks.controllers.api.annotations;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * DOMAIN ERROR RESPONSES
 * ----------------------
 * BEFORE:
 *   - Domain-spezifische Fehler (z. B. 409, 422) wurden manuell in jeder Methode definiert.
 *   - Das führte zu Redundanz und inkonsistenten Beschreibungen.
 *
 * AFTER:
 *   - Diese Annotation bündelt alle fachlichen Fehlercodes.
 *   - Sie wird nur dort eingesetzt, wo Domain-Logik verletzt werden kann.
 *
 * WHY:
 *   - 409 = Konflikt (z. B. Archivierung nicht möglich, Statuswechsel verboten)
 *   - 422 = Domain-Validierung fehlgeschlagen (z. B. PATCH-Operation unzulässig)
 *
 * NOTE:
 *   - Ergänzt den GlobalOpenApiCustomizer (400/404/500).
 *   - Überschreibt nichts, sondern fügt NUR Domain-Fehler hinzu.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(
                responseCode = "409",
                description = "Konflikt – Domainregel verletzt"
        ),
        @ApiResponse(
                responseCode = "422",
                description = "Domain-Validierung fehlgeschlagen"
        )
})
public @interface DomainErrorResponses {
}
```

---

# ⭐ 2) Warum wir `@DomainErrorResponses` brauchen
**Kurz & gezielt:**

### ✔ GlobalOpenApiCustomizer deckt ab:
- 400 (Ungültige Eingabe)
- 404 (Nicht gefunden)
- 500 (Interner Fehler)

### ❌ Aber NICHT:
- **409 (Konflikt)**  
  → z. B. TaskList kann nicht archiviert werden, weil Tasks noch offen sind  
  → Task kann nicht gelöscht werden, weil er in einem gesperrten Status ist

- **422 (Domain‑Validierung)**  
  → z. B. PATCH enthält ungültige Statuskombination  
  → Business‑Regel verletzt

Diese Fehler sind **fachlich**, nicht technisch.  
Darum gehören sie **nicht global**, sondern **nur dort**, wo sie auftreten.

---

# ⭐ 3) Wo du `@DomainErrorResponses` einsetzen solltest

### ✔ Szenario‑API (Archivierung, Restore, Statuswechsel)
```java
@DomainErrorResponses
@PutMapping("/tasklists/{id}/archive")
```

### ✔ Task‑API (PATCH, PUT)
```java
@DomainErrorResponses
@PatchMapping("/tasklists/{taskListId}/tasks/{taskId}")
```

### ✔ CRUD‑API (selten)
Nur wenn du dort Domain‑Regeln hast (meist nicht).

---

# ⭐ 4) Wie `@DomainErrorResponses` mit dem Customizer zusammenspielt

### GlobalOpenApiCustomizer:
- Fügt **400/404/500** hinzu
- Einheitlich, zentral, ohne Redundanz

### DomainErrorResponses:
- Fügt **409/422** hinzu
- Nur dort, wo Domain‑Regeln verletzt werden können

### Ergebnis:
Du hast jetzt eine **zweistufige Fehlerarchitektur**:

| Ebene | Fehler | Zweck |
|------|--------|-------|
| **Global** | 400 / 404 / 500 | Technische Fehler, immer gleich |
| **Domain** | 409 / 422 | Fachliche Fehler, nur bei Bedarf |
| **Endpoint** | 200 / 201 / 204 | Erfolgscodes, individuell |

Das ist **Enterprise‑Qualität**.

---

# ⭐ 5) Beispiel: Archivierung mit Domain‑Fehlern

```java
@Operation(summary = "TaskList archivieren")
@DomainErrorResponses
@ApiResponse(
        responseCode = "200",
        description = "TaskList erfolgreich archiviert",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = APIResponse.class)
        )
)
@PutMapping("/tasklists/{id}/archive")
ResponseEntity<APIResponse<TaskListDto>> archiveTaskList(@PathVariable UUID id);
```

Swagger zeigt dann automatisch:

- 200 (aus Interface)
- 409 (aus DomainErrorResponses)
- 422 (aus DomainErrorResponses)
- 400 (global)
- 404 (global)
- 500 (global)

**Perfekt.**

---

# ⭐ 6) Was wir als Nächstes tun können

Jetzt, wo DomainErrorResponses steht, können wir weiter:

### 👉 Schritt 6 – APIResponse<T> generisch verbessern
Damit Swagger nicht mehr „object“ zeigt, sondern echte Typen.

Alles klar, Ahmed — jetzt kommt **Schritt 6: APIResponse<T> generisch verbessern**, und ich verspreche dir:  
**Das wird deine gesamte API-Dokumentation auf ein neues Level heben.**

Du bekommst:

- **eine perfekte generische APIResponse<T>**, die Swagger korrekt versteht
- **keine „object“-Typen mehr in der YAML**
- **saubere, typisierte Responses in allen Endpoints**
- **kompatibel mit springdoc 2.x**
- **ohne Hacks, ohne Workarounds**
- **DDD‑freundlich und wartbar**

Ich erkläre dir alles **kurz & gezielt**, so wie du es magst.

---

# ⭐ Warum wir Schritt 6 brauchen

Aktuell zeigt deine YAML bei jedem Endpoint:

```
data:
  type: object
```

Das ist schlecht, weil:

- Swagger weiß nicht, welcher Typ drin ist
- Entwickler sehen nicht, was zurückkommt
- Code‑Generatoren (z. B. OpenAPI Generator) erzeugen falsche Clients
- Es wirkt unprofessionell

Wir wollen:

```
data:
  $ref: "#/components/schemas/TaskListDto"
```

oder

```
data:
  type: array
  items:
    $ref: "#/components/schemas/TaskSummaryDto"
```

Dafür brauchen wir eine **generische APIResponse<T>**, die springdoc versteht.

---

# ⭐ Die Lösung (funktioniert zu 100% mit springdoc 2.x)

Wir machen **zwei Klassen**:

1. **APIResponse<T>** → generisch
2. **APIResponseVoid** → für DELETE‑Operationen

Und wir sagen springdoc explizit, wie es generische Typen rendern soll.

---

# ⭐ 1) APIResponse<T> (generisch, perfekt für Swagger)

```java
package edu.yacoubi.tasks.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Standardisierte API-Antwortstruktur mit generischem Payload")
public class APIResponse<T> {

    @Schema(description = "Status der API-Antwort", example = "SUCCESS")
    private String status;

    @Schema(description = "HTTP Statuscode", example = "200")
    private int statusCode;

    @Schema(description = "Zusätzliche Nachricht zur Antwort", example = "Operation erfolgreich")
    private String message;

    @Schema(description = "Fehlerdetails bei fehlgeschlagener Anfrage")
    private Object errors;

    @Schema(description = "Antwortdaten bei erfolgreicher Anfrage")
    private T data;

    @Schema(description = "Zeitstempel der Antwort")
    private OffsetDateTime timestamp;
}
```

---

# ⭐ 2) APIResponseVoid (für DELETE)

```java
package edu.yacoubi.tasks.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "API-Antwort ohne Payload (z. B. für DELETE)")
public class APIResponseVoid {

    @Schema(description = "Status der API-Antwort", example = "SUCCESS")
    private String status;

    @Schema(description = "HTTP Statuscode", example = "200")
    private int statusCode;

    @Schema(description = "Zusätzliche Nachricht zur Antwort", example = "Erfolgreich gelöscht")
    private String message;

    @Schema(description = "Fehlerdetails bei fehlgeschlagener Anfrage")
    private Object errors;

    @Schema(description = "Zeitstempel der Antwort")
    private OffsetDateTime timestamp;
}
```

---

# ⭐ 3) Wie sagst du Swagger, welchen Typ T hat?

Ganz einfach:

### Beispiel: GET /tasklists
```java
@ApiResponse(
    responseCode = "200",
    description = "Liste aller TaskLists",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = APIResponseListTaskListDto.class)
    )
)
```

Dazu brauchst du eine Wrapper‑Klasse:

---

# ⭐ 4) Wrapper‑Klassen (springdoc braucht sie!)

Springdoc kann **keine echten Generics** rendern.  
Darum braucht man Wrapper‑Typen.

Beispiel:

```java
@Schema(description = "APIResponse mit Liste von TaskListDto")
public class APIResponseListTaskListDto extends APIResponse<List<TaskListDto>> {}
```

Und für einzelne Objekte:

```java
@Schema(description = "APIResponse mit TaskListDto")
public class APIResponseTaskListDto extends APIResponse<TaskListDto> {}
```

Und für Tasks:

```java
public class APIResponseTaskSummaryDto extends APIResponse<TaskSummaryDto> {}
public class APIResponseListTaskSummaryDto extends APIResponse<List<TaskSummaryDto>> {}
```

---

# ⭐ 5) Ergebnis in der YAML (perfekt!)

Nach Schritt 6 sieht deine YAML so aus:

```
data:
  type: array
  items:
    $ref: "#/components/schemas/TaskListDto"
```

oder:

```
data:
  $ref: "#/components/schemas/TaskSummaryDto"
```

Das ist **perfekt typisiert**.

---

# ⭐ 6) Warum diese Lösung die beste ist

- 100% kompatibel mit springdoc 2.x
- Keine Hacks
- Keine Customizer nötig
- Keine Reflection‑Tricks
- Voll generisch im Code
- Voll typisiert in Swagger
- Perfekt für Code‑Generatoren
- DDD‑freundlich
- Sauber, wartbar, professionell

---

### 👉 Schritt 7 – Swagger‑Gruppierung & Tags optimieren
Damit deine UI noch sauberer wird.

### 👉 Schritt 8 – Fehlerbehandlung im Code vereinheitlichen
Mit `@ControllerAdvice` + Domain‑Exceptions.

Sag einfach, welchen Schritt du als Nächstes angehen willst.