package com.modulith.auctionsystem.tasks.shared.dto;

import com.modulith.auctionsystem.tasks.domain.enums.DependencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to create a task dependency relationship")
public record CreateTaskDependencyRequest(
        @NotNull(message = "Predecessor task ID is required")
        @Schema(description = "ID of the task that must be completed first", example = "1")
        Integer predecessorTaskId,

        @NotNull(message = "Successor task ID is required")
        @Schema(description = "ID of the task that depends on the predecessor", example = "2")
        Integer successorTaskId,

        @Schema(description = "Type of dependency relationship", example = "FINISH_TO_START", defaultValue = "FINISH_TO_START")
        DependencyType dependencyType
) {
}
