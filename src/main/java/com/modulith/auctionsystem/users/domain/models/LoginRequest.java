package com.modulith.auctionsystem.users.domain.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request containing user credentials")
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Schema(description = "Username", example = "kenshirn", required = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password", example = "kenshirn", required = true)
    private String password;
}
