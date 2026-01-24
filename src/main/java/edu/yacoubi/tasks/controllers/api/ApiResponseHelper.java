package edu.yacoubi.tasks.domain.dto.response;

import edu.yacoubi.tasks.controllers.api.APIResponse;
import edu.yacoubi.tasks.controllers.api.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Globaler Helper für APIResponse-Erstellung.
 *
 * <p>Dieser Helper stellt statische Methoden bereit, um konsistente APIResponses
 * für DTOs, Listen oder leere Antworten zu erzeugen.</p>
 *
 * <p>Beispiel:</p>
 * <pre>
 *     final APIResponse<TaskListDto> response =
 *         ApiResponseHelper.buildResponse("TaskList erstellt",
 *             ResponseStatus.SUCCESS, HttpStatus.CREATED, dto);
 * </pre>
 *
 * @author Ahmed
 */
@Slf4j
public class ApiResponseHelper {
    private ApiResponseHelper() {
        // Utility-Klasse → kein Konstruktor
    }

    /**
     * Baut eine APIResponse für beliebige Daten (DTO, Liste, Page, etc.).
     *
     * @param <T>        Typ der Daten
     * @param msg        Nachricht
     * @param status     ResponseStatus (Enum)
     * @param statusCode HTTP-Status
     * @param data       Datenobjekt (kann auch null sein)
     * @return APIResponse<T>
     */
    public static <T> APIResponse<T> buildResponse(
            final String msg,
            final ResponseStatus status,
            final HttpStatus statusCode,
            final T data) {

        if (log.isInfoEnabled()) {
            log.info("::buildResponse gestartet mit msg={}, status={}, code={}",
                    msg, status, statusCode);
        }

        final APIResponse<T> response = APIResponse.<T>builder()
                .message(msg == null ? "No message provided" : msg)
                .status(status) // geändert
                .statusCode(statusCode.value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();

        if (log.isInfoEnabled()) {
            log.info("::buildResponse erfolgreich abgeschlossen");
        }

        return response;
    }

    /**
     * Baut eine APIResponse ohne Daten (z. B. für DELETE).
     *
     * @param msg        Nachricht
     * @param status     ResponseStatus
     * @param statusCode HTTP-Status
     * @return APIResponse<Void>
     */
    public static APIResponse<Void> buildVoidResponse(
            final String msg,
            final ResponseStatus status,
            final HttpStatus statusCode) {

        return buildResponse(msg, status, statusCode, null);
    }
}
