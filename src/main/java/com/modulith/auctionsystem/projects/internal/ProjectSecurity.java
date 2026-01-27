package com.modulith.auctionsystem.projects.internal;

import com.modulith.auctionsystem.projects.config.exceptions.ProjectNotFoundException;
import com.modulith.auctionsystem.projects.domain.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
@Transactional(readOnly = true)
class ProjectSecurity {
    ProjectRepository repository;

    public boolean isProjectOwner(int projectId, Authentication authentication) {
        if (isAdmin(authentication)) return true;

        var project = repository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
        var userId = authentication.getName();

        var isOwner = project.getCreatedBy().equals(userId);
        if (isOwner) {
            log.debug("User {} is owner of project {}", userId, projectId);
        } else {
            log.info("User {} is NOT owner of project {}", userId, projectId);
        }
        return isOwner;
    }

    public boolean isProjectMember(int projectId, Authentication authentication) {
        if (isAdmin(authentication)) return true;
        
        var project = repository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
        
        var userId = authentication.getName();

        // Stream is simplified. The projectId check was redundant as these members belong to the project.
        boolean isMember = project.getMembers().stream()
                .anyMatch(projectMember -> projectMember.getId().getUserId().equals(userId));

        if (isMember) {
            log.debug("User {} is member of project {}", userId, projectId);
        } else {
            log.info("User {} is NOT member of project {}", userId, projectId);
        }

        return isMember;
    }

    private boolean isAdmin(Authentication authentication) {
        var isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_client_admin"));
        log.debug("Is user admin: {}", isAdmin);
        return isAdmin;
    }
}