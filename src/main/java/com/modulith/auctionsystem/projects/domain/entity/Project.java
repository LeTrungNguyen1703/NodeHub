package com.modulith.auctionsystem.projects.domain.entity;

import com.modulith.auctionsystem.common.domain.AbstractAuditableEntity;
import com.modulith.auctionsystem.projects.domain.valueobject.ProjectName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "projects", indexes = {
        @Index(name = "idx_project_created_by", columnList = "created_by")
})
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Project extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false, length = 100))
    })
    private ProjectName name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Size(max = 36)
    @Column(name = "created_by", length = 36)
    private String createdBy;

    @Column(name = "deleted_at")
    private java.time.Instant deletedAt;
}
