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
}
