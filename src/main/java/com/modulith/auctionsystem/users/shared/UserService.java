package com.modulith.auctionsystem.users.shared;

import com.modulith.auctionsystem.users.shared.dtos.UpdateProfileRequest;
import com.modulith.auctionsystem.users.shared.dtos.UserResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface UserService {
    UserResponse syncUserOnLogin(Jwt jwt);

    UserResponse findByEmail(String email);

   UserResponse findByUserId(String userId);

    void updateUserProfile(String userId, UpdateProfileRequest updateProfileRequest);

}
