package edu.yacoubi.tasks.domain.dto.request.tasklist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "Parameter zur Paginierung einer Anfrage")
public record PaginationRequestDto(

        @Schema(description = "Seitenzahl (beginnend bei 0)", example = "0")
        @Min(0)
        int page,

        @Schema(description = "Größe pro Seite", example = "10")
        @Min(1)
        int size
) {}

