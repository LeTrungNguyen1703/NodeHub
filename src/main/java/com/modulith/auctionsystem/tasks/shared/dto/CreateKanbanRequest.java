package com.modulith.auctionsystem.tasks.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new Kanban board")
public record CreateKanbanRequest(
        @NotNull(message = "Project ID is required")
        @Schema(description = "Project ID this Kanban board belongs to", example = "1")
        Integer projectId,

        @NotBlank(message = "Kanban board name is required")
        @Size(max = 100, message = "Kanban board name must not exceed 100 characters")
        @Schema(description = "Kanban board name", example = "Sprint 1 Board")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        @Schema(description = "Kanban board description", example = "Main development board for Sprint 1")
        String description
) {
}

