package edu.yacoubi.tasks.mappers;

import edu.yacoubi.tasks.domain.dto.response.task.TaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTasksDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TaskListMapper {

    @Mapping(target = "count", expression = "java(taskCount(taskList.getTasks()))")
    @Mapping(target = "progress", expression = "java(calculateProgress(taskList.getTasks()))")
    @Mapping(target = "tasks", expression = "java(toTaskDetailDtoList(taskList.getTasks()))")
    TaskListWithTasksDto toDto(TaskList taskList);

    // 👇 Berechnet die Anzahl aller Tasks
    @Named("taskCount")
    default Integer taskCount(List<Task> tasks) {
        return tasks != null ? tasks.size() : 0;
    }

    // 👇 Berechnet den Fortschritt (completed / total)
    @Named("calculateProgress")
    default Double calculateProgress(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) return 0.0;
        long closed = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.CLOSED)
                .count();
        return (double) closed / tasks.size() * 100;
    }


    // 👇 Mapped alle Tasks zur Detail-DTO
    @Named("toTaskDetailDtoList")
    default List<TaskDetailDto> toTaskDetailDtoList(List<Task> tasks) {
        return tasks.stream().map(this::mapToDetail).toList();
    }

    // 👇 Einzel-Mapping für Task → TaskDetailDto
    TaskDetailDto mapToDetail(Task task);
}
