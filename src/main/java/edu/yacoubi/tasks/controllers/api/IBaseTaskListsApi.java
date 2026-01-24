package edu.yacoubi.tasks.controllers.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Basis-Interface für alle TaskLists-APIs.
 * Enthält zentrale Metadaten wie den Hauptpfad und Swagger-Tag.
 */
@RequestMapping(path = "/api")
@Tag(
        name = "TaskLists",
        description = "REST-Endpunkte für TaskLists"
)
public interface IBaseTaskListsApi {
    // Keine Methoden – nur zentrale Annotationen
}

