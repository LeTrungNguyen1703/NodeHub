package com.modulith.auctionsystem.common.web.docs;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameters({
        @Parameter(name = "page", description = "The number of page(0..N)", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
        @Parameter(name = "size", description = "Page size", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")),
        @Parameter(
                name = "sort",
                description = "Sort by(VD: createdAt,desc)",
                in = ParameterIn.QUERY,
                schema = @Schema(type = "array", example = "[\"createdAt,desc\"]"),
                array = @ArraySchema(schema = @Schema(type = "string"))
        )
})
public @interface PageableDocs {
}