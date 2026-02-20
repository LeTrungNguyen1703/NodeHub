package com.modulith.auctionsystem.tasks.domain.repository;

import com.modulith.auctionsystem.common.domain.PageUtils;
import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.tasks.domain.entity.Kanban;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KanbanRepository extends JpaRepository<Kanban, Integer> {

    Optional<Kanban> findByKanbanIdAndDeletedAtIsNull(Integer kanbanId);

    List<Kanban> findByProjectIdAndDeletedAtIsNull(Integer projectId);

    Page<Kanban> findAllByDeletedAtIsNull(Pageable pageable);

    default PagedResult<Kanban> findAllKanbanBoards(Pageable pageable) {
        return new PagedResult<>(findAllByDeletedAtIsNull(PageUtils.makePageable(pageable, "kanbanId")));
    }

    default PagedResult<Kanban> findKanbanBoardsByProjectId(Integer projectId) {
        List<Kanban> kanbans = findByProjectIdAndDeletedAtIsNull(projectId);
        // Create a simple PagedResult from a List (single page with all results)
        return new PagedResult<>(
                kanbans,
                kanbans.size(),
                1,
                1,
                true,
                true,
                false,
                false
        );
    }
}


