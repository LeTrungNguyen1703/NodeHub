package com.modulith.auctionsystem.users.domain;

import com.modulith.auctionsystem.common.domain.AbstractAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User extends AbstractAuditableEntity {
    @Id
    @Size(max = 36)
    @NotNull
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 100)
    @NotNull
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Size(max = 255)
    @Column(name = "full_name")
    private String fullName;

    @Size(max = 10)
    @ColumnDefault("'vi'")
    @Column(name = "preferred_language", length = 10)
    private String preferredLanguage;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    protected void onCreate() {
        if (preferredLanguage == null) {
            preferredLanguage = "vi";
        }
    }
}