CREATE TABLE kanbans
(
    kanban_id     INT AUTO_INCREMENT     NOT NULL,
    created_at    datetime DEFAULT NOW() NOT NULL,
    updated_at    datetime DEFAULT NOW() NULL,
    created_by    VARCHAR(36) NULL,
    updated_by    VARCHAR(36) NULL,
    project_id    INT                    NOT NULL,
    name          VARCHAR(100)           NOT NULL,
    `description` VARCHAR(500) NULL,
    deleted_at    datetime NULL,
    CONSTRAINT pk_kanbans PRIMARY KEY (kanban_id)
);

ALTER TABLE tasks
    ADD position_index DOUBLE DEFAULT 65535 NULL;

CREATE INDEX idx_kanban_created_by ON kanbans (created_by);

CREATE INDEX idx_kanban_project_id ON kanbans (project_id);

CREATE INDEX idx_task_status_position ON tasks (status, position_index);

ALTER TABLE users
    ALTER `role` SET DEFAULT 'client_user';

ALTER TABLE comments
    MODIFY updated_at datetime NULL;

ALTER TABLE notifications
    MODIFY updated_at datetime NULL;

ALTER TABLE projects
    MODIFY updated_at datetime NULL;

ALTER TABLE tasks
    MODIFY updated_at datetime NULL;