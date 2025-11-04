package edu.yacoubi.tasks.services.app.impl;

import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.TaskListFilterDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.repositories.TaskListRepository;
import edu.yacoubi.tasks.services.app.ITaskListService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public TaskList getTaskListOrThrow(UUID id) {
        return taskListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TaskList not found"));
    }


    @Override
    public TaskList createTaskList(CreateTaskListDto taskListDto) {
        TaskList taskList = new TaskList();
        taskList.setTitle(taskListDto.title());
        taskList.setDescription(taskListDto.description());

        return taskListRepository.save(taskList);
    }

    @Override
    public boolean deleteTaskList(UUID id) {
        if (taskListRepository.existsById(id)) {
            taskListRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public TaskList updateTaskList(UUID id, UpdateTaskListDto dto) {
        TaskList taskList = getTaskListOrThrow(id);
        taskList.setTitle(dto.title());
        taskList.setDescription(dto.description());
        return taskListRepository.save(taskList);
    }
}
