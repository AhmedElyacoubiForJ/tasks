package edu.yacoubi.tasks.mappers;

import edu.yacoubi.tasks.domain.dto.response.task.TaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TaskListMapper {

    @Mapping(target = "count", expression = "java(taskCount(taskList.getTasks()))")
    @Mapping(target = "progress", expression = "java(calculateProgress(taskList.getTasks()))")
    @Mapping(target = "tasks", expression = "java(mapToDetailList(taskList.getTasks()))")
    TaskListWithTaskDetailDto toWithTaskDetailDto(TaskList taskList);

    @Mapping(target = "count", expression = "java(taskCount(taskList.getTasks()))")
    @Mapping(target = "progress", expression = "java(calculateProgress(taskList.getTasks()))")
    @Mapping(target = "tasks", expression = "java(mapToSummaryList(taskList.getTasks()))")
    TaskListWithTaskSummaryDto toWithTaskSummaryDto(TaskList taskList);


    default Integer taskCount(List<Task> tasks) {
        return tasks != null ? tasks.size() : 0;
    }

    default Double calculateProgress(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) return 0.0;
        long closed = tasks.stream().filter(t -> t.getStatus() == TaskStatus.CLOSED).count();
        return (double) closed / tasks.size() * 100;
    }

    default List<TaskDetailDto> mapToDetailList(List<Task> tasks) {
        return tasks.stream().map(this::mapToDetail).toList();
    }

    default List<TaskSummaryDto> mapToSummaryList(List<Task> tasks) {
        return tasks.stream().map(this::mapToSummary).toList();
    }

    TaskDetailDto mapToDetail(Task task);
    TaskSummaryDto mapToSummary(Task task);
}
