package com.modulith.auctionsystem.projects.shared.public_api;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.projects.shared.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ProjectPublicApi {
    //CRUD operations for Project entity (Aggregate root)
    ProjectResponse createProject(CreateProjectRequest request, String userId);

    ProjectResponse getProject(Integer projectId);
    ProjectResponse updateProject(Integer projectId, UpdateProjectRequest request);
    void deleteProject(Integer projectId);
    PagedResult<ProjectResponse> getProjectsByUserId(String userId, Pageable pageable);


    //CRUD operations for Project Member entity (Child entity)
    void addProjectMember(Integer projectId, String userId);
    void removeProjectMember(DeleteProjectMemberRequest request);
    Set<ProjectMemberResponse> getProjectMembersByProjectId(Integer projectId, Pageable pageable);

}
