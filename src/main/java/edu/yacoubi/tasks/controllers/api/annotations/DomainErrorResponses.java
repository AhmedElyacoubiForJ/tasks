package edu.yacoubi.tasks.controllers.api.annotations;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * DOMAIN ERROR RESPONSES
 * ----------------------
 * Ergänzt @DefaultApiResponses um fachliche Fehlercodes.
 *
 * BEFORE:
 *   - 409 und 422 mussten in jeder Methode manuell definiert werden.
 *   - Das führte zu Redundanz und inkonsistenten Beschreibungen.
 *
 * AFTER:
 *   - Diese Annotation bündelt alle Domain-Fehler zentral.
 *   - Sie wird nur dort eingesetzt, wo Domainregeln verletzt werden können.
 *
 * WHY:
 *   - 409 = Konflikt (z. B. Archivierung nicht möglich, Statuswechsel verboten)
 *   - 422 = Domain-Validierung fehlgeschlagen (z. B. ungültige PATCH-Operation)
 *
 * NOTE:
 *   - Ergänzt @DefaultApiResponses (400/404/500).
 *   - Überschreibt nichts, sondern fügt NUR Domain-Fehler hinzu.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(
                responseCode = "409",
                description = "Konflikt – Domainregel verletzt"
        ),
        @ApiResponse(
                responseCode = "422",
                description = "Domain-Validierung fehlgeschlagen"
        )
})
public @interface DomainErrorResponses {
}
