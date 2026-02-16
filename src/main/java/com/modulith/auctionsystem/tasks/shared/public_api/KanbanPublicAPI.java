package com.modulith.auctionsystem.tasks.shared.public_api;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.tasks.shared.dto.CreateKanbanRequest;
import com.modulith.auctionsystem.tasks.shared.dto.KanbanResponse;
import com.modulith.auctionsystem.tasks.shared.dto.UpdateKanbanRequest;

/**
 * KanbanPublicAPI exposes Kanban board operations to other modules.
 *
 * Focus on CRUD operations for Kanban boards within projects.
 * NOTE: Implementation required - create a service class annotated with @Service("kanbanService")
 * that implements this interface.
 */
public interface KanbanPublicAPI {

    /**
     * Return a paginated list of Kanban boards visible to the caller.
     */
    PagedResult<KanbanResponse> getKanbanBoards();

    /**
     * Return all Kanban boards for a specific project.
     */
    PagedResult<KanbanResponse> getKanbanBoardsByProjectId(Integer projectId);

    /**
     * Return a single Kanban board by id. Throw not-found exception if missing.
     */
    KanbanResponse getKanbanBoardById(Integer kanbanId);

    /**
     * Create a new Kanban board in the given project and return the created representation.
     */
    KanbanResponse createKanbanBoard(CreateKanbanRequest request, String userId);

    /**
     * Update an existing Kanban board and return the updated representation.
     */
    KanbanResponse updateKanbanBoard(Integer kanbanId, UpdateKanbanRequest request, String userId);

    /**
     * Delete (soft delete) a Kanban board by id.
     */
    void deleteKanbanBoard(Integer kanbanId, String userId);
}