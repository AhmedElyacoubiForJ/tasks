package edu.yacoubi.tasks.exceptions;

import edu.yacoubi.tasks.controllers.api.ApiErrorResponse;
import edu.yacoubi.tasks.controllers.api.ResponseStatus;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseVoid;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * GlobalExceptionHandler ---------------------- Zentrale Fehlerbehandlung für alle REST-Controller.
 *
 * <p>Verantwortlichkeiten: - Konsistente Fehlerstruktur für alle API-Responses - Saubere Trennung
 * zwischen: * 404 Not Found * 409 Domainregel verletzt * 422 Domain-Validierung * 400
 * Validierungsfehler (Bean Validation) * 400 JSON-Parsing-Fehler * 400 Parameterfehler (UUID etc.)
 * * 500 interne Fehler
 *
 * <p>Jede Exception wird in ein APIResponseVoid-Objekt übersetzt.
 */
@RestControllerAdvice
@Hidden
@Slf4j
public class GlobalExceptionHandler {

  // ------------------------------------------------------------
  // 404 – Ressource nicht gefunden
  // ------------------------------------------------------------
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<APIResponseVoid> handleNotFound(
      EntityNotFoundException ex, HttpServletRequest request) {

    APIResponseVoid body =
        APIResponseVoid.builder()
            .status(ResponseStatus.ERROR)
            .statusCode(404)
            .message("Ressource nicht gefunden")
            .errors(List.of(new ApiErrorResponse(404, ex.getMessage())))
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.status(404).body(body);
  }

  // ------------------------------------------------------------
  // 422 – Domain-Validierung (fachliche Regeln)
  // ------------------------------------------------------------
  @ExceptionHandler(DomainValidationException.class)
  public ResponseEntity<APIResponseVoid> handleDomainValidation(DomainValidationException ex) {

    APIResponseVoid body =
        APIResponseVoid.builder()
            .status(ResponseStatus.ERROR)
            .statusCode(422)
            .message("Domain-Validierung fehlgeschlagen")
            .errors(List.of(new ApiErrorResponse(422, ex.getMessage())))
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.status(422).body(body);
  }

  // ------------------------------------------------------------
  // 409 – Domainregel verletzt (z. B. Task gehört nicht zur TaskList)
  // ------------------------------------------------------------
  @ExceptionHandler(DomainRuleViolationException.class)
  public ResponseEntity<APIResponseVoid> handleDomainRule(DomainRuleViolationException ex) {

    APIResponseVoid body =
        APIResponseVoid.builder()
            .status(ResponseStatus.ERROR)
            .statusCode(409)
            .message("Domainregel verletzt")
            .errors(List.of(new ApiErrorResponse(409, ex.getMessage())))
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.status(409).body(body);
  }

  // ------------------------------------------------------------
  // 400 – Bean Validation Fehler (@Valid)
  // ------------------------------------------------------------
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<APIResponseVoid> handleMethodArgInvalid(
      MethodArgumentNotValidException ex) {

    List<ApiErrorResponse> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> new ApiErrorResponse(400, fe.getField() + ": " + fe.getDefaultMessage()))
            .toList();

    APIResponseVoid body =
        APIResponseVoid.builder()
            .status(ResponseStatus.ERROR)
            .statusCode(400)
            .message("Validierungsfehler")
            .errors(errors)
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.badRequest().body(body);
  }

  // ------------------------------------------------------------
  // 400 – Ungültiger Parameter (z. B. UUID falsch)
  // ------------------------------------------------------------
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<APIResponseVoid> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex) {

    APIResponseVoid body =
        APIResponseVoid.builder()
            .status(ResponseStatus.ERROR)
            .statusCode(400)
            .message("Ungültiger Parameter")
            .errors(List.of(new ApiErrorResponse(400, ex.getMessage())))
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.badRequest().body(body);
  }

  // ------------------------------------------------------------
  // 400 – JSON-Parsing-Fehler (ungültiges JSON oder ungültiger Enum)
  // ------------------------------------------------------------
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<APIResponseVoid> handleJsonErrors(HttpMessageNotReadableException ex) {

    Throwable cause = ex.getCause();

    // Fall A: JSON komplett ungültig
    if (cause instanceof com.fasterxml.jackson.core.JsonParseException) {
      APIResponseVoid body =
          APIResponseVoid.builder()
              .status(ResponseStatus.ERROR)
              .statusCode(400)
              .message("Ungültiges JSON")
              .errors(List.of(new ApiErrorResponse(400, "JSON konnte nicht gelesen werden")))
              .timestamp(LocalDateTime.now())
              .build();

      return ResponseEntity.badRequest().body(body);
    }

    // Fall B: Enum oder Datentyp ungültig
    if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException invalid) {

      String fieldName = invalid.getPath().get(0).getFieldName();

      APIResponseVoid body =
          APIResponseVoid.builder()
              .status(ResponseStatus.ERROR)
              .statusCode(400)
              .message("Validierungsfehler")
              .errors(List.of(new ApiErrorResponse(400, fieldName + ": Ungültiger Wert")))
              .timestamp(LocalDateTime.now())
              .build();

      return ResponseEntity.badRequest().body(body);
    }

    // Fallback
    APIResponseVoid body =
        APIResponseVoid.builder()
            .status(ResponseStatus.ERROR)
            .statusCode(400)
            .message("Ungültige Anfrage")
            .errors(List.of(new ApiErrorResponse(400, "Anfrage konnte nicht verarbeitet werden")))
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.badRequest().body(body);
  }

  // ------------------------------------------------------------
  // 500 – Interner Serverfehler (Fallback)
  // ------------------------------------------------------------
  @ExceptionHandler(Exception.class)
  public ResponseEntity<APIResponseVoid> handleInternalServerError(Exception ex) {

    log.error("Unerwarteter Fehler", ex);

    APIResponseVoid body =
        APIResponseVoid.builder()
            .status(ResponseStatus.ERROR)
            .statusCode(500)
            .message("Interner Serverfehler")
            .errors(List.of(new ApiErrorResponse(500, "Ein unerwarteter Fehler ist aufgetreten")))
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.status(500).body(body);
  }

  // ------------------------------------------------------------
  // 405 – HTTP-Methode nicht erlaubt
  // ------------------------------------------------------------
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<APIResponseVoid> handleMethodNotSupported(
          HttpRequestMethodNotSupportedException ex) {

    APIResponseVoid body =
            APIResponseVoid.builder()
                    .status(ResponseStatus.ERROR)
                    .statusCode(405)
                    .message("HTTP-Methode nicht erlaubt")
                    .errors(List.of(
                            new ApiErrorResponse(
                                    405,
                                    "Erlaubte Methoden: " + String.join(", ", ex.getSupportedMethods())
                            )
                    ))
                    .timestamp(LocalDateTime.now())
                    .build();

    return ResponseEntity.status(405).body(body);
  }

  // ------------------------------------------------------------
  // 404 – Technischer Resource-Not-Found (Endpoint existiert nicht)
  // ------------------------------------------------------------
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<APIResponseVoid> handleNoResourceFound(NoResourceFoundException ex) {

    APIResponseVoid body =
            APIResponseVoid.builder()
                    .status(ResponseStatus.ERROR)
                    .statusCode(404)
                    .message("Endpoint nicht gefunden")
                    .errors(List.of(
                            new ApiErrorResponse(404, ex.getMessage())
                    ))
                    .timestamp(LocalDateTime.now())
                    .build();

    return ResponseEntity.status(404).body(body);
  }
}
