package com.modulith.auctionsystem.users.domain;

import com.modulith.auctionsystem.users.UserService;
import com.modulith.auctionsystem.users.domain.models.UserProfileView;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Transactional
    public UserProfileView syncUserFromKeycloak(Jwt jwt) {
        var id = jwt.getClaimAsString("sub");
        log.debug("Syncing user with ID: {}", id);

        var user = userRepository.findById(id).orElseGet(
                () -> User.builder()
                        .userId(id)
                        .email(jwt.getClaimAsString("email"))
                        .username(jwt.getClaimAsString("preferred_username"))
                        .fullName(jwt.getClaimAsString("name"))
                        .build()
        );
        user.setEmail(jwt.getClaimAsString("email"));
        user.setFullName(jwt.getClaimAsString("name"));

        User savedUser = userRepository.save(user);
        log.debug("Synced user with ID: {}", savedUser.getUserId());

        return UserProfileView.from(savedUser);
    }

    @Transactional(readOnly = true)
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}
