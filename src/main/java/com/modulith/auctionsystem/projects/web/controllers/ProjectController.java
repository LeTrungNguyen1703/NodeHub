package com.modulith.auctionsystem.projects.web.controllers;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.common.web.docs.PageableDocs;
import com.modulith.auctionsystem.common.web.response.GenericApiResponse;
import com.modulith.auctionsystem.projects.shared.dto.*;
import com.modulith.auctionsystem.projects.shared.public_api.ProjectPublicApi;
import com.modulith.auctionsystem.projects.shared.validator.IsProjectMember;
import com.modulith.auctionsystem.projects.shared.validator.IsProjectOwner;
import com.modulith.auctionsystem.projects.web.docs.ProjectApiStandardErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management APIs")
class ProjectController {

    private final ProjectPublicApi projectPublicApi;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create a new project",
            description = "Creates a new project with the provided details. The creator automatically becomes a member.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
    })
    @ProjectApiStandardErrors
    public ResponseEntity<GenericApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    ) {
        var userId = jwt.getSubject();

        ProjectResponse response = projectPublicApi.createProject(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericApiResponse.success("Project created successfully", response, 201));
    }

    @GetMapping("/{projectId}")
    @Operation(
            summary = "Get project by ID",
            description = "Retrieves detailed information about a specific project.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
    })
    @ProjectApiStandardErrors
    @IsProjectMember
    public ResponseEntity<GenericApiResponse<ProjectResponse>> getProject(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Integer projectId) {
        var response = projectPublicApi.getProject(projectId);
        return ResponseEntity.ok(GenericApiResponse.success("Project retrieved successfully", response));
    }

    @PutMapping("/{projectId}")
    @IsProjectOwner
    @Operation(
            summary = "Update project",
            description = "Updates an existing project. Only the project owner can perform this action.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
    })
    @ProjectApiStandardErrors
    public ResponseEntity<GenericApiResponse<ProjectResponse>> updateProject(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Integer projectId,
            @Valid @RequestBody UpdateProjectRequest request) {
        var response = projectPublicApi.updateProject(projectId, request);
        return ResponseEntity.ok(GenericApiResponse.success("Project updated successfully", response));
    }

    @DeleteMapping("/{projectId}")
    @IsProjectOwner
    @Operation(
            summary = "Delete project",
            description = "Soft deletes a project. Only the project owner can perform this action.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project deleted successfully"),
    })
    @ProjectApiStandardErrors
    public ResponseEntity<GenericApiResponse<Void>> deleteProject(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Integer projectId) {
        projectPublicApi.deleteProject(projectId);
        return ResponseEntity.ok(GenericApiResponse.success("Project deleted successfully", null));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get projects by user",
            description = "Retrieves a paginated list of projects created by the authenticated user.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
    })
    @ProjectApiStandardErrors
    @PageableDocs
    public ResponseEntity<GenericApiResponse<PagedResult<ProjectResponse>>> getProjects(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @Parameter(hidden = true) Pageable pageable
    ) {
        var userId = jwt.getSubject();
        var response = projectPublicApi.getProjectsByUserId(userId, pageable);
        return ResponseEntity.ok(GenericApiResponse.success("Projects retrieved successfully", response));
    }

    @PostMapping("/{projectId}/members")
    @Operation(
            summary = "Add a member to a project",
            description = "Adds a user as a member to the specified project. Only the project owner can perform this action.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member added successfully"),
            @ApiResponse(responseCode = "409", description = "User is already a member of this project",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
    })
    @ProjectApiStandardErrors
    @IsProjectOwner
    public ResponseEntity<GenericApiResponse<Void>> addProjectMember(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Integer projectId,
            @Valid @RequestBody AddProjectMemberRequest request) {
        projectPublicApi.addProjectMember(projectId, request.userId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericApiResponse.success("Member added successfully", null, 201));
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Remove a member from a project",
            description = "Removes a user from the specified project.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member removed successfully"),
    })
    @ProjectApiStandardErrors
    public ResponseEntity<GenericApiResponse<Void>> removeProjectMember(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Integer projectId,
            @Parameter(description = "User ID to remove", required = true)
            @PathVariable String userId) {
        projectPublicApi.removeProjectMember(new DeleteProjectMemberRequest(projectId, userId));
        return ResponseEntity.ok(GenericApiResponse.success("Member removed successfully", null, 200));
    }
}
