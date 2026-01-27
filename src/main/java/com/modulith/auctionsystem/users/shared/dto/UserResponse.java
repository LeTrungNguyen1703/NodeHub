package com.modulith.auctionsystem.users.shared.dto;

import com.modulith.auctionsystem.users.domain.Role;

import java.time.LocalDate;

public record UserResponse(
        String userId,
        String email,
        String username,
        String fullName,
        String avatar,
        Role role
) {
}
