package com.modulith.auctionsystem.projects.domain.entity;

import com.modulith.auctionsystem.common.domain.AbstractAuditableEntity;
import com.modulith.auctionsystem.projects.config.exceptions.UserAlreadyInProjectException;
import com.modulith.auctionsystem.projects.config.exceptions.UserNotBelongToProjectException;
import com.modulith.auctionsystem.projects.domain.enums.ProjectRole;
import com.modulith.auctionsystem.projects.domain.valueobject.ProjectName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProjectMember> members = new HashSet<>();

    public void addMember(String userId) {
        var newMember = new ProjectMember(this, userId);
        if (this.members.contains(newMember)) {
            throw new UserAlreadyInProjectException("User is already a member of this project. " + userId);
        }

        boolean added = this.members.add(newMember);
        if (added && this.members.size() == 1) {
            newMember.setRole(ProjectRole.MANAGER);
        }
    }

    public void removeMember(String userId) {
        ProjectMember memberToRemove = this.members.stream()
                .filter(m -> m.getId().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(
                        () -> new UserNotBelongToProjectException("User is not a member of this project. " + userId));

        this.members.remove(memberToRemove);
        memberToRemove.setProject(null);
    }
}
