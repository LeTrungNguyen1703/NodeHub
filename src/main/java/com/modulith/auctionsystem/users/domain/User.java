package com.modulith.auctionsystem.users.domain;

import com.modulith.auctionsystem.common.domain.AbstractAuditableEntity;
import com.modulith.auctionsystem.users.domain.valueobject.Address;
import com.modulith.auctionsystem.users.domain.valueobject.Email;
import com.modulith.auctionsystem.common.valueobject.Money;
import com.modulith.auctionsystem.users.domain.valueobject.PhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_username", columnList = "username")
})
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User extends AbstractAuditableEntity {
    @Id
    @Size(max = 36)
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false))
    })
    private Email email;

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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "phone", length = 20))
    })
    private PhoneNumber phone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "address", columnDefinition = "text"))
    })
    private Address address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "avatar")
    private String avatar;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance", nullable = false))
    })
    private Money balance;

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
