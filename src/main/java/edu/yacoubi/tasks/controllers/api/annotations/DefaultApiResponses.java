package edu.yacoubi.tasks.controllers.api.annotations;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Zentrale Sammlung der Standard-API-Fehlerantworten.
 *
 * VORHER:
 *   - Jeder Endpoint musste 400/404/500 selbst deklarieren.
 *   - Viel Copy-Paste, schwer wartbar.
 *
 * NACHHER:
 *   - Eine einzige Annotation für alle Standardfehler.
 *   - Kann auf Interfaces ODER einzelne Methoden gesetzt werden.
 *   - Reduziert Redundanz und hält die API-Doku konsistent.
 *
 * Warum eine Annotation?
 *   - Weil wir sie direkt auf API-Interfaces/Methoden anwenden wollen.
 *   - Sie verhält sich wie ein "Swagger-Response-MixIn".
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Ungültige Eingabe"),
        @ApiResponse(responseCode = "404", description = "Nicht gefunden"),
        @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
})
public @interface DefaultApiResponses {
}
