package com.modulith.auctionsystem.projects.config;

public class ProjectNotFoundException extends IllegalArgumentException {
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
