package com.modulith.auctionsystem.users.shared.dto;

import com.modulith.auctionsystem.users.domain.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserProfileView(String userId, String email, String username, String fullName) implements Serializable {

    public static UserProfileView from(User user) {
        return new UserProfileView(
                user.getUserId(),
                user.getEmail() != null ? user.getEmail().value() : null,
                user.getUsername(),
                user.getFullName()
        );
    }
}