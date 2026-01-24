package com.modulith.auctionsystem.collaboration.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Comment response with all details")
public record CommentResponse(
        @Schema(description = "Comment ID", example = "1")
        Integer commentId,

        @Schema(description = "Task ID this comment belongs to", example = "1")
        Integer taskId,

        @Schema(description = "User ID who created the comment", example = "550e8400-e29b-41d4-a716-446655440000")
        String userId,

        @Schema(description = "Comment content", example = "This task needs more clarification on the requirements.")
        String content,

        @Schema(description = "Creation timestamp", example = "2026-01-24T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Last update timestamp", example = "2026-01-24T14:30:00Z")
        Instant updatedAt,

        @Schema(description = "User ID who created the comment", example = "550e8400-e29b-41d4-a716-446655440000")
        String createdBy,

        @Schema(description = "User ID who last updated the comment", example = "550e8400-e29b-41d4-a716-446655440000")
        String updatedBy
) {
}
