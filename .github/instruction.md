# Auction System - AI Coding Rules

## 1. Testing Strategy

### Integration Tests
- **Pattern:** `*IT.java` (e.g., `AuctionLifecycleIT.java`)
- **Base:** Extend `BaseIntegrationTest` for Spring Boot + Testcontainers + MockMvc setup
- **Modulith:** Use `@ApplicationModuleTest` for module tests with `Scenario` for event verification
- **Environment:** `@SpringBootTest(webEnvironment = RANDOM_PORT)` via `BaseIntegrationTest`
- **Database:** MySQL/PostgreSQL via Testcontainers, auto-rollback with `@Transactional`
- **Structure:** Group tests with `@Nested` classes, use `@DisplayName` for readability
- **Mocks:** Mock external services (PayOS, Email) as `@Primary` beans in `TestcontainersConfiguration`

### Test Scenarios
- **POST:** 201 Created, verify response, test 400 for invalid input
- **GET:** 200 OK, test pagination (`page`, `size`, `isFirst`, `isLast`)
- **PUT/PATCH:** 200 OK, verify state change
- **DELETE:** 204 No Content, verify deletion

## 2. Authentication & Security

### Keycloak Integration
- **Test Utils:** Use `KeycloakTestUtils.createMockJwt(userId, role)` for mock JWTs
- **User Sync:** Call `userService.syncUserFromKeycloak(jwt)` in `@BeforeEach` when needed
- **Test Coverage:**
    - ✅ Happy Path: Valid JWT + roles → 200/201
    - ❌ Unauthorized: No JWT → 401
    - ❌ Forbidden: Valid JWT, missing role → 403

### Example
```java
String adminJwt = KeycloakTestUtils.createMockJwt("admin123", "client_admin");
String userJwt = KeycloakTestUtils.createMockJwt("user456", "client_user");
```

## 3. Code Organization

### Module Structure
```
auction-system/
├── user/
│   ├── web/controller/       # UserController
│   ├── domain/               # UserService, User entity
│   ├── config/               # UserExceptionHandler
│   └── event/                # UserCreatedEvent
├── auction/
│   ├── web/controller/       # AuctionController, BidController
│   ├── domain/               # AuctionService, Auction, Bid
│   ├── internal/             # TimeExtender, AutoBidProcessor
│   ├── config/               # AuctionExceptionHandler
│   └── event/                # AuctionCompletedEvent, BidPlacedEvent
├── payment/
│   ├── web/controller/       # PaymentController
│   ├── domain/               # PaymentService, Transaction
│   ├── config/               # PaymentExceptionHandler
│   └── event/                # PaymentCompletedEvent
└── notification/
    ├── web/controller/       # NotificationController
    ├── domain/               # NotificationService
    └── event/                # (Event listeners only)
```

### Test Utils
- **Pattern:** `<Module>TestUtils.java` per module
- **Purpose:** Encapsulate test data creation (entities, DTOs, JWTs)
- **Example:** `AuctionTestUtils.createAuctionDto()`, `ProductTestUtils.createProduct()`

## 4. API Documentation (Swagger)

### Controller Level
```java
@Tag(name = "Auctions", description = "Manage auction sessions")
@RestController
@RequestMapping("/api/v1/auctions")
```

### Endpoint Level
```java
@Operation(
    summary = "Create auction",
    description = """
        Creates a new auction session for an approved product.
        Requires client_admin or client_user role.
        """,
    security = @SecurityRequirement(name = "bearer-jwt")
)
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Auction created"),
    @ApiResponse(responseCode = "400", description = "Invalid input"),
    @ApiResponse(responseCode = "401", description = "Not authenticated"),
    @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
    @ApiResponse(responseCode = "404", description = "Product not found")
})
```

### DTOs
```java
@Schema(description = "Request to create an auction")
public record CreateAuctionRequest(
    @Schema(description = "Product ID", example = "123", required = true)
    @NotNull Long productId,
    
    @Schema(description = "Start time", example = "2024-12-01T10:00:00")
    @NotNull LocalDateTime startTime
) {}
```

