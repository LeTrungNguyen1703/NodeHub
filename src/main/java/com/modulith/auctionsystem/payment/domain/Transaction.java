package com.modulith.auctionsystem.payment.domain;

import com.modulith.auctionsystem.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@ToString(exclude = "payosPayment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "auction_id")
    private Integer auctionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payos_payment_id")
    private PayOsPayment payosPayment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false))
    })
    private Money amount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_before", nullable = false))
    })
    private Money balanceBefore;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_after", nullable = false))
    })
    private Money balanceAfter;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
}
