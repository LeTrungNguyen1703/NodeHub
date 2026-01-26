package com.modulith.auctionsystem.projects.shared.dto;

import java.time.Instant;
import java.time.LocalDate;

public record ProjectResponse(
        Integer projectId,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        String createdBy,
        Instant createdAt,
        Instant updatedAt
) {
}
