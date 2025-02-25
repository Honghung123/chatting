package com.honghung.chatapp.service.notification;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
 
import com.honghung.chatapp.entity.Notification;

@Service
public interface NotificationService {
    void markAsRead(UUID notificationId);
    void deleteNotification(UUID notificationId);
    Page<Notification> getNotifications(UUID userId, Integer page, Integer size);
}
