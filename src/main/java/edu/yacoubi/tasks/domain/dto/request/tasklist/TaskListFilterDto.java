package edu.yacoubi.tasks.domain.dto.request.tasklist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "Filter- und Pagination-Parameter für Task-Listen")
public record TaskListFilterDto(
        @Schema(description = "Suchbegriff im Titel oder Beschreibung", example = "Demo")
        String query,

        @Schema(description = "Seitenzahl (beginnend bei 0)", example = "0")
        @Min(0)
        int page,

        @Schema(description = "Seitengröße", example = "10")
        @Min(1)
        int size
) {
}
