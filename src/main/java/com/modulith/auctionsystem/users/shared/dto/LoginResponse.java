package com.modulith.auctionsystem.users.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login response containing access token and user information")
public class LoginResponse {

    @JsonProperty("access_token")
    @Schema(description = "JWT access token")
    private String accessToken;

    @JsonProperty("expires_in")
    @Schema(description = "Token expiration time in seconds")
    private Long expiresIn;

    @JsonProperty("refresh_expires_in")
    @Schema(description = "Refresh token expiration time in seconds")
    private Long refreshExpiresIn;

    @JsonProperty("refresh_token")
    @Schema(description = "Refresh token")
    private String refreshToken;

    @JsonProperty("token_type")
    @Schema(description = "Token type (Bearer)")
    private String tokenType;

    @JsonProperty("not-before-policy")
    @Schema(description = "Not before policy")
    private Long notBeforePolicy;

    @JsonProperty("session_state")
    @Schema(description = "Session state")
    private String sessionState;

    @JsonProperty("scope")
    @Schema(description = "Token scope")
    private String scope;
}

