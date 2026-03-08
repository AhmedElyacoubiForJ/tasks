package edu.yacoubi.tasks.controllers.api.v1.contract.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status der API-Antwort")
public enum ResponseStatus {
    @Schema(description = "Antwort erfolgreich verarbeitet")
    SUCCESS,

    @Schema(description = "Antwort enthält Fehler")
    ERROR
}
