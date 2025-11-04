package edu.yacoubi.tasks.exceptions;

import edu.yacoubi.tasks.controllers.api.APIResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Tag(name = "Errors", description = "Globale Fehlerbehandlung")
@Hidden
@Slf4j
public class RestExceptionHandler {

    @Operation(hidden = true)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Object>> handleInternalServerError(Exception ex) {
        log.error("Unbehandelte Exception im Controller: {}", ex.getMessage(), ex);

        ApiErrorResponse error = new ApiErrorResponse(500, "Ein unerwarteter Fehler ist aufgetreten");
        log.debug("API Error Response: StatusCode={}, Message={}", 500, error.getMessage());

        APIResponse<Object> response = APIResponse.builder()
                .status("error")
                .statusCode(500)
                .message("Interner Serverfehler")
                .errors(List.of(error))
                .build();

        log.info("Fehlerantwort wird zurückgegeben mit Status 500");
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<Object>> handleValidationError(ConstraintViolationException ex) {
        log.warn("⚠️ Validierungsfehler: {}", ex.getMessage());

        ApiErrorResponse error = new ApiErrorResponse(400, "Ungültige Eingabe: " + ex.getMessage());

        APIResponse<Object> response = APIResponse.builder()
                .status("error")
                .statusCode(400)
                .message("Validierungsfehler")
                .errors(List.of(error))
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}
