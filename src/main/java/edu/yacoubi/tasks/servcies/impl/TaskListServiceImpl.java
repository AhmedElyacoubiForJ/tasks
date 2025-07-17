package edu.yacoubi.tasks.servcies.impl;

import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.repositories.TaskListRepository;
import edu.yacoubi.tasks.servcies.ITaskListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskListServiceImpl implements ITaskListService {

    private final TaskListRepository taskListRepository;

    @Override
    public List<TaskList> listTaskLists() {
        return taskListRepository.findAll();
    }

    @Override
    public Optional<TaskList> getTaskListById(UUID id) {
        return Optional.empty();
    }

    @Override
    public TaskList createTaskList(TaskList taskList) {
        return null;
    }

    @Override
    public void deleteTaskList(UUID id) {

    }
}
