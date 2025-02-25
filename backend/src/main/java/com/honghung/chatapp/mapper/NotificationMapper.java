package com.honghung.chatapp.mapper;

import com.honghung.chatapp.dto.response.notification.NotificationResponse;
import com.honghung.chatapp.entity.Notification;

public final class NotificationMapper {
    public static final NotificationResponse convertToNotificationResponse(Notification notification){
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.getIsRead())
                .user(UserMapper.convertToUserQuickInfo(notification.getUser()))
                .type(notification.getType())
                .targetId(notification.getTargetId())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
