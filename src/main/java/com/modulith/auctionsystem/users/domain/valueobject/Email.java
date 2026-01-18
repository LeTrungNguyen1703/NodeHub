package com.modulith.auctionsystem.users.domain.valueobject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public record Email(String value) {
    public Email {
        // Validation can be added here
        // For now allowing null as JPA might use default constructor with null
    }

    public Email() {
        this(null);
    }

    @Override
    public String toString() {
        return value;
    }
}
