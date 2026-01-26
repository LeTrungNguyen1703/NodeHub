package com.modulith.auctionsystem.projects.config.exceptions;

public class UserAlreadyInProjectException extends IllegalArgumentException {
    public UserAlreadyInProjectException(String message) {
        super(message);
    }
}
