package com.modulith.auctionsystem.users.internal;

import com.modulith.auctionsystem.users.config.UserNotFoundException;
import com.modulith.auctionsystem.users.domain.Role;
import com.modulith.auctionsystem.users.domain.User;
import com.modulith.auctionsystem.users.domain.UserRepository;
import com.modulith.auctionsystem.users.shared.UserService;
import com.modulith.auctionsystem.users.shared.dtos.UpdateProfileRequest;
import com.modulith.auctionsystem.users.shared.dtos.UserResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    ApplicationEventPublisher events;
    Keycloak keycloak;

    @Value("${keycloak.admin.realm}")
    @NonFinal
    String realm;

    @Override
    @Transactional
    public UserResponse syncUserOnLogin(Jwt jwt) {
        var id = jwt.getSubject();

        var user = userRepository.findById(id).orElseGet(
                () -> User.builder()
                        .userId(id)
                        .email(jwt.getClaimAsString("email"))
                        .username(jwt.getClaimAsString("preferred_username"))
                        .fullName(jwt.getClaimAsString("name"))
                        .role(extractRoleFromJwt(jwt))
                        .balance(0L)
                        .build()
        );
        String avatarUrl = jwt.getClaimAsString("picture");
        if (avatarUrl != null && !avatarUrl.isBlank()) {
            user.setAvatar(avatarUrl);
        }

        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }

    @SuppressWarnings("unchecked")
    private Role extractRoleFromJwt(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles != null && roles.contains("client_admin")) {
                return Role.client_admin;
            }
        }
        return Role.client_user;
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByUserId(String userId) {
        var user = this.getUserById(userId);

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public void updateUserProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Update local database
        userMapper.updateUserFromRequest(request, user);

        updateKeycloakUser(user);

        userRepository.save(user);

        log.debug("Updated profile for user {}", userId);
    }

    @Transactional
    public void updateKeycloakUser(User user) {
        String userId = user.getUserId();
        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();
            this.handleSplitFullNameToFristAndLastName(user, userRepresentation);
            userResource.update(userRepresentation);
            log.debug("Successfully updated Keycloak user {}", userId);

        } catch (NotFoundException e) {
            log.warn("User {} not found in Keycloak, skipping Keycloak update", userId);
        } catch (Exception e) {
            log.error("Failed to update Keycloak user {}: {}", userId, e.getMessage(), e);
            // Don't throw exception - we don't want to rollback DB changes if Keycloak update fails
        }
    }

    //----------------------------- Helper method only used inside this class -------------------------

    User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    void handleSplitFullNameToFristAndLastName(User user, UserRepresentation userRepresentation) {
        if (user.getFullName() != null) {
            String fullName = user.getFullName().trim();
            if (!fullName.isBlank()) {
                String[] nameParts = fullName.split("\\s+", 2);
                userRepresentation.setFirstName(nameParts[0]);
                if (nameParts.length > 1) {
                    userRepresentation.setLastName(nameParts[1]);
                } else {
                    userRepresentation.setLastName("");
                }
            }
        }

    }
}
