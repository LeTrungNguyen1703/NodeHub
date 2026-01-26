package com.modulith.auctionsystem.projects.config;

public class UserAlreadyInProjectException extends IllegalArgumentException {
    public UserAlreadyInProjectException(String message) {
        super(message);
    }
}
