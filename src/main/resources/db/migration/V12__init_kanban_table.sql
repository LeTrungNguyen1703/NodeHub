-- ==========================================
-- MODULE: TASK MANAGEMENT
-- Migration V12: Initialize Kanban Boards table
-- ==========================================

-- Kanban Boards Table - Manages Kanban boards for projects
CREATE TABLE kanbans
(
    kanban_id   INT AUTO_INCREMENT PRIMARY KEY,
    project_id  INT          NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    created_by  VARCHAR(36),
    updated_by  VARCHAR(36),
    deleted_at  TIMESTAMP(6),
    INDEX idx_kanban_project_id (project_id),
    INDEX idx_kanban_created_by (created_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add optional kanban_id column to tasks table for future board assignment
ALTER TABLE tasks
    ADD COLUMN kanban_id INT NULL AFTER project_id,
    ADD INDEX idx_task_kanban_id (kanban_id);