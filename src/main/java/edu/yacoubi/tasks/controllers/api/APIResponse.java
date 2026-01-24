package edu.yacoubi.tasks.controllers.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.yacoubi.tasks.exceptions.ApiErrorResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Schema(description = "Standardisierte API-Antwortstruktur mit Erfolgs-/Fehlermeldung und Payload")
public class APIResponse<T> {

    @Schema(description = "Status der Antwort", example = "success")
    private ResponseStatus status;

    @Schema(description = "HTTP Statuscode", example = "200")
    private int statusCode;

    @Schema(description = "Zusätzliche Nachricht zur Antwort", example = "Liste erfolgreich zurückgegeben")
    private String message;

    @Schema(description = "Fehlerdetails bei fehlgeschlagener Anfrage")
    private List<ApiErrorResponse> errors;

    @Schema(description = "Antwortdaten bei erfolgreicher Anfrage")
    private T data;

    @Schema(description = "Zeitstempel der Antwort")
    private LocalDateTime timestamp;

    // 🔧 Verbesserungsidee: Trace-ID oder Request-ID für verteilte Systeme,
    // damit man Fehler leichter über mehrere Services hinweg verfolgen kann.
    // private String traceId;
}
