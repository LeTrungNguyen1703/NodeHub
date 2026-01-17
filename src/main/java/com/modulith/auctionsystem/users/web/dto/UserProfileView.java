package com.modulith.auctionsystem.users.web.dto;


import com.modulith.auctionsystem.users.domain.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserProfileView(String userId, String email, String username, String fullName,
                              String preferredLanguage) implements Serializable {

    public static UserProfileView from(User user) {
        return new UserProfileView(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getFullName(),
                user.getPreferredLanguage()
        );
    }
}