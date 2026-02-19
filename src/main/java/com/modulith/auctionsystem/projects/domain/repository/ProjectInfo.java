package com.modulith.auctionsystem.projects.domain.repository;

import com.modulith.auctionsystem.projects.domain.enums.ProjectRole;
import java.time.Instant;

/**
 * Projection interface for Project member query results.
 *
 * Maps the SQL SELECT fields from the native query:
 * - m.id.projectId as projectId
 * - m.id.userId as userId
 * - m.role as role
 * - m.joinedAt as joinedAt
 *
 * Property names must match the aliases in the @Query annotation.
 */
public interface ProjectInfo {
    Integer getProjectId();

    String getUserId();

    ProjectRole getRole();

    Instant getJoinedAt();
}