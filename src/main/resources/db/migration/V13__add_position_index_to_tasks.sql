-- ==========================================
-- MODULE: TASK MANAGEMENT
-- Migration V13: Add position_index for Kanban drag-and-drop ordering
-- ==========================================

-- Add position_index column to tasks table for ordering within status columns
ALTER TABLE tasks
    ADD COLUMN position_index DOUBLE DEFAULT 65535.0 COMMENT 'Position index for drag-and-drop ordering within status column';

-- Create composite index for efficient querying by status and position
CREATE INDEX idx_task_status_position ON tasks (status, position_index);

-- Update existing tasks to have incremental position values grouped by status
-- This ensures existing tasks have proper ordering
SET @row_num = 0;
SET @current_status = '';

UPDATE tasks t
    JOIN (
        SELECT
            task_id,
            status,
            @row_num := IF(@current_status = status, @row_num + 1, 1) AS new_position,
            @current_status := status
        FROM tasks
        ORDER BY status, task_id
    ) AS ranked
    ON t.task_id = ranked.task_id
SET t.position_index = ranked.new_position * 65536.0;

