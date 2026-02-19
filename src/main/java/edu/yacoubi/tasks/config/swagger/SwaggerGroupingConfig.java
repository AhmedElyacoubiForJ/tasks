package edu.yacoubi.tasks.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupingConfig {

    @Bean
    public GroupedOpenApi taskListsCrudGroup() {
        return GroupedOpenApi.builder()
                .group("TaskLists – CRUD")
                .pathsToMatch(
                        "/api/tasklists",
                        "/api/tasklists/",
                        "/api/tasklists/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi taskListsScenarioGroup() {
        return GroupedOpenApi.builder()
                .group("TaskLists – Szenarien")
                .pathsToMatch(
                        "/api/tasklists/active",
                        "/api/tasklists/archived",
                        "/api/tasklists/**/archive"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi tasksInTaskListsGroup() {
        return GroupedOpenApi.builder()
                .group("Tasks in TaskLists")
                .pathsToMatch(
                        "/api/tasklists/**/tasks",
                        "/api/tasklists/**/tasks/**"
                )
                .build();
    }
}
