package com.modulith.auctionsystem.tasks.web.controller;

import com.modulith.auctionsystem.common.web.response.GenericApiResponse;
import com.modulith.auctionsystem.tasks.shared.dto.TaskResponse;
import com.modulith.auctionsystem.tasks.shared.dto.UpdateTaskPositionRequest;
import com.modulith.auctionsystem.tasks.shared.public_api.TaskPublicAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management APIs")
class TaskController {

    private final TaskPublicAPI taskPublicAPI;

    @PatchMapping("/position")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Update task position for Kanban drag-and-drop",
            description = "Updates task position and optionally status when dragging tasks in Kanban board. " +
                    "The position_index uses fractional indexing for efficient reordering.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task position updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not a project member"),
            @ApiResponse(responseCode = "404", description = "Task or project not found")
    })
    public ResponseEntity<GenericApiResponse<TaskResponse>> updateTaskPosition(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Integer projectId,
            @Valid @RequestBody UpdateTaskPositionRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    ) {
        var userId = jwt.getSubject();
        TaskResponse response = taskPublicAPI.updateTaskPosition(projectId, request, userId);
        return ResponseEntity.ok(GenericApiResponse.success("Task position updated successfully", response));
    }
}

