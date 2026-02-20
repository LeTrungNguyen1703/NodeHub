package com.modulith.auctionsystem.tasks.internal;

import com.modulith.auctionsystem.projects.config.exceptions.ProjectNotFoundException;
import com.modulith.auctionsystem.projects.config.exceptions.UserAlreadyInProjectException;
import com.modulith.auctionsystem.projects.config.exceptions.UserNotBelongToProjectException;
import com.modulith.auctionsystem.tasks.config.exceptions.KanbanNotFoundException;
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
class KanbanExceptionHandler {

    @ExceptionHandler({KanbanNotFoundException.class})
    public ProblemDetail handleProjectRelatedExceptions(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Kanban Board Not Found");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
