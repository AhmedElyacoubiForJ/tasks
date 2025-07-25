package edu.yacoubi.tasks.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "task_lists")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "taskList", cascade = {
            CascadeType.REMOVE, CascadeType.PERSIST
    })
    private List<Task> tasks;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tasks=" + tasks +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
