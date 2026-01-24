package com.modulith.auctionsystem.notifications.domain.repository;

import com.modulith.auctionsystem.notifications.domain.entity.Notification;
import com.modulith.auctionsystem.notifications.domain.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserId(String userId);

    List<Notification> findByUserIdAndIsRead(String userId, Boolean isRead);

    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(String userId, Boolean isRead);

    List<Notification> findByUserIdAndType(String userId, NotificationType type);

    long countByUserIdAndIsRead(String userId, Boolean isRead);

    void deleteByUserId(String userId);
}
