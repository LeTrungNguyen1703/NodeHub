package com.modulith.auctionsystem.tasks.domain.entity;

import com.modulith.auctionsystem.common.domain.AbstractAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "kanbans", indexes = {
        @Index(name = "idx_kanban_project_id", columnList = "project_id"),
        @Index(name = "idx_kanban_created_by", columnList = "created_by_audit")
})
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Kanban extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_id", nullable = false)
    private Integer kanbanId;

    @NotNull
    @Column(name = "project_id", nullable = false)
    private Integer projectId; // Cross-module reference - no JPA relationship

    @NotNull
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}

