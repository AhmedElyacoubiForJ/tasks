package edu.yacoubi.tasks.servcies.impl;

import edu.yacoubi.tasks.domain.dto.request.tasklist.TaskListFilterDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.repositories.TaskListRepository;
import edu.yacoubi.tasks.servcies.ITaskListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskListServiceImpl implements ITaskListService {

    private final TaskListRepository taskListRepository;
    private final TaskListMapper mapper;

    @Override
    public List<TaskList> listTaskLists() {
        return taskListRepository.findAll();
    }

    @Override
    public Page<TaskListDto> getFilteredTaskLists(TaskListFilterDto params) {
        Pageable pageable = PageRequest.of(params.page(), params.size());
        if (params.query() != null && !params.query().isBlank()) {
            return taskListRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    params.query(), params.query(), pageable
            ).map(mapper::toTaskListDto);
        }

        return taskListRepository.findAll(pageable).map(mapper::toTaskListDto);
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
