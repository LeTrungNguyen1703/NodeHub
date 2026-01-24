package com.modulith.auctionsystem.tasks.shared.dto;

import com.modulith.auctionsystem.tasks.domain.enums.TaskPriority;
import com.modulith.auctionsystem.tasks.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Schema(description = "Request to create a new task")
public record CreateTaskRequest(
        @NotNull(message = "Project ID is required")
        @Schema(description = "Project ID this task belongs to", example = "1")
        Integer projectId,

        @NotBlank(message = "Task title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        @Schema(description = "Task title", example = "Implement user authentication")
        String title,

        @Size(max = 5000, message = "Description must not exceed 5000 characters")
        @Schema(description = "Task description", example = "Implement JWT-based authentication with refresh tokens")
        String description,

        @Schema(description = "Task start date", example = "2026-02-01T09:00:00")
        LocalDateTime startDate,

        @Schema(description = "Task deadline", example = "2026-02-15T17:00:00")
        @FutureOrPresent(message = "Deadline must be in the present or future")
        LocalDateTime deadline,

        @Schema(description = "Task status", example = "TO_DO", defaultValue = "TO_DO")
        TaskStatus status,

        @Schema(description = "Task priority", example = "HIGH", defaultValue = "MEDIUM")
        TaskPriority priority,

        @Size(max = 36, message = "Assigned user ID must not exceed 36 characters")
        @Schema(description = "User ID assigned to this task", example = "550e8400-e29b-41d4-a716-446655440000")
        String assignedTo
) {
}
