package com.modulith.auctionsystem.auction.domain;

import com.modulith.auctionsystem.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
@Getter
@Setter
@ToString(exclude = "auction")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Integer bidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Column(name = "bidder_id", nullable = false, length = 36)
    private String bidderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "bid_amount", nullable = false))
    })
    private Money bidAmount;

    @Column(name = "bid_time")
    private LocalDateTime bidTime;

    @Column(name = "is_auto_bid")
    private Boolean isAutoBid = false;

    @Column(name = "is_buy_now")
    private Boolean isBuyNow = false;
}
