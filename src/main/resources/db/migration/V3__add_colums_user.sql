ALTER TABLE users
    ADD address TEXT NULL;

ALTER TABLE users
    ADD avatar VARCHAR(255) NULL;

ALTER TABLE users
    ADD balance BIGINT NULL;

ALTER TABLE users
    ADD date_of_birth date NULL;

ALTER TABLE users
    ADD is_anonymous BIT(1) DEFAULT 0 NULL;

ALTER TABLE users
    ADD phone VARCHAR(20) NULL;

ALTER TABLE users
    ADD `role` VARCHAR(255) DEFAULT 'client_user' NULL;

ALTER TABLE users
    MODIFY balance BIGINT NOT NULL;

ALTER TABLE users
    MODIFY is_anonymous BIT (1) NOT NULL;

ALTER TABLE users
    MODIFY `role` VARCHAR (255) NOT NULL;

CREATE INDEX idx_user_email ON users (email);

CREATE INDEX idx_user_username ON users (username);