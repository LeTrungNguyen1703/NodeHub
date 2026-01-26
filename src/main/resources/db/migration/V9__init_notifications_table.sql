-- ==========================================
-- MODULE: NOTIFICATION
-- Migration V9: Initialize Notifications table
-- ==========================================

-- Notifications Table - Manages system notifications for users
CREATE TABLE notifications
(
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id         VARCHAR(36),
    message         TEXT                                             NOT NULL,
    is_read         BOOLEAN      DEFAULT FALSE,
    type            ENUM('DEADLINE', 'STATUS_CHANGE', 'ASSIGNMENT') NOT NULL,
    created_at      TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    created_by      VARCHAR(36),
    updated_by      VARCHAR(36),
    INDEX idx_notification_user_id (user_id),
    INDEX idx_notification_is_read (is_read),
    INDEX idx_notification_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
