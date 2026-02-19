## ⭐ Wenn du willst, gehen wir jetzt weiter zu:
>  👉 Schritt 8 – Fehlerbehandlung vereinheitlichen mit:

- @ControllerAdvice

- DomainExceptions

- Validation‑Mapping

- Logging

- Trace‑ID

APIResponse‑Factory

Sag einfach:

„Schritt 8“


---

Ahmed… ich liebe diesen Moment.  
Du hast mir gerade deine komplette `RestExceptionHandler`‑Klasse gegeben — und ich sehe sofort:

👉 **Du hast schon sehr viel richtig gemacht.**  
Aber:  
👉 **Sie passt nicht mehr zu deiner neuen API‑Architektur (Wrapper‑Klassen, Domain‑Fehler, konsistente Responses).**

Schritt 8 bedeutet:  
**Wir bringen deine Fehlerbehandlung auf das gleiche professionelle Niveau wie deine API‑Doku.**

Ich zeige dir jetzt:

1. **Was an deiner aktuellen Klasse gut ist**
2. **Was jetzt nicht mehr zu deiner API passt**
3. **Wie wir eine perfekte, moderne Fehlerarchitektur bauen**
4. **Wie wir Domain‑Fehler (409/422) sauber integrieren**
5. **Wie wir APIResponse‑Wrapper automatisch erzeugen**
6. **Wie wir Logging + Trace‑ID einbauen**

Und dann bekommst du eine **komplett neue, perfekte Fehlerarchitektur**, die zu deiner API passt.

---

# ⭐ 1. Was an deiner aktuellen Klasse gut ist

- Du deckst alle wichtigen Fehler ab
- Du nutzt `@RestControllerAdvice`
- Du loggst sauber
- Du gibst APIResponse zurück
- Du hast Validierungsfehler korrekt abgebildet
- Du hast 404, 400, 405, 415, 500 sauber getrennt

Das ist eine sehr solide Basis.

---

# ⭐ 2. Was jetzt NICHT mehr zu deiner API passt

### ❌ Problem 1 — Du gibst `APIResponse<T>` zurück
Aber deine API nutzt **Wrapper‑Klassen**:

- `APIResponseTaskListDto`
- `APIResponseTaskSummaryDto`
- `APIResponseListTaskListDto`
- `APIResponseListTaskSummaryDto`
- `APIResponseVoid`

→ **APIResponse<T> ist nicht mehr Teil der öffentlichen API.**

### ❌ Problem 2 — Domain‑Fehler (409/422) fehlen
Du hast in deiner YAML:

- `409` → Domainregel verletzt
- `422` → Domain‑Validierung fehlgeschlagen

Aber in deinem Handler gibt es **keine DomainExceptions**.

### ❌ Problem 3 — Fehler sind nicht typisiert
Swagger zeigt aktuell:

```
APIResponseVoid
APIResponseTaskListDto
APIResponseTaskSummaryDto
...
```

Aber Fehler kommen als:

```
APIResponse<Object>
```

Das ist inkonsistent.

### ❌ Problem 4 — Kein zentrales Response‑Factory
Du baust APIResponse in jedem Handler manuell.

→ Das erzeugt Boilerplate.  
→ Das ist fehleranfällig.  
→ Das ist nicht DDD‑konform.

### ❌ Problem 5 — Kein Trace‑ID
Für Logging und Debugging wäre das extrem wertvoll.

---

# ⭐ 3. Was wir jetzt bauen (Schritt 8 – Zielbild)

Wir bauen:

## ✔ A) Eigene DomainExceptions
- `DomainValidationException` → 422
- `DomainRuleViolationException` → 409

## ✔ B) Eine zentrale Response‑Factory
z. B.:

```java
public class ApiResponseFactory {
    public static <T> APIResponse<T> error(int status, String message, List<ApiErrorResponse> errors) { ... }
}
```

## ✔ C) Ein neuer, sauberer `GlobalExceptionHandler`
- 404 → EntityNotFoundException
- 400 → Validation
- 409 → DomainRuleViolationException
- 422 → DomainValidationException
- 500 → Exception

## ✔ D) Einheitliche Fehlerstruktur
Alle Fehler nutzen:

```
APIResponseVoid
```

→ weil Fehler **nie** Daten zurückgeben.

## ✔ E) Trace‑ID
Wir fügen automatisch eine Trace‑ID hinzu:

- in Logs
- in APIResponse

---

# ⭐ 4. Ich zeige dir jetzt die perfekte neue Fehlerarchitektur

## 📌 A) DomainExceptions

```java
package edu.yacoubi.tasks.exceptions;

public class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}
```

```java
package edu.yacoubi.tasks.exceptions;

public class DomainRuleViolationException extends RuntimeException {
    public DomainRuleViolationException(String message) {
        super(message);
    }
}
```

---

## 📌 B) Response‑Factory

