package com.honghung.chatapp.service.notification;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
 
import com.honghung.chatapp.entity.Notification;

@Service
public interface NotificationService {
    void markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
    Page<Notification> getLatestNotifications(UUID userId, Integer page, Integer size);
}
