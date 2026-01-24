package com.modulith.auctionsystem.collaboration.domain.repository;

import com.modulith.auctionsystem.collaboration.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByTaskId(Integer taskId);

    List<Comment> findByUserId(String userId);

    List<Comment> findByTaskIdOrderByCreatedAtDesc(Integer taskId);

    List<Comment> findByUserIdOrderByCreatedAtDesc(String userId);

    long countByTaskId(Integer taskId);

    void deleteByTaskId(Integer taskId);
}
