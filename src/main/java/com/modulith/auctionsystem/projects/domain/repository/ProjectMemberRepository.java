package com.modulith.auctionsystem.projects.domain.repository;

import com.modulith.auctionsystem.projects.domain.entity.ProjectMember;
import com.modulith.auctionsystem.projects.domain.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectId(Integer projectId);

    List<ProjectMember> findByIdUserId(String userId);

    boolean existsByIdProjectIdAndIdUserId(Integer projectId, String userId);
}
