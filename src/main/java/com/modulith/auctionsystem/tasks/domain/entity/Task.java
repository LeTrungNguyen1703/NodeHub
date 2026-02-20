package com.modulith.auctionsystem.tasks.domain.entity;

import com.modulith.auctionsystem.common.domain.AbstractAuditableEntity;
import com.modulith.auctionsystem.tasks.domain.enums.TaskPriority;
import com.modulith.auctionsystem.tasks.domain.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks", indexes = {
        @Index(name = "idx_task_project_id", columnList = "project_id"),
        @Index(name = "idx_task_assigned_to", columnList = "assigned_to"),
        @Index(name = "idx_task_status", columnList = "status"),
        @Index(name = "idx_task_status_position", columnList = "status, position_index")
})
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Task extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "project_id")
    private Integer projectId; // Cross-module reference - no JPA relationship

    @NotNull
    @Size(max = 200)
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'TO_DO'")
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'MEDIUM'")
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @Size(max = 36)
    @Column(name = "assigned_to", length = 36)
    private String assignedTo; // Cross-module reference - no JPA relationship


    @ColumnDefault("65535.0")
    @Column(name = "position_index")
    private Double positionIndex; // Position for drag-and-drop ordering within status column

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "kanban_id")
    private Kanban kanban;

}
