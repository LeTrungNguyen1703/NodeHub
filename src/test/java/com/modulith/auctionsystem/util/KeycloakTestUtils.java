package com.modulith.auctionsystem.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public class KeycloakTestUtils {

    public static final String KEYCLOAK_ID = "keycloak-id-123";

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getMockJwt(String... roles) {
        return jwt()
                .jwt(jwtBuilder -> {
                    jwtBuilder
                            .claim("sub", KEYCLOAK_ID)
                            .claim("email", "siva@style.com")
                            .claim("preferred_username", "siva")
                            .claim("name", "Siva Style");
                })
                .authorities(Arrays.stream(roles)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList()));
    }

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getMockJwtWithoutAuthorities() {
        return jwt()
                .jwt(jwtBuilder -> {
                    jwtBuilder
                            .claim("sub", KEYCLOAK_ID)
                            .claim("email", "siva@style.com")
                            .claim("preferred_username", "siva")
                            .claim("name", "Siva Style");
                });
    }
}