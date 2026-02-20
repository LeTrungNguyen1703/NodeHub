package com.modulith.auctionsystem.tasks.shared.dto;

import com.modulith.auctionsystem.tasks.domain.enums.TaskPriority;
import com.modulith.auctionsystem.tasks.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDateTime;

@Schema(description = "Task response with all details")
public record TaskResponse(
        @Schema(description = "Task ID", example = "1")
        Integer taskId,

        @Schema(description = "Project ID this task belongs to", example = "1")
        Integer projectId,

        @Schema(description = "Task title", example = "Implement user authentication")
        String title,

        @Schema(description = "Task description", example = "Implement JWT-based authentication with refresh tokens")
        String description,

        @Schema(description = "Task start date", example = "2026-02-01T09:00:00")
        LocalDateTime startDate,

        @Schema(description = "Task deadline", example = "2026-02-15T17:00:00")
        LocalDateTime deadline,

        @Schema(description = "Task status", example = "IN_PROGRESS")
        TaskStatus status,

        @Schema(description = "Task priority", example = "HIGH")
        TaskPriority priority,

        @Schema(description = "User ID assigned to this task", example = "550e8400-e29b-41d4-a716-446655440000")
        String assignedTo,

        @Schema(description = "Kanban board ID this task belongs to", example = "1")
        Integer kanbanId,

        @Schema(description = "Position index for Kanban ordering", example = "32768.0")
        Double positionIndex,

        @Schema(description = "Creation timestamp", example = "2026-01-20T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Last update timestamp", example = "2026-01-24T14:20:00Z")
        Instant updatedAt
) {
}
