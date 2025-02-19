package com.honghung.chatapp.service.notification;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.component.exception.BusinessException;
import com.honghung.chatapp.component.exception.types.NotificationException;
import com.honghung.chatapp.entity.Notification;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.repository.NotificationRepository;
import com.honghung.chatapp.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    @Override
    public Page<Notification> getLatestNotifications(UUID userId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        User user = userService.getUserById(userId);
        return notificationRepository.findAllByRecipient(user, pageRequest);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
            () -> BusinessException.from(NotificationException.NOTIFICATION_NOT_FOUND, "Notification not found")
        );
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
}
