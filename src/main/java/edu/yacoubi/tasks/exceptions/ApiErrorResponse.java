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
}
