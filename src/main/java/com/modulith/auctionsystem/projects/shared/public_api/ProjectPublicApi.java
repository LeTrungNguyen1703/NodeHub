package com.modulith.auctionsystem.projects.shared.public_api;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.projects.shared.dto.CreateProjectRequest;
import com.modulith.auctionsystem.projects.shared.dto.DeleteProjectMemberRequest;
import com.modulith.auctionsystem.projects.shared.dto.ProjectResponse;
import com.modulith.auctionsystem.projects.shared.dto.UpdateProjectRequest;
import org.springframework.data.domain.Pageable;

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

}
