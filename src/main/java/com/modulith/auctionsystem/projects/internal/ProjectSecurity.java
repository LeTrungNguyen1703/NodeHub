package com.modulith.auctionsystem.projects.internal;

import com.modulith.auctionsystem.projects.domain.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
class ProjectSecurity {
    ProjectRepository repository;

    public boolean isProjectOwner(int projectId, Authentication authentication) {
        var projectOptional = repository.findById(projectId);
        if (isAdmin(authentication) || projectOptional.isEmpty()) return true;

        var userId = authentication.getName();
        var isOwner = repository.existsByProjectIdAndCreatedBy(projectId, userId);
        if (isOwner) {
            log.debug("User {} is owner of project {}", userId, projectId);
        } else {
            log.info("User {} is NOT owner of project {}", userId, projectId);
        }
        return isOwner;
    }

    public boolean isProjectMember(int projectId, Authentication authentication) {
        var projectOptional = repository.findById(projectId);
        if (isAdmin(authentication) || projectOptional.isEmpty()) return true;

        var userId = authentication.getName();
        var isMember = repository.existsByMembers_Id_ProjectIdAndMembers_Id_UserId(projectId, userId);
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