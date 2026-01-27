-- ==========================================
-- Migration V10: Add role and joined_at to Project_Members and create Task_Assignments table
-- ==========================================

-- Add role and joined_at columns to project_members table
ALTER TABLE project_members
ADD COLUMN role ENUM('MANAGER', 'MEMBER') NOT NULL DEFAULT 'MEMBER',
ADD COLUMN joined_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);

-- Task_Assignments Table - Manages N-N relationship between Tasks and Users
CREATE TABLE task_assignments
(
    task_id      INT         NOT NULL,
    user_id      VARCHAR(36) NOT NULL,
    assigned_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES tasks (task_id) ON DELETE CASCADE
    -- Note: No FK to users table following Spring Modulith pattern (cross-module reference)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
