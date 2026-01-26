-- ==========================================
-- MODULE: COLLABORATION
-- Migration V8: Initialize Comments table
-- ==========================================

-- Comments Table - Manages task discussions and comments
CREATE TABLE comments
(
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    task_id    INT,
    user_id    VARCHAR(36),
    content    TEXT         NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    INDEX idx_comment_task_id (task_id),
    INDEX idx_comment_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
