package com.modulith.auctionsystem.collaboration.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new comment")
public record CreateCommentRequest(
        @NotNull(message = "Task ID is required")
        @Schema(description = "Task ID this comment belongs to", example = "1")
        Integer taskId,

        @NotBlank(message = "Comment content is required")
        @Size(max = 5000, message = "Content must not exceed 5000 characters")
        @Schema(description = "Comment content", example = "This task needs more clarification on the requirements.")
        String content
) {
}
