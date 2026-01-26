package com.modulith.auctionsystem.notifications.internal.mapper;

import com.modulith.auctionsystem.notifications.domain.entity.Notification;
import com.modulith.auctionsystem.notifications.shared.dto.CreateNotificationRequest;
import com.modulith.auctionsystem.notifications.shared.dto.NotificationResponse;
import com.modulith.auctionsystem.notifications.shared.dto.UpdateNotificationRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface NotificationMapper {

    NotificationResponse toNotificationResponse(Notification notification);

    @Mapping(target = "isRead", constant = "false")
    Notification toNotification(CreateNotificationRequest createNotificationRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNotificationFromDto(UpdateNotificationRequest updateNotificationRequest, @MappingTarget Notification notification);
}
