package com.modulith.auctionsystem.tasks.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update an existing Kanban board")
public record UpdateKanbanRequest(
        @Size(max = 100, message = "Kanban board name must not exceed 100 characters")
        @Schema(description = "Kanban board name", example = "Sprint 1 Board - Updated")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        @Schema(description = "Kanban board description", example = "Updated description for Sprint 1")
        String description
) {
}

