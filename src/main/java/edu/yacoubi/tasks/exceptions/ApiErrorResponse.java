package edu.yacoubi.tasks.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Fehlermeldung als API-Antwort")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiErrorResponse {

    @Schema(description = "HTTP Statuscode", example = "500")
    private int status;

    @Schema(description = "Fehlermeldung", example = "Ein unerwarteter Fehler ist aufgetreten")
    private String message;

    // 🔧 Verbesserungsidee: Zeitstempel hinzufügen, damit Fehler im Log und Response korrelierbar sind
    // private LocalDateTime timestamp;

    // 🔧 Verbesserungsidee: Ein interner Fehlercode (z. B. TASK_NOT_FOUND),
    // damit Clients gezielt reagieren können statt nur auf die Message.
    // private String errorCode;

    // 🔧 Verbesserungsidee: Den Request-Pfad zurückgeben, um schneller zu sehen,
    // bei welchem Endpoint der Fehler auftrat.
    // private String path;
}
