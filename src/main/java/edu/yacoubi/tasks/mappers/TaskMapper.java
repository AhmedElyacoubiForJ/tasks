package edu.yacoubi.tasks.mappers;

import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * TaskMapper – DDD-konformer Mapper für Task-Entities.
 *
 * <p>Dieser Mapper konvertiert ausschließlich zwischen DTOs und Domain-Entities,
 * ohne Aggregatsgrenzen zu verletzen. Die TaskList wird bewusst NICHT gesetzt,
 * da dies Aufgabe des Orchestrators ist.</p>
 *
 * <p>Der Mapper nutzt den Lombok-Builder der Task-Entity, um sicherzustellen,
 * dass alle Invarianten und Domain-Regeln eingehalten werden.</p>
 *
 * <p>Update-DTOs werden nicht direkt auf Entities gemappt, da Änderungen an
 * Domain-Objekten ausschließlich über Domain-Methoden erfolgen dürfen.</p>
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = false)
)
public interface TaskMapper {

    /**
     * Mappt ein CreateTaskDto auf eine Task-Domain-Entity.
     * <p>
     * WICHTIG: Die TaskList wird NICHT gesetzt, da dies eine Aggregatsgrenze ist.
     * Der Orchestrator setzt die TaskList nachträglich.
     */
    Task toEntity(CreateTaskDto dto);

    /**
     * Mappt eine Task-Domain-Entity auf ein Detail-DTO.
     */
    @Mapping(target = "taskListId", source = "taskList.id")
    TaskDetailDto toDetailDto(Task task);

    /**
     * Mappt eine Task-Domain-Entity auf ein Summary-DTO.
     */
    TaskSummaryDto toSummaryDto(Task task);
}

// @Mapping(target = "dueDate", expression = "java(formatDate(task.getDueDate()))")
// über die Methode

//default String formatDate(LocalDateTime date) {
//    return date != null ? date.format(DateTimeFormatter.ISO_DATE) : null;

