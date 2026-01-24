package edu.yacoubi.tasks.mappers;

import edu.yacoubi.tasks.domain.dto.response.task.TaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * TaskListMapper – DDD-konformer Mapper für TaskList-Aggregate.
 *
 * <p>Dieser Mapper konvertiert TaskList-Domain-Entities in verschiedene DTO-Varianten,
 * ohne Aggregatsgrenzen zu verletzen. Tasks werden nur gemappt, wenn sie bereits
 * vom Orchestrator oder Service geladen wurden. Der Mapper lädt niemals selbst
 * Daten nach und enthält keine Business-Logik.</p>
 *
 * <p>Progress- und Count-Berechnungen sind rein DTO-bezogen und verletzen keine
 * Domain-Regeln. Der Mapper nutzt den Lombok-Builder der Domain-Entities und
 * bleibt vollständig kompatibel mit DDD-Prinzipien.</p>
 */

@Mapper(
        componentModel = "spring",
        uses = TaskMapper.class,
        builder = @Builder(disableBuilder = false)
)
public interface TaskListMapper {

    // ✅ TaskList → TaskListDto (ohne Tasks)
    @Mapping(target = "count", expression = "java(taskCount(taskList.getTasks()))")
    @Mapping(target = "progress", expression = "java(calculateProgress(taskList.getTasks()))")
    TaskListDto toTaskListDto(TaskList taskList);

    // ✅ TaskList → TaskListWithTaskSummaryDto
    @Mapping(target = "count", expression = "java(taskCount(taskList.getTasks()))")
    @Mapping(target = "progress", expression = "java(calculateProgress(taskList.getTasks()))")
    @Mapping(target = "tasks", expression = "java(mapToSummaryList(taskList.getTasks()))")
    TaskListWithTaskSummaryDto toWithTaskSummaryDto(TaskList taskList);

    // ✅ TaskList → TaskListWithTaskDetailDto
    @Mapping(target = "count", expression = "java(taskCount(taskList.getTasks()))")
    @Mapping(target = "progress", expression = "java(calculateProgress(taskList.getTasks()))")
    @Mapping(target = "tasks", expression = "java(mapToDetailList(taskList.getTasks()))")
    TaskListWithTaskDetailDto toWithTaskDetailDto(TaskList taskList);

    // ✅ Hilfsmethoden (rein technisch)
    default Integer taskCount(List<Task> tasks) {
        return tasks != null ? tasks.size() : 0;
    }

    default Double calculateProgress(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) return 0.0;
        long closed = tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        return (double) closed / tasks.size() * 100;
    }

    default List<TaskDetailDto> mapToDetailList(List<Task> tasks) {
        return tasks == null ? List.of() : tasks.stream().map(this::mapToDetail).toList();
    }

    default List<TaskSummaryDto> mapToSummaryList(List<Task> tasks) {
        return tasks == null ? List.of() : tasks.stream().map(this::mapToSummary).toList();
    }

    TaskDetailDto mapToDetail(Task task);
    TaskSummaryDto mapToSummary(Task task);
}
