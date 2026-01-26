package com.modulith.auctionsystem.projects.config;

public class UserNotBelongToProjectException extends IllegalArgumentException {
    public UserNotBelongToProjectException(String message) {
        super(message);
    }
}
