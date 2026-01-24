-- ==========================================
-- MODULE: TASK MANAGEMENT
-- Migration V6: Initialize Tasks and Task_Dependencies tables
-- ==========================================

-- Tasks Table - Manages task details
CREATE TABLE tasks
(
    task_id     INT AUTO_INCREMENT PRIMARY KEY,
    project_id  INT,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    start_date  DATETIME(6),
    deadline    DATETIME(6),
    status      ENUM('TO_DO', 'IN_PROGRESS', 'DONE', 'CANCEL') NOT NULL DEFAULT 'TO_DO',
    priority    ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL DEFAULT 'MEDIUM',
    assigned_to VARCHAR(36),
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    created_by_audit  VARCHAR(36),
    updated_by_audit  VARCHAR(36),
    INDEX idx_task_project_id (project_id),
    INDEX idx_task_assigned_to (assigned_to),
    INDEX idx_task_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Task_Dependencies Table - Manages task dependency relationships
CREATE TABLE task_dependencies
(
    dependency_id       INT AUTO_INCREMENT PRIMARY KEY,
    predecessor_task_id INT NOT NULL,
    successor_task_id   INT NOT NULL,
    dependency_type     ENUM('FINISH_TO_START', 'START_TO_START', 'FINISH_TO_FINISH', 'START_TO_FINISH') NOT NULL DEFAULT 'FINISH_TO_START',
    UNIQUE KEY uk_predecessor_successor (predecessor_task_id, successor_task_id),
    INDEX idx_predecessor_task (predecessor_task_id),
    INDEX idx_successor_task (successor_task_id),
    FOREIGN KEY (predecessor_task_id) REFERENCES tasks (task_id) ON DELETE CASCADE,
    FOREIGN KEY (successor_task_id) REFERENCES tasks (task_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
