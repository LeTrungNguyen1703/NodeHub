package com.modulith.auctionsystem.users.web.controllers;

import com.modulith.auctionsystem.common.web.response.GenericApiResponse;
import com.modulith.auctionsystem.users.shared.public_api.AuthPublicApi;
import com.modulith.auctionsystem.users.shared.dto.LoginRequest;
import com.modulith.auctionsystem.users.shared.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication via Keycloak")
class AuthController {

    private final AuthPublicApi authPublicApi;

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticate user with username and password via Keycloak and return JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<GenericApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        log.info("Login attempt for user: {}", loginRequest.getUsername());

        LoginResponse loginResponse = authPublicApi.login(loginRequest);

        return ResponseEntity.ok(
                GenericApiResponse.success(
                        "Login successful",
                        loginResponse,
                        200
                )
        );
    }



}

