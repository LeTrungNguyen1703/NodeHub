package com.modulith.auctionsystem.tasks.domain.repository;

import com.modulith.auctionsystem.tasks.domain.entity.TaskDependency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskDependencyRepository extends JpaRepository<TaskDependency, Integer> {

    List<TaskDependency> findByPredecessorTaskTaskId(Integer predecessorTaskId);

    List<TaskDependency> findBySuccessorTaskTaskId(Integer successorTaskId);

    boolean existsByPredecessorTaskTaskIdAndSuccessorTaskTaskId(Integer predecessorTaskId, Integer successorTaskId);
}
