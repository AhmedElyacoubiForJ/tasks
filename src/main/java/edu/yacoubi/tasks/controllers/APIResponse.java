package edu.yacoubi.tasks.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.yacoubi.tasks.exceptions.ApiErrorResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Schema(description = "Standardisierte API-Antwortstruktur mit Erfolgs-/Fehlermeldung und Payload")
public class APIResponse<T> {

    @Schema(description = "Status der Antwort", example = "success")
    private String status;

    @Schema(description = "HTTP Statuscode", example = "200")
    private int statusCode;

    @Schema(description = "Zusätzliche Nachricht zur Antwort", example = "Liste erfolgreich zurückgegeben")
    private String message;

    @Schema(description = "Fehlerdetails bei fehlgeschlagener Anfrage")
    private List<ApiErrorResponse> errors;

    @Schema(description = "Antwortdaten bei erfolgreicher Anfrage")
    private T data;
}
