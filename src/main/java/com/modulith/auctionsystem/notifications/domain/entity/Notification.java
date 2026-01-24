package com.modulith.auctionsystem.notifications.domain.entity;

import com.modulith.auctionsystem.common.domain.AbstractAuditableEntity;
import com.modulith.auctionsystem.notifications.domain.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notification_user_id", columnList = "user_id"),
        @Index(name = "idx_notification_is_read", columnList = "is_read"),
        @Index(name = "idx_notification_type", columnList = "type")
})
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Notification extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Integer notificationId;

    @Column(name = "user_id", length = 36)
    private String userId; // Cross-module reference - no JPA relationship

    @NotNull
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;
}
