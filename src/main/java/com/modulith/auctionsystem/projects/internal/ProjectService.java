package com.modulith.auctionsystem.projects.internal;

import com.modulith.auctionsystem.projects.config.exceptions.ProjectNotFoundException;
import com.modulith.auctionsystem.projects.domain.entity.Project;
import com.modulith.auctionsystem.projects.domain.repository.ProjectRepository;
import com.modulith.auctionsystem.projects.shared.dto.CreateProjectRequest;
import com.modulith.auctionsystem.projects.shared.dto.DeleteProjectMemberRequest;
import com.modulith.auctionsystem.projects.shared.dto.ProjectResponse;
import com.modulith.auctionsystem.projects.shared.public_api.ProjectPublicApi;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ProjectService implements ProjectPublicApi {

    ProjectRepository projectRepository;
    ProjectMapper projectMapper;


    // CRUD operations for Project entity (Aggregate root)

    @Override
    public ProjectResponse createProject(CreateProjectRequest request) {
        var project = projectMapper.toProject(request);
        var savedProject = projectRepository.save(project);
        log.info("Created new project with id: {}", savedProject.getProjectId());

        return projectMapper.toProjectResponse(savedProject);

    }

    // CRUD operations for Project Member entity (Child entity)

    @Override
    public void addProjectMember(Integer projectId, String userId) {
        var project = this.findByProjectId(projectId);
        project.addMember(userId);
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
        if (project.isEmpty()) {
            log.error("Project with id: {} not found", projectId);
            throw new ProjectNotFoundException("Project with id " + projectId + " not found");
        }

        return project.get();
    }

}
