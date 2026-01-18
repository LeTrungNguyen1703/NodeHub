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

        @Size(min = 9,max = 12, message = "Phone number must be between 9 and 12 characters")
        @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Phone number contains invalid characters")
        @Schema(description = "Phone number", example = "+1234567890")
        String phone,

        @Size(min = 5,max = 500, message = "Address must be between 5 and 500 characters")
        @Schema(description = "User address", example = "123 Main St, City, Country")
        String address,

        @Past(message = "Date of birth must be in the past")
        @Schema(description = "Date of birth", example = "1990-01-01")
        LocalDate dateOfBirth,

        @Size(min = 1,max = 255, message = "Avatar URL must be between 1 and 255 characters")
        @Schema(description = "Avatar image URL", example = "https://example.com/avatar.jpg")
        String avatar
) {
}
