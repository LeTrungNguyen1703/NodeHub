package com.modulith.auctionsystem.projects.config.exceptions;

public class UserNotBelongToProjectException extends IllegalArgumentException {
    public UserNotBelongToProjectException(String message) {
        super(message);
    }
}
