package com.modulith.auctionsystem.projects.shared.dto;

import com.modulith.auctionsystem.projects.domain.enums.ProjectRole;

import java.time.Instant;

public record ProjectMemberResponse(
        Integer projectId,
        String userId,
        ProjectRole role,
        Instant joinedAt
) {
}
