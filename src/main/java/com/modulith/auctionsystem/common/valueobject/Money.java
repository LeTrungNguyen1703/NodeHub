package com.modulith.auctionsystem.common.valueobject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public record Money(Long amount) {
    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
    }

    /**
     * Default constructor for JPA.
     */
    public Money() {
        this(0L);
    }

    public static final Money ZERO = new Money(0L);
}
