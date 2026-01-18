package com.modulith.auctionsystem.users.web.controllers;

import com.modulith.auctionsystem.common.web.response.GenericApiResponse;
import com.modulith.auctionsystem.users.shared.UserService;
import com.modulith.auctionsystem.users.shared.dtos.UpdateProfileRequest;
import com.modulith.auctionsystem.users.shared.dtos.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User management", description = "APIs for managing users")
class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get current user profile",
            description = "Retrieves the profile of the currently authenticated user",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<GenericApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        UserResponse userProfile = userService.syncUserOnLogin(jwt);
        return ResponseEntity.ok(GenericApiResponse.success("User profile retrieved successfully", userProfile, 200));
    }

    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Update current user profile",
            description = "Partially updates the profile of the currently authenticated user",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<GenericApiResponse<Void>> updateCurrentUserProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        userService.updateUserProfile(jwt.getSubject(), request);
        return ResponseEntity.ok(GenericApiResponse.success("User profile updated successfully", null, 200));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('CLIENT_ADMIN')")
    @Operation(
            summary = "Find user by email",
            description = "Retrieves a user profile by email address. Requires admin role.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required", content = @Content)
    })
    public ResponseEntity<GenericApiResponse<UserResponse>> findUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(GenericApiResponse.success("User found", user, 200)))
                .orElse(ResponseEntity.status(404)
                        .body(GenericApiResponse.error("User not found with email: " + email, 404)));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('CLIENT_ADMIN') or #userId == authentication.principal.subject")
    @Operation(
            summary = "Find user by user ID",
            description = "Retrieves a user profile by user ID. Admin can view any user, regular users can only view their own profile.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Cannot view other users", content = @Content)
    })
    public ResponseEntity<GenericApiResponse<UserResponse>> findUserById(@PathVariable String userId) {
        return userService.findByUserId(userId)
                .map(user -> ResponseEntity.ok(GenericApiResponse.success("User found", user, 200)))
                .orElse(ResponseEntity.status(404)
                        .body(GenericApiResponse.error("User not found with id: " + userId, 404)));
    }
}
