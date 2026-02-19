package edu.yacoubi.tasks.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        tags = {
                @Tag(
                        name = "TaskLists – CRUD",
                        description = "Basisoperationen für TaskLists: erstellen, abrufen, aktualisieren, löschen"
                ),
                @Tag(
                        name = "TaskLists – Szenarien",
                        description = "Spezial- und Szenario-Endpunkte wie Archivierung oder Statusfilter"
                ),
                @Tag(
                        name = "Tasks in TaskLists",
                        description = "Endpunkte für Tasks innerhalb einer TaskList"
                )
        }
)
public class SwaggerTagsConfig {
}
