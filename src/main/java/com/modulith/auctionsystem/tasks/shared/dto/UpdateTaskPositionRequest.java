package com.modulith.auctionsystem.tasks.shared.dto;

import com.modulith.auctionsystem.tasks.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update task position in Kanban board")
public record UpdateTaskPositionRequest(
        @NotNull(message = "Task ID is required")
        @Schema(description = "Task ID to move", example = "1")
        Integer taskId,

        @NotNull(message = "Target status is required")
        @Schema(description = "Target status column", example = "IN_PROGRESS")
        TaskStatus targetStatus,

        @NotNull(message = "Position index is required")
        @Schema(description = "New position index (between adjacent tasks)", example = "32768.0")
        Double positionIndex
) {
}