## 5. Exception Handling

### Module Exception Handler
```java
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AuctionExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(AuctionNotFoundException.class)
    ProblemDetail handleNotFound(AuctionNotFoundException ex) {
        log.debug("Auction not found: {}", ex.getMessage());
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Auction Not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
```

### Exception Mapping
- `*NotFoundException` → 404
- `InvalidRequest*Exception`, Validation errors → 400
- `Insufficient*Exception` → 403
- `PayOS*Exception`, External API errors → 502
- Internal errors → 500

### Logging
- `log.debug()` for 4xx errors
- `log.error()` for 5xx errors

## 6. Validation

### DTO Validation
```java
public record PlaceBidRequest(
    @NotNull(message = "Auction ID is required")
    Long auctionId,
    
    @NotNull(message = "Bid amount is required")
    @Min(value = 1000, message = "Minimum bid is 1,000 VND")
    Long bidAmount
) {}
```

### Controller
```java
@PostMapping("/bids")
public ResponseEntity<GenericApiResponse<BidResponse>> placeBid(
    @Valid @RequestBody PlaceBidRequest request,
    @AuthenticationPrincipal Jwt jwt
) {
    // ...
}
```

## 7. Response Format

### Standard Wrapper
```java
// Success
return ResponseEntity.status(HttpStatus.CREATED)
    .body(GenericApiResponse.success("Bid placed successfully", bidResponse));

// Success with custom status
return ResponseEntity.ok(
    GenericApiResponse.success("Auction retrieved", auctionResponse, 200)
);
```

### Generic Response Structure
```java
public record GenericApiResponse<T>(
    String message,
    T data,
    Integer statusCode,
    LocalDateTime timestamp
) {
    public static <T> GenericApiResponse<T> success(String message, T data) {
        return new GenericApiResponse<>(message, data, 200, LocalDateTime.now());
    }
}
```

## 8. Event-Driven Communication

### Publishing Events
```java
@Service
@Slf4j
public class AuctionService {
    private final ApplicationEventPublisher eventPublisher;
    
    public void completeAuction(Long auctionId) {
        // ... business logic
        eventPublisher.publishEvent(new AuctionCompletedEvent(auction));
        log.debug("Published AuctionCompletedEvent for auction {}", auctionId);
    }
}
```

### Listening to Events
```java
@Service
@Slf4j
public class NotificationService {
    
    @EventListener
    public void onAuctionCompleted(AuctionCompletedEvent event) {
        log.debug("Received AuctionCompletedEvent for auction {}", event.auctionId());
        // Send email to winner/loser
    }
}
```

## 9. Naming Conventions

### Tests
- Class: `AuctionLifecycleIT`, `BidPlacementIT`
- Method: `shouldCreateAuctionSuccessfully()`, `shouldReturn403WhenNotAdmin()`

### Code
- Entity: `Auction`, `Bid`, `Transaction`
- DTO: `CreateAuctionRequest`, `AuctionResponse`
- Service: `AuctionService`, `BidService`
- Event: `AuctionCompletedEvent`, `BidPlacedEvent`

## 10. Tech Stack

- **Java:** 21
- **Framework:** Spring Boot 3.x, Spring Modulith
- **Database:** MySQL (BIGINT for VND currency)
- **Auth:** Keycloak (JWT)
- **Payment:** PayOS
- **Testing:** JUnit 5, Testcontainers, MockMvc
- **Logging:** SLF4J + Lombok `@Slf4j`

## 11. Quick Checklist

When creating a new endpoint:
- [ ] Add `@Operation` with description and security
- [ ] Add `@ApiResponses` for all status codes
- [ ] Validate input with `@Valid` and Jakarta annotations
- [ ] Use `GenericApiResponse` wrapper
- [ ] Create exception handler for domain exceptions
- [ ] Write integration test with auth scenarios
- [ ] Publish events for cross-module communication
- [ ] Add `@Schema` to DTOs