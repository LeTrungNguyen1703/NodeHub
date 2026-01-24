package com.modulith.auctionsystem.tasks.domain.entity;

import com.modulith.auctionsystem.tasks.domain.enums.DependencyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "task_dependencies",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_predecessor_successor",
            columnNames = {"predecessor_task_id", "successor_task_id"})
    },
    indexes = {
        @Index(name = "idx_predecessor_task", columnList = "predecessor_task_id"),
        @Index(name = "idx_successor_task", columnList = "successor_task_id")
    }
)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TaskDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dependency_id", nullable = false)
    private Integer dependencyId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predecessor_task_id", nullable = false)
    private Task predecessorTask;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "successor_task_id", nullable = false)
    private Task successorTask;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FINISH_TO_START'")
    @Column(name = "dependency_type", nullable = false)
    private DependencyType dependencyType;
}
