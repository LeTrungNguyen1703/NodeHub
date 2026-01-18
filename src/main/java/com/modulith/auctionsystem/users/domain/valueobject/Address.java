package com.modulith.auctionsystem.users.domain.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String value) {
    public Address() {
        this(null);
    }

    @Override
    public String toString() {
        return value;
    }
}
