package com.modulith.auctionsystem.tasks.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "task_assignments")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TaskAssignment {

    @EmbeddedId
    private TaskAssignmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @NotNull
    @Column(name = "assigned_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant assignedAt = Instant.now();

    public TaskAssignment(Task task, String userId) {
        this.task = task;
        this.id = new TaskAssignmentId(task.getTaskId(), userId);
        this.assignedAt = Instant.now();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TaskAssignment that = (TaskAssignment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