```java
package edu.yacoubi.tasks.controllers.api;

import java.time.LocalDateTime;
import java.util.List;

public class ApiResponseFactory {

    public static APIResponseVoid error(int status, String message, List<ApiErrorResponse> errors) {
        return APIResponseVoid.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(status)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
```

---

## 📌 C) Neuer GlobalExceptionHandler (perfekt abgestimmt)

Ich zeige dir nur den Kern — wenn du willst, generiere ich dir die komplette Klasse.

### 404 – EntityNotFoundException

```java
@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<APIResponseVoid> handleNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponseFactory.error(
                    404,
                    "Ressource nicht gefunden",
                    List.of(new ApiErrorResponse(404, ex.getMessage()))
            ));
}
```

### 422 – DomainValidationException

```java
@ExceptionHandler(DomainValidationException.class)
public ResponseEntity<APIResponseVoid> handleDomainValidation(DomainValidationException ex) {
    return ResponseEntity.status(422)
            .body(ApiResponseFactory.error(
                    422,
                    "Domain-Validierung fehlgeschlagen",
                    List.of(new ApiErrorResponse(422, ex.getMessage()))
            ));
}
```

### 409 – DomainRuleViolationException

```java
@ExceptionHandler(DomainRuleViolationException.class)
public ResponseEntity<APIResponseVoid> handleDomainRule(DomainRuleViolationException ex) {
    return ResponseEntity.status(409)
            .body(ApiResponseFactory.error(
                    409,
                    "Domainregel verletzt",
                    List.of(new ApiErrorResponse(409, ex.getMessage()))
            ));
}
```

### 400 – Validation

→ bleibt wie bei dir, aber mit APIResponseVoid

### 500 – Exception

→ bleibt wie bei dir, aber mit APIResponseVoid

---

# ⭐ 5. Was du jetzt bekommst

Wenn du willst, generiere ich dir:

👉 **die komplette neue Fehlerarchitektur als fertige Klassen**  
👉 **inkl. DomainExceptions**  
👉 **inkl. Response‑Factory**  
👉 **inkl. GlobalExceptionHandler**  
👉 **inkl. Trace‑ID‑Integration**  
👉 **inkl. Logging‑Verbesserungen**  
👉 **inkl. Swagger‑Dokumentation**

Sag einfach:

**„Bitte komplette Fehlerarchitektur generieren“**

Ich bin bereit.

---
### Überblick

Ich baue dir jetzt eine **vollständige, saubere Fehlerarchitektur**, passend zu deiner aktuellen API:

- Trennung **Infrastructure‑Exceptions** vs. **Domain‑Exceptions**
- Einheitlicher Response‑Typ für Fehler: **`APIResponseVoid`**
- Zentrale **Factory** für Fehler‑Responses
- Ein **GlobalExceptionHandler** statt verstreuter Logik
- Alles so, dass deine OpenAPI/YAML weiterhin stimmt

Ich nutze deine bestehenden Klassen (`APIResponse`, `APIResponseVoid`, `ApiErrorResponse`, `ResponseStatus`) und passe nur das Fehler‑System an.

---

## 1. Domain‑Exceptions

```java
package edu.yacoubi.tasks.exceptions;

/**
 * 422 – Domain-Validierung fehlgeschlagen
 * Beispiel: Fachliche Regeln zu Werten, Zuständen, Übergängen.
 */
public class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}
```

```java
package edu.yacoubi.tasks.exceptions;

/**
 * 409 – Domainregel verletzt
 * Beispiel: Archivieren, obwohl noch offene Tasks existieren.
 */
public class DomainRuleViolationException extends RuntimeException {
    public DomainRuleViolationException(String message) {
        super(message);
    }
}
```

---

## 2. Response‑Factory für Fehler

```java
package edu.yacoubi.tasks.controllers.api;

import java.time.LocalDateTime;
import java.util.List;

public final class ApiResponseFactory {

    private ApiResponseFactory() {
    }

    public static APIResponseVoid error(int httpStatus, String message, List<ApiErrorResponse> errors) {
        return APIResponseVoid.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(httpStatus)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static APIResponseVoid errorSingle(int httpStatus, String message, String detail) {
        ApiErrorResponse error = new ApiErrorResponse(httpStatus, detail);
        return error(httpStatus, message, List.of(error));
    }
}
```

> Wichtig: `APIResponseVoid` ist dein Wrapper ohne `data`‑Payload und passt perfekt für Fehler.

---

## 3. Neuer GlobalExceptionHandler

Ersetzt deinen bisherigen `RestExceptionHandler` (oder du benennst ihn um).

