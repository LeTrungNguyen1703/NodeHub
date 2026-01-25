package com.modulith.auctionsystem.users.shared.public_api;

import com.modulith.auctionsystem.users.shared.dto.UpdateProfileRequest;
import com.modulith.auctionsystem.users.shared.dto.UserResponse;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserPublicApi {
    UserResponse syncUserOnLogin(Jwt jwt);

    UserResponse findByEmail(String email);

    UserResponse findByUserId(String userId);

    void updateUserProfile(String userId, UpdateProfileRequest updateProfileRequest);

}
