package edu.yacoubi.tasks.exceptions;


import edu.yacoubi.tasks.controllers.api.APIResponse;
import edu.yacoubi.tasks.controllers.api.ResponseStatus;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Tag(name = "Errors", description = "Globale Fehlerbehandlung")
@Hidden
@Slf4j
public class RestExceptionHandler {

    /**
     * Wird ausgelöst, wenn eine Entity (z. B. Task, TaskList) nicht gefunden wird.
     * Typischer Fall: taskRepository.findById(...) liefert Optional.empty().
     * → Antwort: 404 Not Found mit einer klaren Fehlermeldung.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleNotFound(EntityNotFoundException ex,
                                                            HttpServletRequest request) {
        log.warn("⚠️ [RestExceptionHandler] EntityNotFoundException abgefangen für URI {}: {}",
                request.getRequestURI(), ex.getMessage());

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        APIResponse<Void> response = APIResponse.<Void>builder()
                .status(ResponseStatus.ERROR)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message("Ressource nicht gefunden")
                .errors(List.of(error))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    /**
     * Wird ausgelöst, wenn Bean Validation fehlschlägt (z. B. @NotNull, @Size).
     * Typischer Fall: ConstraintViolationException bei ungültigen Eingaben.
     * → Antwort: 400 Bad Request mit Validierungsdetails.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<Object>> handleValidationError(ConstraintViolationException ex) {
        log.warn("⚠️ Validierungsfehler: {}", ex.getMessage());
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Ungültige Eingabe: " + ex.getMessage()
        );

        APIResponse<Object> response = APIResponse.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validierungsfehler")
                .errors(List.of(error))
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Wird ausgelöst, wenn eine HTTP-Methode nicht unterstützt wird.
     * Typischer Fall: POST auf einem Endpoint, der nur GET erlaubt.
     * → Antwort: 405 Method Not Allowed.
     */
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<APIResponse<Object>> handleMethodNotSupported(Exception ex) {
        log.warn("🚫 Methode nicht unterstützt: {}", ex.getMessage());
        ApiErrorResponse error = new ApiErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "HTTP-Methode nicht erlaubt");
        APIResponse<Object> response = APIResponse.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message("Methode nicht erlaubt")
                .errors(List.of(error))
                .build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * Wird ausgelöst, wenn ein Content-Type nicht unterstützt wird.
     * Typischer Fall: Client sendet XML, Endpoint erwartet JSON.
     * → Antwort: 415 Unsupported Media Type.
     */
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<APIResponse<Object>> handleMediaType(Exception ex) {
        log.warn("📦 MediaType nicht unterstützt: {}", ex.getMessage());
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "Content-Type nicht unterstützt"
        );
        APIResponse<Object> response = APIResponse.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .message("Medientyp nicht unterstützt")
                .errors(List.of(error))
                .build();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    /**
     * Wird ausgelöst, wenn ein Request-Parameter fehlt.
     * Typischer Fall: Endpoint erwartet ?id=..., aber Parameter fehlt.
     * → Antwort: 400 Bad Request mit Hinweis auf fehlenden Parameter.
     */
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<APIResponse<Object>> handleMissingParam(Exception ex) {
        log.warn("🧩 Fehlender Request-Parameter: {}", ex.getMessage());
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Fehlender Parameter: " + ex.getMessage()
        );
        APIResponse<Object> response = APIResponse.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Fehlender Parameter")
                .errors(List.of(error))
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Wird ausgelöst, wenn Bean Validation bei einem RequestBody fehlschlägt.
     * Typischer Fall: @Valid DTO mit fehlerhaften Feldern.
     * → Antwort: 400 Bad Request mit Feld-Fehlern.
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Object>> handleMethodArgInvalid(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        log.warn("⚠️ Bean Validation (RequestBody) fehlgeschlagen: {}", ex.getMessage());
        List<ApiErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), fe.getField() + ": " + fe.getDefaultMessage()))
                .toList();
        APIResponse<Object> response = APIResponse.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validierungsfehler")
                .errors(errors)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Wird ausgelöst, wenn Binding von Form- oder Query-Parametern fehlschlägt.
     * Typischer Fall: falscher Typ (z. B. String statt Zahl).
     * → Antwort: 400 Bad Request mit Feld-Fehlern.
     */
    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ResponseEntity<APIResponse<Object>> handleBindException(org.springframework.validation.BindException ex) {
        log.warn("⚠️ BindException (Form/Query) fehlgeschlagen: {}", ex.getMessage());
        List<ApiErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), fe.getField() + ": " + fe.getDefaultMessage()))
                .toList();
        APIResponse<Object> response = APIResponse.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validierungsfehler")
                .errors(errors)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Catch-all für alle nicht behandelten Exceptions.
     * Typischer Fall: NullPointerException, IllegalStateException, etc.
     * → Antwort: 500 Internal Server Error mit generischer Fehlermeldung.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Object>> handleInternalServerError(Exception ex) {
        log.error("Unbehandelte Exception im Controller: {}", ex.getMessage(), ex);
        ApiErrorResponse error = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ein unerwarteter Fehler ist aufgetreten");
        APIResponse<Object> response = APIResponse.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Interner Serverfehler")
                .errors(List.of(error))
                .build();
        log.info("Fehlerantwort wird zurückgegeben mit Status {}", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}