package com.modulith.auctionsystem.users.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request to update user profile information")
public record UpdateProfileRequest(
        @Size(max = 255, message = "Full name must not exceed 255 characters")
        @Schema(description = "User's full name", example = "John Doe")
        String fullName,

        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Phone number contains invalid characters")
        @Schema(description = "Phone number", example = "+1234567890")
        String phone,

        @Size(max = 500, message = "Address must not exceed 500 characters")
        @Schema(description = "User address", example = "123 Main St, City, Country")
        String address,

        @Past(message = "Date of birth must be in the past")
        @Schema(description = "Date of birth", example = "1990-01-01")
        LocalDate dateOfBirth,

        @Size(max = 255, message = "Avatar URL must not exceed 255 characters")
        @Schema(description = "Avatar image URL", example = "https://example.com/avatar.jpg")
        String avatar
) {
}
