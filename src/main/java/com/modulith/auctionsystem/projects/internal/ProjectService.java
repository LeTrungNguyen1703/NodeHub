package com.modulith.auctionsystem.projects.internal;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.projects.config.exceptions.ProjectNotFoundException;
import com.modulith.auctionsystem.projects.domain.entity.Project;
import com.modulith.auctionsystem.projects.domain.repository.ProjectRepository;
import com.modulith.auctionsystem.projects.shared.dto.CreateProjectRequest;
import com.modulith.auctionsystem.projects.shared.dto.DeleteProjectMemberRequest;
import com.modulith.auctionsystem.projects.shared.dto.ProjectResponse;
import com.modulith.auctionsystem.projects.shared.dto.UpdateProjectRequest;
import com.modulith.auctionsystem.projects.shared.public_api.ProjectPublicApi;
import com.modulith.auctionsystem.users.shared.public_api.UserPublicApi;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ProjectService implements ProjectPublicApi {

    ProjectRepository projectRepository;
    ProjectMapper projectMapper;
    UserPublicApi userPublicApi;


    // CRUD operations for Project entity (Aggregate root)

    @Override
    public ProjectResponse createProject(CreateProjectRequest request, String userId) {
        var project = projectMapper.toProject(request);
        project.addMember(userId);
        var savedProject = projectRepository.save(project);
        log.debug("Created new project with id: {} by user: {}", savedProject.getProjectId(), userId);

        return projectMapper.toProjectResponse(savedProject);

    }

    @Override
    public ProjectResponse getProject(Integer projectId) {
        var project = this.findByProjectId(projectId);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Integer projectId, UpdateProjectRequest request) {
        var project = this.findByProjectId(projectId);
        projectMapper.updateProjectFromDto(request, project);
        var savedProject = projectRepository.save(project);
        log.info("Updated project with id: {}", savedProject.getProjectId());
        return projectMapper.toProjectResponse(savedProject);
    }

    @Override
    public void deleteProject(Integer projectId) {
        var project = this.findByProjectId(projectId);
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
        log.info("Deleted project with id: {}", projectId);
    }

    @Override
    public PagedResult<ProjectResponse> getProjectsByUserId(String userId, Pageable pageable) {
        var projects = projectRepository.findAllByCreatedBy(userId, pageable);
        return projects.map(projectMapper::toProjectResponse);
    }


    // CRUD operations for Project Member entity (Child entity)

    @Override
    public void addProjectMember(Integer projectId, String userId) {
        var project = this.findByProjectId(projectId);
        var user = userPublicApi.findByUserId(userId);

        project.addMember(user.userId());
        projectRepository.save(project);
        log.info("Added user with id: {} to project with id: {}", userId, projectId);
    }

    @Override
    public void removeProjectMember(DeleteProjectMemberRequest request) {
        var project = this.findByProjectId(request.projectId());

        project.removeMember(request.userId());
        projectRepository.save(project);
        log.info("Removed user with id: {} from project with id: {}", request.userId(), request.projectId());
    }

    // Helper methods

    private Project findByProjectId(int projectId) {
        var project = projectRepository.findById(projectId);
        if (project.isEmpty() || project.get().getDeletedAt() != null) {
            log.error("Project with id: {} not found", projectId);
            throw new ProjectNotFoundException("Project with id " + projectId + " not found");
        }

        return project.get();
    }

}
