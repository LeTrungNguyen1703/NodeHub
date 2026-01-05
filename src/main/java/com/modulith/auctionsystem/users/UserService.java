package com.modulith.auctionsystem.users;

import com.modulith.auctionsystem.users.domain.models.UserProfileView;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {
    UserProfileView syncUserFromKeycloak(Jwt jwt);

}
