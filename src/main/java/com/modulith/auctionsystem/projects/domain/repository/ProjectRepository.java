package com.modulith.auctionsystem.projects.domain.repository;

import com.modulith.auctionsystem.common.domain.PageUtils;
import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.projects.domain.entity.Project;
import com.modulith.auctionsystem.projects.domain.entity.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByCreatedBy(String userId);

    List<Project> findByCreatedByAndDeletedAtIsNull(String userId);

    Page<Project> findByCreatedByAndDeletedAtIsNull(String userId, Pageable pageable);

    default PagedResult<Project> findAllByCreatedBy(String userId, Pageable pageable) {
        return new PagedResult<>(findByCreatedByAndDeletedAtIsNull(userId, PageUtils.makePageable(pageable, "projectId")));
    }

    boolean existsByProjectIdAndCreatedBy(Integer projectId, String createdBy);

    boolean existsByMembers_Id_ProjectIdAndMembers_Id_UserId(Integer projectId, String userId);

    @Query("""
            select m
            from ProjectMember m
            where m.id.projectId = :projectId
            """)
    Set<ProjectMember> findMembersByProjectId(Integer projectId);

}
