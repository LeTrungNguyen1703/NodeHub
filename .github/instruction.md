# Gemini Rules for Flashcard Backend Project

This document outlines the general rules and guidelines for generating and maintaining code and tests within the `com.modulith.flashcardbe` project.

## 1. General Testing Philosophy

*   **Integration over Unit:** Prioritize integration tests (`*IT.java`) for Controllers to verify the full stack (Controller -> Service -> DB).
*   **Base Class:** Extend `BaseIntegrationTest` for all integration tests to inherit configuration for Spring Boot, Testcontainers, and MockMvc.
*   **Modulith Testing:** Use `@ApplicationModuleTest` for module-specific integration tests (e.g., `DeckModuleTest`) to verify internal module logic and event publication using `Scenario`.
*   **Environment:** Use `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)` for integration tests (handled by `BaseIntegrationTest`).
*   **Containers:** Use `TestcontainersConfiguration` to manage database dependencies (PostgreSQL, etc.) ensuring a clean, isolated environment.
*   **MockMvc:** Use `@AutoConfigureMockMvc` to inject and use `MockMvc` for REST API testing (handled by `BaseIntegrationTest`).
*   **Transactional:** Apply `@Transactional` at the class level for test classes to ensure database rollback after each test (handled by `BaseIntegrationTest`).
*   **Structure:** Use `@Nested` inner classes to group tests by functionality (e.g., `CrudTests`, `SecurityTests`).
*   **Readability:** Use `@DisplayName` to describe the test class and methods in plain English.

## 2. Authentication and Security

*   **Keycloak Simulation:** The application uses Keycloak. Use `KeycloakTestUtils` (in `common` package) to generate mock JWTs.
*   **Role-Based Access:**
    *   Create helper methods in module-specific `TestUtils` (e.g., `DeckTestUtils`) to generate JWTs with specific roles/authorities.
    *   Test **Happy Path**: Request with valid JWT and roles -> 200/201.
    *   Test **Unauthorized**: Request with no JWT -> 401.
    *   Test **Forbidden**: Request with valid JWT but missing roles -> 403.
*   **User Synchronization:** For tests requiring a valid user in the local DB, call `userService.syncUserFromKeycloak(KeycloakTestUtils.createRawJwt())` in the `@BeforeEach` setup.

## 3. Test Data Management

*   **Module TestUtils:** Create a `*TestUtils` class for each module (e.g., `DeckTestUtils`, `UserTestUtils`) to encapsulate data creation logic.
*   **Builders:** Use the Builder pattern for creating DTOs and Entities to keep tests clean.
*   **Avoid Hardcoding:** Prefer using helper methods to generate JSON bodies or objects rather than hardcoded strings, unless testing specific malformed JSON.

## 4. Naming Conventions

*   **Test Classes:** Ends with `IT` (e.g., `DeckLifecycleIT`).
*   **Test Methods:** Use `should[Action][Condition]` format (e.g., `shouldCreateResourceSuccessfully`, `shouldReturn400WhenInvalidData`).

## 5. Standard API Test Scenarios

*   **Create (POST):**
    *   Verify 201 Created.
    *   Verify response body matches created data.
    *   Verify 400 Bad Request for invalid input (validation).
*   **Read (GET):**
    *   Verify 200 OK.
    *   Verify correct data mapping.
    *   **Pagination:** For list endpoints, use `@ParameterizedTest` to verify `page`, `size`, `isFirst`, `isLast`, `hasNext`.
*   **Update (PUT/PATCH):**
    *   Verify 200 OK.
    *   Verify state change.
*   **Delete (DELETE):**
    *   Verify 204 No Content.
    *   Verify resource is gone.

## 6. Swagger/OpenAPI Documentation

*   **Controller Level:**
    *   Use `@Tag(name = "...", description = "...")` to categorize endpoints.
*   **Endpoint Level:**
    *   Use `@Operation` with `summary` and `description` for each endpoint.
    *   Add `security = @SecurityRequirement(name = "bearer-jwt")` for protected endpoints.
    *   Use triple-quoted strings (`"""..."""`) for multi-line descriptions.
