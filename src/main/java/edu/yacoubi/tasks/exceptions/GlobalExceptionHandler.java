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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Hidden
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIResponseVoid> handleNotFound(EntityNotFoundException ex,
                                                          HttpServletRequest request) {

        APIResponseVoid body = APIResponseVoid.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(404)
                .message("Ressource nicht gefunden")
                .errors(List.of(new ApiErrorResponse(404, ex.getMessage())))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(404).body(body);
    }

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<APIResponseVoid> handleDomainValidation(DomainValidationException ex) {

        APIResponseVoid body = APIResponseVoid.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(422)
                .message("Domain-Validierung fehlgeschlagen")
                .errors(List.of(new ApiErrorResponse(422, ex.getMessage())))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(422).body(body);
    }

    @ExceptionHandler(DomainRuleViolationException.class)
    public ResponseEntity<APIResponseVoid> handleDomainRule(DomainRuleViolationException ex) {

        APIResponseVoid body = APIResponseVoid.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(409)
                .message("Domainregel verletzt")
                .errors(List.of(new ApiErrorResponse(409, ex.getMessage())))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(409).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponseVoid> handleMethodArgInvalid(MethodArgumentNotValidException ex) {

        List<ApiErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiErrorResponse(400, fe.getField() + ": " + fe.getDefaultMessage()))
                .toList();

        APIResponseVoid body = APIResponseVoid.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(400)
                .message("Validierungsfehler")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponseVoid> handleInternalServerError(Exception ex) {

        APIResponseVoid body = APIResponseVoid.builder()
                .status(ResponseStatus.ERROR)
                .statusCode(500)
                .message("Interner Serverfehler")
                .errors(List.of(new ApiErrorResponse(500, "Ein unerwarteter Fehler ist aufgetreten")))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(500).body(body);
    }
}
