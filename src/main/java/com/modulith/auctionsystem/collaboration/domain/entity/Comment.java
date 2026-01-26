package com.modulith.auctionsystem.collaboration.domain.entity;

import com.modulith.auctionsystem.common.domain.AbstractAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comment_task_id", columnList = "task_id"),
        @Index(name = "idx_comment_user_id", columnList = "user_id")
})
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comment extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @Column(name = "task_id")
    private Integer taskId; // Cross-module reference - no JPA relationship

    @Column(name = "user_id", length = 36)
    private String userId; // Cross-module reference - no JPA relationship

    @NotNull
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
}
