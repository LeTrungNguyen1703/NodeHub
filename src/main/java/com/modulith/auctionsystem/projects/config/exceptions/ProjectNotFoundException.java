package com.modulith.auctionsystem.projects.config.exceptions;

public class ProjectNotFoundException extends IllegalArgumentException {
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
