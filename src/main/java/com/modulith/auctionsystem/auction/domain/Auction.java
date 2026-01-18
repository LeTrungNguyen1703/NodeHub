package com.modulith.auctionsystem.auction.domain;

import com.modulith.auctionsystem.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auctions", indexes = {
        @Index(name = "idx_auction_status", columnList = "status"),
        @Index(name = "idx_auction_end_time", columnList = "end_time")
})
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Integer auctionId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "host_id", nullable = false, length = 36)
    private String hostId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "original_end_time", nullable = false)
    private LocalDateTime originalEndTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "current_price", nullable = false))
    })
    private Money currentPrice;

    @Column(name = "winner_id", length = 36)
    private String winnerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AuctionStatus status = AuctionStatus.scheduled;

    @Column(name = "is_host_anonymous")
    private Boolean isHostAnonymous = false;

    @Version
    private Integer version;
}
