package com.modulith.auctionsystem.users.shared.dtos;

import com.modulith.auctionsystem.users.domain.Role;

import java.time.LocalDate;

public record UserResponse(
        String userId,
        String email,
        String username,
        String fullName,
        String phone,
        String address,
        LocalDate dateOfBirth,
        String avatar,
        Role role,
        long balance
) {
}
