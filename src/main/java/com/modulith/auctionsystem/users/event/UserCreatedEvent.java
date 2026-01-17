package com.modulith.auctionsystem.users.event;

public record UserCreatedEvent(String userId, String email, String username) {
}
