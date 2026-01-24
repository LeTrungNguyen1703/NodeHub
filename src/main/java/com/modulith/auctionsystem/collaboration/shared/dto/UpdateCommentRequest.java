package com.modulith.auctionsystem.collaboration.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update an existing comment")
public record UpdateCommentRequest(
        @Size(max = 5000, message = "Content must not exceed 5000 characters")
        @Schema(description = "Updated comment content", example = "Updated: Requirements have been clarified in the documentation.")
        String content
) {
}
