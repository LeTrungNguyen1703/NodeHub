package com.modulith.auctionsystem.notifications.shared.dto;

import com.modulith.auctionsystem.notifications.domain.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Notification response with all details")
public record NotificationResponse(
        @Schema(description = "Notification ID", example = "1")
        Integer notificationId,

        @Schema(description = "User ID who receives the notification", example = "550e8400-e29b-41d4-a716-446655440000")
        String userId,

        @Schema(description = "Notification message", example = "Task deadline is approaching in 2 days")
        String message,

        @Schema(description = "Whether the notification has been read", example = "false")
        Boolean isRead,

        @Schema(description = "Notification type", example = "DEADLINE")
        NotificationType type,

        @Schema(description = "Creation timestamp", example = "2026-01-24T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Last update timestamp", example = "2026-01-24T14:30:00Z")
        Instant updatedAt,

        @Schema(description = "User ID who created the notification", example = "550e8400-e29b-41d4-a716-446655440000")
        String createdBy,

        @Schema(description = "User ID who last updated the notification", example = "550e8400-e29b-41d4-a716-446655440000")
        String updatedBy
) {
}
