package edu.yacoubi.tasks.servcies;

import edu.yacoubi.tasks.domain.entities.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskListService {
    List<TaskList> listTaskLists();

    Optional<TaskList> getTaskListById(UUID id);

    TaskList createTaskList(TaskList taskList);

    void deleteTaskList(UUID id);
}
