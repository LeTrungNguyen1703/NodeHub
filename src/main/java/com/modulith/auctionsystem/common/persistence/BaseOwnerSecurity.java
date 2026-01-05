package com.modulith.auctionsystem.common.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public abstract class BaseOwnerSecurity<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    protected abstract String getEntityId(T entity);

    public boolean isOwner(ID entityId, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return true;

        String currentUserId = authentication.getName();

        return getRepository().findById(entityId)
                .map(entity -> {
                    String ownerId = getEntityId(entity);
                    return ownerId.equals(currentUserId);
                })
                .orElse(false);
    }
}
