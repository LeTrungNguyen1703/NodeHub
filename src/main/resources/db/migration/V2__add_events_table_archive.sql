CREATE TABLE IF NOT EXISTS event_publication_archive
(
    -- ID is usually a UUID. In MySQL, VARCHAR(36) is the safest default for Hibernate.
    -- If you explicitly configured BINARY(16) for UUIDs, change this to BINARY(16).
    id               VARCHAR(36) NOT NULL,

    -- When the event was finished processing
    completion_date  DATETIME(6),

    -- The Java class name of the event (e.g., com.myapp.OrderCreatedEvent)
    event_type       VARCHAR(512),

    -- The method/listener that handled the event
    listener_id      VARCHAR(512),

    -- When the event was originally fired
    publication_date DATETIME(6),

    -- The actual JSON data of the event.
    serialized_event JSON,

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;