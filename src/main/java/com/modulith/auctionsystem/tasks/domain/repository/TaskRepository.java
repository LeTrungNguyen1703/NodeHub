package com.modulith.auctionsystem.tasks.domain.repository;

import com.modulith.auctionsystem.tasks.domain.entity.Task;
import com.modulith.auctionsystem.tasks.domain.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByProjectId(Integer projectId);

    List<Task> findByAssignedTo(String userId);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByProjectIdAndStatus(Integer projectId, TaskStatus status);

    List<Task> findByProjectIdOrderByDeadlineAsc(Integer projectId);

    List<Task> findByAssignedToAndStatus(String userId, TaskStatus status);

    /**
     * Find tasks by project ID and status, ordered by position index for Kanban display
     */
    List<Task> findByProjectIdAndStatusOrderByPositionIndexAsc(Integer projectId, TaskStatus status);

    /**
     * Find all tasks by project ID, ordered by status and position index for Kanban display
     */
    List<Task> findByProjectIdOrderByStatusAscPositionIndexAsc(Integer projectId);
}
