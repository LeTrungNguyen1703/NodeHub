package com.modulith.auctionsystem.tasks.internal;

import com.modulith.auctionsystem.common.models.PagedResult;
import com.modulith.auctionsystem.tasks.domain.enums.TaskStatus;
import com.modulith.auctionsystem.tasks.shared.dto.CreateTaskRequest;
import com.modulith.auctionsystem.tasks.shared.dto.TaskResponse;
import com.modulith.auctionsystem.tasks.shared.dto.UpdateTaskPositionRequest;
import com.modulith.auctionsystem.tasks.shared.dto.UpdateTaskRequest;
import com.modulith.auctionsystem.tasks.shared.public_api.TaskPublicAPI;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class TaskService implements TaskPublicAPI {


    @Override
    public PagedResult<TaskResponse> getTasks() {
        return null;
    }

    @Override
    public PagedResult<TaskResponse> getTasksByProjectIdAndStatus(Integer projectId, TaskStatus status) {
        return null;
    }

    @Override
    public TaskResponse getTaskById(Integer taskId) {
        return null;
    }

    @Override
    public TaskResponse createTask(Integer projectId, CreateTaskRequest request, String userId) {
        return null;
    }

    @Override
    public TaskResponse updateTask(Integer projectId, UpdateTaskRequest request, String userId) {
        return null;
    }

    @Override
    public void updateTaskStatus(Integer projectId, Integer taskId, TaskStatus status, String userId) {

    }

    @Override
    public void assignTask(Integer projectId, Integer taskId, Set<String> userIds) {

    }

    @Override
    public void unassignTask(Integer projectId, Integer taskId, Set<String> userIds) {

    }

    @Override
    public TaskResponse updateTaskPosition(Integer projectId, UpdateTaskPositionRequest request, String userId) {
        return null;
    }
}
