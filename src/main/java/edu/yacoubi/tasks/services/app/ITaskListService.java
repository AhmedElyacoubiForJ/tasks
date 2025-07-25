package edu.yacoubi.tasks.services.app;

import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.TaskListFilterDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskListService {
    List<TaskList> listTaskLists();

    Page<TaskListDto> getFilteredTaskLists(TaskListFilterDto params);

    Optional<TaskList> getTaskListById(UUID id);

    TaskList createTaskList(CreateTaskListDto taskListDto);

    boolean deleteTaskList(UUID id);
}
