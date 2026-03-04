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
                        name = "Tasks – CRUD in TaskLists",
                        description = "Endpunkte für Tasks innerhalb einer TaskList"
                ),
                @Tag(
                        name = "TaskLists – UseCases",
                        description = "Spezial- und UseCase-Endpunkte wie Archivierung oder Statusfilter"
                )
        }
)
public class SwaggerTagsConfig {
}
