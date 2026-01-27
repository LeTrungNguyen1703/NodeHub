-- ==========================================
-- Migration V11: Alter tables for JPA compatibility and cleanup
-- ==========================================

-- ==========================================
-- PART 1: Clean up unused columns from users table
-- ==========================================
ALTER TABLE users DROP COLUMN address;
ALTER TABLE users DROP COLUMN date_of_birth;
ALTER TABLE users DROP COLUMN is_anonymous;
ALTER TABLE users DROP COLUMN phone;
ALTER TABLE users DROP COLUMN preferred_language;

-- ==========================================
-- PART 2: Standardize audit columns to VARCHAR(36)
-- ==========================================
-- Users table: created_by from VARCHAR(255) to VARCHAR(36)
ALTER TABLE users MODIFY created_by VARCHAR(36);
-- Users table: updated_by from VARCHAR(255) to VARCHAR(36)
ALTER TABLE users MODIFY updated_by VARCHAR(36);

-- ==========================================
-- PART 3: Convert ENUM columns to VARCHAR(255) for JPA compatibility
-- All entities use @Enumerated(EnumType.STRING) which requires VARCHAR
-- ==========================================

-- 1. tasks.status: ENUM('TO_DO', 'IN_PROGRESS', 'DONE', 'CANCEL') → VARCHAR(255)
ALTER TABLE tasks MODIFY status VARCHAR(255) DEFAULT 'TO_DO' NOT NULL;

-- 2. tasks.priority: ENUM('LOW', 'MEDIUM', 'HIGH') → VARCHAR(255)
ALTER TABLE tasks MODIFY priority VARCHAR(255) DEFAULT 'MEDIUM' NOT NULL;

-- 3. task_dependencies.dependency_type: ENUM('FINISH_TO_START', 'START_TO_START', 'FINISH_TO_FINISH', 'START_TO_FINISH') → VARCHAR(255)
ALTER TABLE task_dependencies MODIFY dependency_type VARCHAR(255) DEFAULT 'FINISH_TO_START' NOT NULL;

-- 4. project_members.role: ENUM('MANAGER', 'MEMBER') → VARCHAR(255)
ALTER TABLE project_members MODIFY `role` VARCHAR(255) DEFAULT 'MEMBER' NOT NULL;

-- 5. notifications.type: ENUM('DEADLINE', 'STATUS_CHANGE', 'ASSIGNMENT') → VARCHAR(255)
ALTER TABLE notifications MODIFY type VARCHAR(255) NOT NULL;

-- ==========================================
-- PART 4: Standardize boolean columns
-- ==========================================
-- notifications.is_read: tinyint(1) → BIT(1) for consistency
ALTER TABLE notifications MODIFY is_read BIT(1) NOT NULL DEFAULT 0;
