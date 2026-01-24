package com.modulith.auctionsystem.notifications.shared.dto;

import com.modulith.auctionsystem.notifications.domain.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new notification")
public record CreateNotificationRequest(
        @NotNull(message = "User ID is required")
        @Size(max = 36, message = "User ID must not exceed 36 characters")
        @Schema(description = "User ID to receive the notification", example = "550e8400-e29b-41d4-a716-446655440000")
        String userId,

        @NotBlank(message = "Notification message is required")
        @Size(max = 5000, message = "Message must not exceed 5000 characters")
        @Schema(description = "Notification message", example = "Task deadline is approaching in 2 days")
        String message,

        @NotNull(message = "Notification type is required")
        @Schema(description = "Notification type", example = "DEADLINE")
        NotificationType type
) {
}
