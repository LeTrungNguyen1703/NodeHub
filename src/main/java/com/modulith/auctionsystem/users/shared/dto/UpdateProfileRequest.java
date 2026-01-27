package com.modulith.auctionsystem.users.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request to update user profile information")
public record UpdateProfileRequest(
        @Size(min = 1 ,max = 255, message = "Full name must be between 1 and 255 characters")
        @Schema(description = "User's full name", example = "John Doe")
        String fullName,

        @Size(min = 1,max = 255, message = "Avatar URL must be between 1 and 255 characters")
        @Schema(description = "Avatar image URL", example = "https://example.com/avatar.jpg")
        String avatar
) {
}
