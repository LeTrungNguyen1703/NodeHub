package com.modulith.auctionsystem.users.domain;

import com.modulith.auctionsystem.common.domain.AbstractAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.LocalDate;

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
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @NotNull
    @Size(max = 255)
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Size(max = 100)
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

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Lob
    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "avatar")
    private String avatar;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "balance", nullable = false)
    private Long balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'client_user'")
    @Column(name = "role", nullable = false)
    private Role role;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_anonymous", nullable = false)
    private boolean isAnonymous;
}
