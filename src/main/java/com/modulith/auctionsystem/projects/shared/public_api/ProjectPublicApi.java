package com.modulith.auctionsystem.projects.shared.public_api;

import com.modulith.auctionsystem.projects.shared.dto.CreateProjectRequest;
import com.modulith.auctionsystem.projects.shared.dto.DeleteProjectMemberRequest;
import com.modulith.auctionsystem.projects.shared.dto.ProjectResponse;

public interface ProjectPublicApi {
    //CRUD operations for Project entity (Aggregate root)
    ProjectResponse createProject(CreateProjectRequest request);


    //CRUD operations for Project Member entity (Child entity)
    void addProjectMember(Integer projectId, String userId);
    void removeProjectMember(DeleteProjectMemberRequest request);
}
