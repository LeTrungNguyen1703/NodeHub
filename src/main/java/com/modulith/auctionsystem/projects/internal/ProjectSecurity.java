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
        if (isAdmin(authentication)) return true;
        var userId = authentication.getName();
        var isOwner = repository.existsByProjectIdAndCreatedBy(projectId, userId);
        if (isOwner) {
            log.debug("User {} is owner of project {}", userId, projectId);
        } else {
            log.info("User {} is NOT owner of project {}", userId, projectId);
        }
        return isOwner;
    }

    private boolean isAdmin(Authentication authentication) {
        var isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_client_admin"));
        log.debug("Is user admin: {}", isAdmin);
        return isAdmin;
    }
}