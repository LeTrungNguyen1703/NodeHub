package com.modulith.auctionsystem.payment.domain;

import com.modulith.auctionsystem.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "held_balances")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeldBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hold_id")
    private Integer holdId;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "auction_id", nullable = false)
    private Integer auctionId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false))
    })
    private Money amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;
}
