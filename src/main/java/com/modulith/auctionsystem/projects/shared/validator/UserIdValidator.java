package com.modulith.auctionsystem.projects.shared.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserIdValidator implements ConstraintValidator<ValidUserId, String> {

    // TODO: Inject UserService or use ApplicationEventPublisher for cross-module user existence validation
    // Option A: ApplicationEventPublisher to publish UserValidationRequestedEvent
    // Option B: Direct call to UserService in shared package (when available)
    // Option C: Async validation in service layer instead of validator

    @Override
    public void initialize(ValidUserId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {
        if (userId == null || userId.isBlank()) {
            return false;
        }

        // TODO: Implement actual user existence check
        // For now, return true to allow development to proceed
        log.debug("Validating userId: {} (validation not yet implemented)", userId);
        return true;
    }
}
