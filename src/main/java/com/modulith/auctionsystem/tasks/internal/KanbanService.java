package com.modulith.auctionsystem.tasks.internal;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.tasks.shared.dto.CreateKanbanRequest;
import com.modulith.auctionsystem.tasks.shared.dto.KanbanResponse;
import com.modulith.auctionsystem.tasks.shared.dto.UpdateKanbanRequest;
import com.modulith.auctionsystem.tasks.shared.public_api.KanbanPublicAPI;
import com.modulith.auctionsystem.tasks.shared.public_api.TaskPublicAPI;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KanbanService implements KanbanPublicAPI {

    TaskPublicAPI taskPublicAPI;
    KanbanMapper kanbanMapper;

    @Override
    public PagedResult<KanbanResponse> getKanbanBoards() {
        return null;
    }

    @Override
    public PagedResult<KanbanResponse> getKanbanBoardsByProjectId(Integer projectId) {
        return null;
    }

    @Override
    public KanbanResponse getKanbanBoardById(Integer kanbanId) {
        return null;
    }

    @Override
    public KanbanResponse createKanbanBoard(CreateKanbanRequest request, String userId) {
        return null;
    }

    @Override
    public KanbanResponse updateKanbanBoard(Integer kanbanId, UpdateKanbanRequest request, String userId) {
        return null;
    }

    @Override
    public void deleteKanbanBoard(Integer kanbanId, String userId) {

    }
}
