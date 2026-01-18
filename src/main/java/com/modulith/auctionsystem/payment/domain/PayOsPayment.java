package com.modulith.auctionsystem.payment.domain;

import com.modulith.auctionsystem.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payos_payments")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOsPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "order_code", nullable = false, unique = true)
    private Long orderCode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false))
    })
    private Money amount;

    @Column(length = 255)
    private String description;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(length = 255)
    private String reference;

    @Column(name = "transaction_datetime")
    private LocalDateTime transactionDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.pending;

    @Lob
    @Column(name = "payos_data", columnDefinition = "TEXT")
    private String payosData;
}
