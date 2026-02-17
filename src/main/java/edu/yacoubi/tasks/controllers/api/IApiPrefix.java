package edu.yacoubi.tasks.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Basis-Interface für alle TaskLists-APIs.
 * Enthält zentrale Metadaten wie den Hauptpfad und Swagger-Tag.
 */
@RequestMapping(path = "/api")
public interface IApiPrefix {
    // Keine Methoden – nur zentrale Annotationen
}

