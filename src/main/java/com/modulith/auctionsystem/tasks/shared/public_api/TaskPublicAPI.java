package com.modulith.auctionsystem.tasks.shared.public_api;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.tasks.domain.enums.TaskStatus;
import com.modulith.auctionsystem.tasks.shared.dto.CreateTaskRequest;
import com.modulith.auctionsystem.tasks.shared.dto.TaskResponse;
import com.modulith.auctionsystem.tasks.shared.dto.UpdateTaskRequest;

import java.util.Set;

/**
 * TaskPublicAPI exposes the primary task operations used by other modules.
 *
 * <p>Comments here focus on each method's main purpose; parameter details are
 * deliberately omitted to keep the contract concise.
 */
public interface TaskPublicAPI {

    /**
     * Return a paginated list of tasks visible to the caller.
     */
    PagedResult<TaskResponse> getTasks();

    /**
     * Return tasks for a specific project, optionally filtered by status.
     */
    PagedResult<TaskResponse> getTasksByProjectIdAndStatus(Integer projectId, TaskStatus status);

    /**
     * Return a single task by id. Implementations should throw an appropriate
     * not-found exception when the task doesn't exist.
     */
    TaskResponse getTaskById(Integer taskId);

    /**
     * Create a new task in the given project and return the created representation.
     */
    TaskResponse createTask(Integer projectId, CreateTaskRequest request, String userId);

    /**
     * Update an existing task and return the updated representation.
     */
    TaskResponse updateTask(Integer projectId, UpdateTaskRequest request, String userId);

    /**
     * Change the lifecycle status of a task; implementations should validate
     * allowable status transitions.
     */
    void updateTaskStatus(Integer projectId, Integer taskId, TaskStatus status, String userId);

    /**
     * Assign one or more users to the task. Implementations should handle
     * duplicates and missing users gracefully and record audit information.
     */
    void assignTask(Integer projectId, Integer taskId, Set<String> userIds);

    /**
     * Unassign one or more users from the task; ignore non-assigned users and
     * record audit information where appropriate.
     */
    void unassignTask(Integer projectId, Integer taskId, Set<String> userIds);
}