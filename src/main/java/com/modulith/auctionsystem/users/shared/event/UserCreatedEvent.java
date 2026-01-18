package com.modulith.auctionsystem.users.shared.event;

public record UserCreatedEvent(String userId, String email, String username) {
}
