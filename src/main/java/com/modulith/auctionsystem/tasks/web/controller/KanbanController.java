package com.modulith.auctionsystem.tasks.web.controller;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.common.web.docs.PageableDocs;
import com.modulith.auctionsystem.common.web.response.GenericApiResponse;
import com.modulith.auctionsystem.projects.shared.validator.IsProjectMember;
import com.modulith.auctionsystem.tasks.shared.dto.CreateKanbanRequest;
import com.modulith.auctionsystem.tasks.shared.dto.KanbanResponse;
import com.modulith.auctionsystem.tasks.shared.dto.TaskResponse;
import com.modulith.auctionsystem.tasks.shared.dto.UpdateKanbanRequest;
import com.modulith.auctionsystem.tasks.shared.public_api.KanbanPublicAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kanbans")
@RequiredArgsConstructor
@Tag(name = "Kanban Boards", description = "Kanban board management APIs")
class KanbanController {

    private final KanbanPublicAPI kanbanPublicAPI;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create a new Kanban board",
            description = "Creates a new Kanban board for a project. User must be authenticated.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kanban board created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<GenericApiResponse<KanbanResponse>> createKanbanBoard(
            @Valid @RequestBody CreateKanbanRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    ) {
        var userId = jwt.getSubject();
        KanbanResponse response = kanbanPublicAPI.createKanbanBoard(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericApiResponse.success("Kanban board created successfully", response, 201));
    }

    @GetMapping("/{kanbanId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get Kanban board by ID",
            description = "Retrieves detailed information about a specific Kanban board.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kanban board retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Kanban board not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<GenericApiResponse<KanbanResponse>> getKanbanBoard(
            @Parameter(description = "Kanban board ID", required = true, example = "1")
            @PathVariable Integer kanbanId) {
        var response = kanbanPublicAPI.getKanbanBoardById(kanbanId);
        return ResponseEntity.ok(GenericApiResponse.success("Kanban board retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get all Kanban boards",
            description = "Retrieves a paginated list of all Kanban boards visible to the user.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kanban boards retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PageableDocs
    public ResponseEntity<GenericApiResponse<PagedResult<KanbanResponse>>> getAllKanbanBoards(
            @Parameter(hidden = true) Pageable pageable) {
        var response = kanbanPublicAPI.getKanbanBoards(pageable);
        return ResponseEntity.ok(GenericApiResponse.success("Kanban boards retrieved successfully", response));
    }

    @GetMapping("/project/{projectId}")
    @IsProjectMember
    @Operation(
            summary = "Get Kanban boards by project ID",
            description = "Retrieves all Kanban boards for a specific project. User must be a project member.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kanban boards retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not a project member"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PageableDocs
    public ResponseEntity<GenericApiResponse<PagedResult<KanbanResponse>>> getKanbanBoardsByProject(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Integer projectId,
            @Parameter(hidden = true) Pageable pageable) {
        var response = kanbanPublicAPI.getKanbanBoardsByProjectId(projectId, pageable);
        return ResponseEntity.ok(GenericApiResponse.success("Kanban boards retrieved successfully", response));
    }

    @GetMapping("/{kanbanId}/tasks")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get tasks by Kanban board",
            description = "Retrieves all tasks associated with a specific Kanban board, ordered by status and position.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Kanban board not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PageableDocs
    public ResponseEntity<GenericApiResponse<PagedResult<TaskResponse>>> getTasksByKanbanBoard(
            @Parameter(description = "Kanban board ID", required = true, example = "1")
            @PathVariable Integer kanbanId,
            @Parameter(hidden = true) Pageable pageable) {
        var response = kanbanPublicAPI.getTasksByKanbanBoard(kanbanId, pageable);
        return ResponseEntity.ok(GenericApiResponse.success("Tasks retrieved successfully", response));
    }

    @PutMapping("/{kanbanId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Update Kanban board",
            description = "Updates an existing Kanban board. User must be authenticated.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kanban board updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Kanban board not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<GenericApiResponse<KanbanResponse>> updateKanbanBoard(
            @Parameter(description = "Kanban board ID", required = true, example = "1")
            @PathVariable Integer kanbanId,
            @Valid @RequestBody UpdateKanbanRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    ) {
        var userId = jwt.getSubject();
        var response = kanbanPublicAPI.updateKanbanBoard(kanbanId, request, userId);
        return ResponseEntity.ok(GenericApiResponse.success("Kanban board updated successfully", response));
    }

    @DeleteMapping("/{kanbanId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Delete Kanban board",
            description = "Deletes (soft delete) a Kanban board. User must be authenticated.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kanban board deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Kanban board not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<GenericApiResponse<Void>> deleteKanbanBoard(
            @Parameter(description = "Kanban board ID", required = true, example = "1")
            @PathVariable Integer kanbanId,
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    ) {
        var userId = jwt.getSubject();
        kanbanPublicAPI.deleteKanbanBoard(kanbanId, userId);
        return ResponseEntity.ok(GenericApiResponse.success("Kanban board deleted successfully", null));
    }
}


