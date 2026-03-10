package edu.yacoubi.tasks.infrastructure.persistence.repositories;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.model.Task;
import edu.yacoubi.tasks.domain.model.enums.TaskStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    // edu/yacoubi/tasks/controllers/api/v1/contract/dto/response/task/TaskSummaryDto.java
    @Query("""
                SELECT new edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.task.TaskSummaryDto(
                    t.id,
                    t.title,
                    t.description,
                    t.dueDate,
                    t.priority,
                    t.status,
                    t.taskList.id
                )
                FROM Task t
                WHERE t.taskList.id = :taskListId
            """)
    List<TaskSummaryDto> findByTaskListId(UUID taskListId);


    @Query("""
                SELECT new edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.task.TaskSummaryDto(
                    t.id,
                    t.title,
                    t.description,
                    t.dueDate,
                    t.priority,
                    t.status,
                    t.taskList.id
                )
                FROM Task t
                WHERE t.taskList.id = :taskListId
                  AND t.status = :status
            """)
    List<TaskSummaryDto> findByTaskListIdAndStatus(UUID taskListId, TaskStatus status);

    @Query("""
                SELECT new edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.task.TaskSummaryDto(
                    t.id,
                    t.title,
                    t.description,
                    t.dueDate,
                    t.priority,
                    t.status,
                    t.taskList.id
                )
                FROM Task t
                WHERE t.taskList.id = :taskListId
            """)
    Page<TaskSummaryDto> findByTaskListId(UUID taskListId, Pageable pageable);
}
