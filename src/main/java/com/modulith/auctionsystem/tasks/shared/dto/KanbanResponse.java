package com.modulith.auctionsystem.tasks.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Kanban board response")
public record KanbanResponse(
        @Schema(description = "Kanban board ID", example = "1")
        Integer kanbanId,

        @Schema(description = "Project ID", example = "1")
        Integer projectId,

        @Schema(description = "Kanban board name", example = "Sprint 1 Board")
        String name,

        @Schema(description = "Kanban board description", example = "Main development board for Sprint 1")
        String description,

        @Schema(description = "Created by user ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String createdBy,

        @Schema(description = "Creation timestamp")
        Instant createdAt,

        @Schema(description = "Last update timestamp")
        Instant updatedAt
) {
}

