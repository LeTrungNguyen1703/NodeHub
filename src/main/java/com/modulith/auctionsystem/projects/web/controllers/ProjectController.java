package com.modulith.auctionsystem.projects.web.controllers;

import com.modulith.auctionsystem.common.web.response.GenericApiResponse;
import com.modulith.auctionsystem.projects.shared.dto.AddProjectMemberRequest;
import com.modulith.auctionsystem.projects.shared.dto.CreateProjectRequest;
import com.modulith.auctionsystem.projects.shared.dto.DeleteProjectMemberRequest;
import com.modulith.auctionsystem.projects.shared.dto.ProjectResponse;
import com.modulith.auctionsystem.projects.shared.public_api.ProjectPublicApi;
import com.modulith.auctionsystem.projects.web.docs.ProductApiStandardErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            description = "Creates a new project with the provided details",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
    })
    @ProductApiStandardErrors
    public ResponseEntity<GenericApiResponse<ProjectResponse>> createProject(@Valid @RequestBody CreateProjectRequest request) {
        ProjectResponse response = projectPublicApi.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericApiResponse.success("Project created successfully", response, 201));
    }

    @PostMapping("/{projectId}/members")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Add a member to a project",
            description = "Adds a user as a member to the specified project",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member added successfully"),
    })
    @ProductApiStandardErrors
    public ResponseEntity<GenericApiResponse<Void>> addProjectMember(
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
            description = "Removes a user from the specified project",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member removed successfully"),
    })
    @ProductApiStandardErrors
    public ResponseEntity<GenericApiResponse<Void>> removeProjectMember(
            @PathVariable Integer projectId,
            @PathVariable String userId) {
        projectPublicApi.removeProjectMember(new DeleteProjectMemberRequest(projectId, userId));
        return ResponseEntity.ok(GenericApiResponse.success("Member removed successfully", null, 200));
    }
}
