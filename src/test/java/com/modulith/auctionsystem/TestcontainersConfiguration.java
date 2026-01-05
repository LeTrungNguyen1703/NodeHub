package com.modulith.auctionsystem;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        JwtDecoder jwtDecoder = mock(JwtDecoder.class);
        // Return a dummy JWT to avoid NullPointerExceptions if decode is called
        when(jwtDecoder.decode(anyString())).thenReturn(
                new Jwt("token", Instant.now(), Instant.now().plusSeconds(30),
                        Map.of("alg", "none"), Map.of("sub", "user"))
        );
        return jwtDecoder;
    }

}
