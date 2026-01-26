package com.modulith.auctionsystem.projects.domain.repository;

import com.modulith.auctionsystem.projects.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByCreatedBy(String userId);

    List<Project> findByCreatedByAndDeletedAtIsNull(String userId);
}
