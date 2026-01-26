/*
  Migration V7: Rename audit columns to standard names
*/

-- Projects Table
-- Drop existing created_by to avoid conflict
ALTER TABLE projects DROP COLUMN created_by;

-- Rename audit columns
ALTER TABLE projects CHANGE COLUMN created_by_audit created_by VARCHAR(36);
ALTER TABLE projects CHANGE COLUMN updated_by_audit updated_by VARCHAR(36);

-- Re-create index on created_by
CREATE INDEX idx_project_created_by ON projects (created_by);

-- Tasks Table
ALTER TABLE tasks CHANGE COLUMN created_by_audit created_by VARCHAR(36);
ALTER TABLE tasks CHANGE COLUMN updated_by_audit updated_by VARCHAR(36);
