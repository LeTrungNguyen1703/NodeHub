package com.modulith.auctionsystem.projects.internal;

import com.modulith.auctionsystem.projects.domain.entity.Project;
import com.modulith.auctionsystem.projects.domain.entity.ProjectMember;
import com.modulith.auctionsystem.projects.domain.valueobject.ProjectName;
import com.modulith.auctionsystem.projects.shared.dto.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ProjectMapper {

    @Mapping(target = "name", source = "name.value")
    ProjectResponse toProjectResponse(Project project);

    @Mapping(target = "name", expression = "java(mapProjectName(createProjectRequest.name()))")
    Project toProject(CreateProjectRequest createProjectRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", expression = "java(mapProjectName(updateProjectRequest.name()))")
    void updateProjectFromDto(UpdateProjectRequest updateProjectRequest, @MappingTarget Project project);

    @Mapping(target = "projectId", source = "id.projectId")
    @Mapping(target = "userId", source = "id.userId")
    ProjectMemberResponse toProjectMemberResponse(ProjectMember projectMember);

    List<ProjectResponse> toProjectResponses(List<Project> projects);

    default ProjectName mapProjectName(String name) {
        return name != null ? new ProjectName(name) : null;
    }
}
