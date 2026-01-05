CREATE TABLE users
(
    user_id            VARCHAR(36)               NOT NULL,
    created_at         datetime    DEFAULT NOW() NOT NULL,
    updated_at         datetime    DEFAULT NOW() NULL,
    created_by         VARCHAR(255)              NULL,
    updated_by         VARCHAR(255)              NULL,
    email              VARCHAR(255)              NOT NULL,
    username           VARCHAR(100)              NOT NULL,
    full_name          VARCHAR(255)              NULL,
    preferred_language VARCHAR(10) DEFAULT 'vi'  NULL,
    last_login         datetime                  NULL,
    deleted_at         datetime                  NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS event_publication
(
    id               CHAR(36)      NOT NULL,
    event_type       VARCHAR(512)  NOT NULL,
    listener_id      VARCHAR(512)  NOT NULL,
    publication_date TIMESTAMP(6)  NOT NULL,
    completion_date  TIMESTAMP(6)  NULL,
    serialized_event JSON          NOT NULL,
    PRIMARY KEY (id)
);