package com.modulith.auctionsystem.product.domain;

import com.modulith.auctionsystem.common.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_name", columnList = "product_name"),
        @Index(name = "idx_product_status", columnList = "status")
})
@Getter
@Setter
@ToString(exclude = {"category", "images"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "seller_id", nullable = false, length = 36)
    private String sellerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "starting_price", nullable = false))
    })
    private Money startingPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "buy_now_price"))
    })
    private Money buyNowPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "step_price", nullable = false))
    })
    private Money stepPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status = ProductStatus.pending;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();
}