*   **Response Documentation:**
    *   Use `@ApiResponses` with `@ApiResponse` for each possible status code:
        *   **200/201**: Success responses
        *   **400**: Bad Request (validation failures)
        *   **401**: Unauthorized (missing authentication)
        *   **403**: Forbidden (insufficient permissions)
        *   **404**: Not Found
        *   **502**: Bad Gateway (external service failures)
    *   Include `@Content` with `@Schema` to specify response types.
*   **Standard Errors:**
    *   Add `@ApiStandardErrors` annotation to include common error responses (500, etc.).
*   **Request Body:**
    *   Use `@io.swagger.v3.oas.annotations.parameters.RequestBody` for detailed request documentation.
    *   Include `@Schema` to specify the DTO class.
*   **Parameters:**
    *   Use `@Parameter(hidden = true)` for `@AuthenticationPrincipal Jwt jwt`.
    *   Add descriptions to path/query parameters with `@Parameter(description = "...")`.
*   **DTO Schemas:**
    *   Add `@Schema(description = "...")` to record classes.
    *   Add `@Schema` annotations to individual fields with `example` and `required` attributes.

## 7. Exception Handling

*   **Module-Specific Handlers:**
    *   Create `<Module>ExceptionHandler` class in `<module>/config/` package.
    *   Extend `ResponseEntityExceptionHandler`.
    *   Use `@RestControllerAdvice` and `@Order(Ordered.HIGHEST_PRECEDENCE)`.
*   **Custom Exceptions:**
    *   Create domain-specific exceptions (e.g., `DeckNotFoundException`, `InvalidAiRequestException`).
    *   Place in `<module>/config/` or `<module>/domain/` package.
*   **Exception Mapping:**
    *   `NotFoundException` → 404
    *   `ValidationException`/`InvalidRequestException` → 400
    *   `ServiceException` (external API failures) → 502
    *   `CacheException`/Internal errors → 500
*   **ProblemDetail:**
    *   Use `ProblemDetail.forStatusAndDetail(status, message)` for RFC 7807 compliance.
    *   Set `title`, `timestamp`, and relevant `properties`.
*   **Logging:**
    *   Use `log.debug()` for client errors (4xx).
    *   Use `log.error()` for server errors (5xx).

## 8. Validation

*   **DTOs:**
    *   Use Jakarta Validation annotations on record fields:
        *   `@NotNull`, `@NotBlank`, `@NotEmpty`
        *   `@Size(min = ..., max = ...)`
        *   `@Email`, `@Pattern`, `@Min`, `@Max`
    *   Add custom messages: `message = "..."`
*   **Controller:**
    *   Use `@Valid` on `@RequestBody` parameters.
    *   Use `@Validated` on class level if needed.
*   **Response:**
    *   Validation errors automatically return 400 with field-level details.

## 9. Response Wrapping

*   **Standard Format:**
    *   Use `GenericApiResponse<T>` for all responses.
    *   Success: `GenericApiResponse.success(message, data)`
    *   Success with status: `GenericApiResponse.success(message, data, statusCode)`
*   **Return Types:**
    *   Use `ResponseEntity<GenericApiResponse<T>>` for typed responses.
    *   Use `ResponseEntity.status(...).body(...)` pattern.

## 10. Code Style & Libraries

*   **Lombok:** Use `@Slf4j` for logging and Lombok annotations for data classes.
*   **Assertions:** Use `MockMvcResultMatchers` (`jsonPath`, `status`) for API assertions.
*   **Java Version:** Project uses Java 21.

## 11. Directory Structure

*   **Source Code:** `src/main/java/com/modulith/flashcardbe/<module>/`
    *   **Controllers:** `<module>/web/controller/`
    *   **Services:** `<module>/domain/` or `<module>/`
    *   **Config/Exceptions:** `<module>/config/`
    *   **DTOs:** `<module>/shared/dtos/` or `<module>/domain/models/`
*   **Tests:** `src/test/java/com/modulith/flashcardbe/<module>/`
    *   **Controller Tests:** `.../<module>/web/controller/`
    *   **Module Utils:** `.../<module>/<Module>TestUtils.java`
