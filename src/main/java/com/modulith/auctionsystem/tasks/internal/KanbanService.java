package com.modulith.auctionsystem.tasks.internal;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.projects.shared.public_api.ProjectPublicApi;
import com.modulith.auctionsystem.tasks.config.exceptions.KanbanNotFoundException;
import com.modulith.auctionsystem.tasks.domain.entity.Kanban;
import com.modulith.auctionsystem.tasks.domain.repository.KanbanRepository;
import com.modulith.auctionsystem.tasks.shared.dto.CreateKanbanRequest;
import com.modulith.auctionsystem.tasks.shared.dto.KanbanResponse;
import com.modulith.auctionsystem.tasks.shared.dto.UpdateKanbanRequest;
import com.modulith.auctionsystem.tasks.shared.public_api.KanbanPublicAPI;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service("kanbanService")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KanbanService implements KanbanPublicAPI {

    KanbanRepository kanbanRepository;
    ProjectPublicApi projectPublicApi;
    KanbanMapper kanbanMapper;

    @Override
    public PagedResult<KanbanResponse> getKanbanBoards() {
        var kanbans = kanbanRepository.findAllKanbanBoards(Pageable.unpaged());
        return kanbans.map(kanbanMapper::toKanbanResponse);
    }

    @Override
    public PagedResult<KanbanResponse> getKanbanBoardsByProjectId(Integer projectId) {
        // Verify project exists
        projectPublicApi.getProject(projectId);

        var kanbans = kanbanRepository.findKanbanBoardsByProjectId(projectId);
        log.debug("Found {} kanban boards for project id: {}", kanbans.totalElements(), projectId);
        return kanbans.map(kanbanMapper::toKanbanResponse);
    }

    @Override
    public KanbanResponse getKanbanBoardById(Integer kanbanId) {
        var kanban = findByKanbanId(kanbanId);
        return kanbanMapper.toKanbanResponse(kanban);
    }

    @Override
    public KanbanResponse createKanbanBoard(CreateKanbanRequest request, String userId) {
        // Verify project exists
        projectPublicApi.getProject(request.projectId());

        var kanban = kanbanMapper.toKanban(request);
        kanban.setCreatedBy(userId);
        var savedKanban = kanbanRepository.save(kanban);
        log.info("Created new kanban board with id: {} for project: {} by user: {}",
                savedKanban.getKanbanId(), request.projectId(), userId);

        return kanbanMapper.toKanbanResponse(savedKanban);
    }

    @Override
    public KanbanResponse updateKanbanBoard(Integer kanbanId, UpdateKanbanRequest request, String userId) {
        var kanban = findByKanbanId(kanbanId);
        kanbanMapper.updateKanbanFromDto(request, kanban);
        var savedKanban = kanbanRepository.save(kanban);
        log.info("Updated kanban board with id: {} by user: {}", kanbanId, userId);

        return kanbanMapper.toKanbanResponse(savedKanban);
    }

    @Override
    public void deleteKanbanBoard(Integer kanbanId, String userId) {
        var kanban = findByKanbanId(kanbanId);
        kanban.setDeletedAt(Instant.now());
        kanbanRepository.save(kanban);
        log.info("Deleted kanban board with id: {} by user: {}", kanbanId, userId);
    }

    // Helper methods

    private Kanban findByKanbanId(Integer kanbanId) {
        var kanban = kanbanRepository.findByKanbanIdAndDeletedAtIsNull(kanbanId);
        if (kanban.isEmpty()) {
            log.error("Kanban board with id: {} not found", kanbanId);
            throw new KanbanNotFoundException("Kanban board with id " + kanbanId + " not found");
        }
        return kanban.get();
    }
}
