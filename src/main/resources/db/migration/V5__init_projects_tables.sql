-- ==========================================
-- MODULE: PROJECT MANAGEMENT
-- Migration V5: Initialize Projects and Project_Members tables
-- ==========================================

-- Projects Table - Manages project details
CREATE TABLE projects
(
    project_id  INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    start_date  DATE,
    end_date    DATE,
    created_by  VARCHAR(36),
    deleted_at  TIMESTAMP(6),
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    created_by_audit  VARCHAR(36),
    updated_by_audit  VARCHAR(36),
    INDEX idx_project_created_by (created_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Project_Members Table - Manages project memberships
CREATE TABLE project_members
(
    project_id INT         NOT NULL,
    user_id    VARCHAR(36) NOT NULL,
    PRIMARY KEY (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
