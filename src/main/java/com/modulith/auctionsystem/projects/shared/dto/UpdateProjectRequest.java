package com.modulith.auctionsystem.projects.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request to update an existing project")
public record UpdateProjectRequest(
        @Size(max = 100, message = "Project name must not exceed 100 characters")
        @Schema(description = "Project name", example = "NodeHub Development Phase 2")
        String name,

        @Size(max = 5000, message = "Description must not exceed 5000 characters")
        @Schema(description = "Project description", example = "Updated project description")
        String description,

        @FutureOrPresent(message = "Start date must be in the present or future")
        @Schema(description = "Project start date", example = "2026-02-01")
        LocalDate startDate,

        @FutureOrPresent(message = "End date must be in the present or future")
        @Schema(description = "Project end date", example = "2026-12-31")
        LocalDate endDate
) {
}
