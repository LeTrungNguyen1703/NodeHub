package com.modulith.auctionsystem.projects.internal.service;

import com.modulith.auctionsystem.projects.domain.repository.ProjectRepository;
import com.modulith.auctionsystem.users.shared.public_api.UserPublicApi;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ProjectService {
    ProjectRepository projectRepository;
    UserPublicApi userPublicApi;


}
