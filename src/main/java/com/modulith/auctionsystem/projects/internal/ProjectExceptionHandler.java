package com.modulith.auctionsystem.projects.internal;

import com.modulith.auctionsystem.projects.config.exceptions.UserAlreadyInProjectException;
import com.modulith.auctionsystem.projects.config.exceptions.UserNotBelongToProjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
class ProjectExceptionHandler {

    @ExceptionHandler(UserAlreadyInProjectException.class)
    ProblemDetail handle(UserAlreadyInProjectException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("User Already In Project");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(UserNotBelongToProjectException.class)
    ProblemDetail handle(UserNotBelongToProjectException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setTitle("User Not Belong To Project");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
