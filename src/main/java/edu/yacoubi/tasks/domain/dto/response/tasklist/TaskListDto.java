package edu.yacoubi.tasks.domain.dto.response.tasklist;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Metadaten einer Task-Liste ohne Aufgaben")
public record TaskListDto(

        @Schema(description = "ID der Task-Liste", example = "640eb884-723b-408a-869d-59b38ca44b84")
        UUID id,

        @Schema(description = "Titel der Task-Liste", example = "🏁 Demo-Liste")
        String title,

        @Schema(description = "Beschreibung der Task-Liste", example = "Beispielhafte Aufgaben für Swagger & Seeding")
        String description,

        @Schema(description = "Erstellungszeitpunkt der Liste", example = "2025-07-23T13:32:16.885754")
        LocalDateTime created,

        @Schema(description = "Zeitpunkt der letzten Aktualisierung", example = "2025-07-24T09:18:44.310112")
        LocalDateTime updated,

        @Schema(description = "Anzahl der enthaltenen Aufgaben", example = "2")
        Integer count,

        @Schema(description = "Fortschritt in Prozent", example = "50.0")
        Double progress
) {
    public Integer roundedProgress() {
        return progress == null ? 0 : (int) Math.round(progress);
    }
}
