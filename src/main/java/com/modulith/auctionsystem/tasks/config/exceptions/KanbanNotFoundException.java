package com.modulith.auctionsystem.tasks.config.exceptions;

public class KanbanNotFoundException extends IllegalArgumentException {
    public KanbanNotFoundException(String message) {
        super(message);
    }
}

