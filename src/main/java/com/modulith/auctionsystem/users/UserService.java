package com.modulith.auctionsystem.users;

import com.modulith.auctionsystem.users.web.dto.CreateUserRequest;
import com.modulith.auctionsystem.users.web.dto.UpdateProfileRequest;
import com.modulith.auctionsystem.users.web.dto.UserResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface UserService {
    UserResponse syncUserOnLogin(Jwt jwt);

    Optional<UserResponse> findByEmail(String email);

    Optional<UserResponse> findByUserId(String userId);

    void updateUserProfile(String userId, UpdateProfileRequest updateProfileRequest);
}
