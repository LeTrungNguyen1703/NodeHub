package com.modulith.auctionsystem.auction.domain;

import com.modulith.auctionsystem.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auto_bids")
@Getter
@Setter
@ToString(exclude = "auction")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auto_bid_id")
    private Integer autoBidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Column(name = "bidder_id", nullable = false, length = 36)
    private String bidderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "max_amount", nullable = false))
    })
    private Money maxAmount;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
