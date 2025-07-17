package edu.yacoubi.tasks.mappers;

import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.UpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(CreateTaskDto dto);

    Task toEntity(UpdateTaskDto dto);

    @Mapping(target = "taskListId", source = "taskList.id")
    TaskDetailDto toDetailDto(Task task);

    // @Mapping(target = "dueDate", expression = "java(formatDate(task.getDueDate()))")
    TaskSummaryDto toSummaryDto(Task task);

    default String formatDate(LocalDateTime date) {
        return date != null ? date.format(DateTimeFormatter.ISO_DATE) : null;
    }
}
