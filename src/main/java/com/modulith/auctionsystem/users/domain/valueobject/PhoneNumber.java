package com.modulith.auctionsystem.users.domain.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record PhoneNumber(String value) {
    public PhoneNumber() {
        this(null);
    }

    @Override
    public String toString() {
        return value;
    }
}
