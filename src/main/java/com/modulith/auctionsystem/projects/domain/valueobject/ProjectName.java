package com.modulith.auctionsystem.projects.domain.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProjectName(String value) {
    public ProjectName {
        if (value == null) {
            throw new IllegalArgumentException("Project name must not be null");
        }
    }
}
