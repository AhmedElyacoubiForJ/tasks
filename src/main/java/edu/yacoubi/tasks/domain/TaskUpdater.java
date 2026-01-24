package edu.yacoubi.tasks.domain;

import edu.yacoubi.tasks.domain.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.UpdateTaskDto;
import edu.yacoubi.tasks.domain.entities.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskUpdater {

    public void applyFullUpdate(Task task, UpdateTaskDto dto) {
        task.changeTitle(dto.title());
        task.changeDescription(dto.description());
        task.changeDueDate(dto.dueDate());
        task.changePriority(dto.priority());
        // Status immer zuletzt
        task.changeStatus(dto.status());
    }

    public void applyPatch(Task task, PatchTaskDto dto) {
        if (dto.title() != null) {
            task.changeTitle(dto.title());
        }
        if (dto.description() != null) {
            task.changeDescription(dto.description());
        }
        if (dto.dueDate() != null) {
            task.changeDueDate(dto.dueDate());
        }
        if (dto.priority() != null) {
            task.changePriority(dto.priority());
        }
        if (dto.status() != null) {
            task.changeStatus(dto.status());
        }
    }
}

