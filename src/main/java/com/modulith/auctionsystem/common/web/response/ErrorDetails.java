package com.modulith.auctionsystem.common.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error details")
public class ErrorDetails {

    @Schema(description = "Error code", example = "VALIDATION_ERROR")
    private String code;

    @Schema(description = "Detailed error message", example = "Invalid input provided")
    private String detail;

    @Schema(description = "Field-specific errors for validation failures")
    private Map<String, String> fieldErrors;

    @Schema(description = "List of error messages")
    private List<String> errors;

    // Static factory methods
    public static ErrorDetails of(String code, String detail) {
        return ErrorDetails.builder()
                .code(code)
                .detail(detail)
                .build();
    }

    public static ErrorDetails withFieldErrors(String code, String detail, Map<String, String> fieldErrors) {
        return ErrorDetails.builder()
                .code(code)
                .detail(detail)
                .fieldErrors(fieldErrors)
                .build();
    }

    public static ErrorDetails withErrors(String code, String detail, List<String> errors) {
        return ErrorDetails.builder()
                .code(code)
                .detail(detail)
                .errors(errors)
                .build();
    }
}

