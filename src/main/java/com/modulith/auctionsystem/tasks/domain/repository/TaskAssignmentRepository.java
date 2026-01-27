package com.modulith.auctionsystem.tasks.domain.repository;

import com.modulith.auctionsystem.tasks.domain.entity.TaskAssignment;
import com.modulith.auctionsystem.tasks.domain.entity.TaskAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, TaskAssignmentId> {

    List<TaskAssignment> findByIdTaskId(Integer taskId);

    List<TaskAssignment> findByIdUserId(String userId);

    void deleteByIdTaskIdAndIdUserId(Integer taskId, String userId);
}
