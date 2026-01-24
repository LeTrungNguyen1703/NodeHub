package com.modulith.auctionsystem.tasks.shared.dto;

import com.modulith.auctionsystem.tasks.domain.enums.DependencyType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Task dependency response with full task details")
public record TaskDependencyResponse(
        @Schema(description = "Dependency ID", example = "1")
        Integer dependencyId,

        @Schema(description = "Predecessor task ID", example = "1")
        Integer predecessorTaskId,

        @Schema(description = "Predecessor task title", example = "Design database schema")
        String predecessorTaskTitle,

        @Schema(description = "Successor task ID", example = "2")
        Integer successorTaskId,

        @Schema(description = "Successor task title", example = "Implement data layer")
        String successorTaskTitle,

        @Schema(description = "Type of dependency relationship", example = "FINISH_TO_START")
        DependencyType dependencyType
) {
}
