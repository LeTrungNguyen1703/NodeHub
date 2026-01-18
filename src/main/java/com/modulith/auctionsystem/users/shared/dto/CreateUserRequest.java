package com.modulith.auctionsystem.users.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new user")
public record CreateUserRequest(
        @NotBlank(message = "User ID is required")
        @Size(max = 36, message = "User ID must not exceed 36 characters")
        @Schema(description = "Unique user identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        String userId,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must not exceed 255 characters")
        @Schema(description = "User email address", example = "user@example.com")
        String email,

        @NotBlank(message = "Username is required")
        @Size(max = 100, message = "Username must not exceed 100 characters")
        @Schema(description = "Username", example = "john_doe")
        String username,

        @Size(max = 255, message = "Full name must not exceed 255 characters")
        @Schema(description = "User's full name", example = "John Doe")
        String fullName
) {
}
