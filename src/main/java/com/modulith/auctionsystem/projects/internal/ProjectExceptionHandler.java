package com.modulith.auctionsystem.projects.internal;

import com.modulith.auctionsystem.projects.config.exceptions.ProjectNotFoundException;
import com.modulith.auctionsystem.projects.config.exceptions.UserAlreadyInProjectException;
import com.modulith.auctionsystem.projects.config.exceptions.UserNotBelongToProjectException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@Order(Ordered.HIGHEST_PRECEDENCE)
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

    @ExceptionHandler(ProjectNotFoundException.class)
    ProblemDetail handle(ProjectNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Project Not Found");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