```java
package edu.yacoubi.tasks.exceptions;

import edu.yacoubi.tasks.controllers.api.APIResponseVoid;
import edu.yacoubi.tasks.controllers.api.ApiErrorResponse;
import edu.yacoubi.tasks.controllers.api.ApiResponseFactory;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Hidden
@Slf4j
public class GlobalExceptionHandler {

    // 404 – Entity nicht gefunden (JPA)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIResponseVoid> handleNotFound(EntityNotFoundException ex,
                                                          HttpServletRequest request) {
        log.warn("⚠️ 404 Not Found für {}: {}", request.getRequestURI(), ex.getMessage());
        APIResponseVoid body = ApiResponseFactory.errorSingle(
                HttpStatus.NOT_FOUND.value(),
                "Ressource nicht gefunden",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // 422 – Domain-Validierung
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<APIResponseVoid> handleDomainValidation(DomainValidationException ex,
                                                                  HttpServletRequest request) {
        log.warn("⚠️ 422 Domain-Validierung fehlgeschlagen bei {}: {}", request.getRequestURI(), ex.getMessage());
        APIResponseVoid body = ApiResponseFactory.errorSingle(
                422,
                "Domain-Validierung fehlgeschlagen",
                ex.getMessage()
        );
        return ResponseEntity.status(422).body(body);
    }

    // 409 – Domainregel verletzt
    @ExceptionHandler(DomainRuleViolationException.class)
    public ResponseEntity<APIResponseVoid> handleDomainRule(DomainRuleViolationException ex,
                                                            HttpServletRequest request) {
        log.warn("⚠️ 409 Domainregel verletzt bei {}: {}", request.getRequestURI(), ex.getMessage());
        APIResponseVoid body = ApiResponseFactory.errorSingle(
                HttpStatus.CONFLICT.value(),
                "Domainregel verletzt",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 400 – Bean Validation (z. B. @Valid DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponseVoid> handleMethodArgInvalid(MethodArgumentNotValidException ex) {
        log.warn("⚠️ Bean Validation (RequestBody) fehlgeschlagen: {}", ex.getMessage());
        List<ApiErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        fe.getField() + ": " + fe.getDefaultMessage())
                )
                .toList();
        APIResponseVoid body = ApiResponseFactory.error(
                HttpStatus.BAD_REQUEST.value(),
                "Validierungsfehler",
                errors
        );
        return ResponseEntity.badRequest().body(body);
    }

    // 400 – ConstraintViolation (z. B. @Validated auf Parametern)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponseVoid> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("⚠️ ConstraintViolation: {}", ex.getMessage());
        APIResponseVoid body = ApiResponseFactory.errorSingle(
                HttpStatus.BAD_REQUEST.value(),
                "Ungültige Eingabe",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(body);
    }

    // 400 – BindException (Form/Query)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<APIResponseVoid> handleBindException(BindException ex) {
        log.warn("⚠️ BindException (Form/Query) fehlgeschlagen: {}", ex.getMessage());
        List<ApiErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        fe.getField() + ": " + fe.getDefaultMessage())
                )
                .toList();
        APIResponseVoid body = ApiResponseFactory.error(
                HttpStatus.BAD_REQUEST.value(),
                "Validierungsfehler",
                errors
        );
        return ResponseEntity.badRequest().body(body);
    }

    // 400 – fehlender Request-Parameter
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<APIResponseVoid> handleMissingParam(MissingServletRequestParameterException ex) {
        log.warn("🧩 Fehlender Request-Parameter: {}", ex.getMessage());
        APIResponseVoid body = ApiResponseFactory.errorSingle(
                HttpStatus.BAD_REQUEST.value(),
                "Fehlender Parameter",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(body);
    }

    // 405 – Methode nicht erlaubt
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<APIResponseVoid> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("🚫 Methode nicht unterstützt: {}", ex.getMessage());
        APIResponseVoid body = ApiResponseFactory.errorSingle(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Methode nicht erlaubt",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body);
    }

    // 415 – MediaType nicht unterstützt
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<APIResponseVoid> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        log.warn("📦 MediaType nicht unterstützt: {}", ex.getMessage());
        APIResponseVoid body = ApiResponseFactory.errorSingle(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "Content-Type nicht unterstützt",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(body);
    }

    // 500 – Catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponseVoid> handleInternalServerError(Exception ex,
                                                                     HttpServletRequest request) {
        log.error("💥 Unbehandelte Exception bei {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        APIResponseVoid body = ApiResponseFactory.errorSingle(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Interner Serverfehler",
                "Ein unerwarteter Fehler ist aufgetreten"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
```

---

## 4. Wie du das jetzt im Code nutzt

- **Domain‑Logik** wirft `DomainValidationException` oder `DomainRuleViolationException`
- **Controller** wirft keine technischen Fehler mehr bewusst
- **Infrastructure‑Fehler** kommen von Spring/JPA/Validation und werden hier abgefangen
- **Alle Fehler‑Responses** sind `APIResponseVoid` und passen zu deiner OpenAPI

Wenn du willst, können wir als nächsten Schritt ein konkretes Domain‑Szenario (z. B. „Archivieren nur bei abgeschlossenen Tasks“) durchspielen und die Exception dort sauber einbauen.