package com.modulith.auctionsystem.common.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericApiResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private Object error;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private int statusCode;

    // Success response with data
    public static <T> GenericApiResponse<T> success(T data) {
        return GenericApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Success response with data and custom message
    public static <T> GenericApiResponse<T> success(String message, T data) {
        return GenericApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Success response with data, message, and status code
    public static <T> GenericApiResponse<T> success(String message, T data, int statusCode) {
        return GenericApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Success response without data
    public static <T> GenericApiResponse<T> success(String message) {
        return GenericApiResponse.<T>builder()
                .success(true)
                .message(message)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Error response
    public static <T> GenericApiResponse<T> error(String message, int statusCode) {
        return GenericApiResponse.<T>builder()
                .success(false)
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Error response with details
    public static <T> GenericApiResponse<T> error(String message, Object error, int statusCode) {
        return GenericApiResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

