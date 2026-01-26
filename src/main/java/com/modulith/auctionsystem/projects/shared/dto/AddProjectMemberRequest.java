package com.modulith.auctionsystem.projects.shared.dto;

import com.modulith.auctionsystem.projects.shared.validator.ValidUserId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to add a member to a project")
public record AddProjectMemberRequest(

        @NotBlank(message = "User ID is required")
        @Size(max = 36, message = "User ID must not exceed 36 characters")
        @ValidUserId
        @Schema(description = "User identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        String userId
) {
}
