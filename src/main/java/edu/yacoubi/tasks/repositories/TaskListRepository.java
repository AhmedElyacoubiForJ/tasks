package edu.yacoubi.tasks.repositories;

import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskListStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, UUID> {
    Page<TaskList> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleQuery,
            String descriptionQuery,
            Pageable pageable
    );

    List<TaskList> findByStatus(TaskListStatus status);

    List<TaskList> findByStatusOrderByCreatedDesc(TaskListStatus status);

    List<TaskList> findByStatusOrderByUpdatedDesc(TaskListStatus status);
}
