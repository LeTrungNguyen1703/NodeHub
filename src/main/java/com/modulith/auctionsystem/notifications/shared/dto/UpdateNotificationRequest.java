package com.modulith.auctionsystem.notifications.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to update notification read status")
public record UpdateNotificationRequest(
        @Schema(description = "Whether the notification has been read", example = "true")
        Boolean isRead
) {
}
