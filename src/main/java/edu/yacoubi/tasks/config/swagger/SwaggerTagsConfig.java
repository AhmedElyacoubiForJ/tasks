package edu.yacoubi.tasks.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        tags = {
                @Tag(
                        name = "A. TaskLists – CRUD",
                        description = "Basisoperationen für TaskLists: erstellen, abrufen, aktualisieren, löschen"
                ),
                @Tag(
                        name = "B. Tasks – CRUD innerhalb von TaskList",
                        description = "Endpunkte für Tasks innerhalb einer TaskList"
                ),
                @Tag(
                        name = "C. TaskLists – UseCases",
                        description = "Spezial- und UseCase-Endpunkte wie Archivierung oder Statusfilter"
                )
        }
)

public class SwaggerTagsConfig {
}
