package com.honghung.chatapp.service.notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.component.exception.BusinessException;
import com.honghung.chatapp.component.exception.types.NotificationException;
import com.honghung.chatapp.constant.KafkaTopic;
import com.honghung.chatapp.entity.Notification;
import com.honghung.chatapp.entity.Post;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.repository.NotificationRepository;
import com.honghung.chatapp.service.post.PostService;
import com.honghung.chatapp.service.user.UserService;
import com.honghung.chatapp.utils.JSONUtils;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserService userService;
    private final NotificationRepository notificationRepository;
    private final PostService postService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Page<Notification> getNotifications(UUID userId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        User user = userService.getUserById(userId);
        return notificationRepository.findAllByUser(user, pageRequest);
    }

    @Override
    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> BusinessException.from(NotificationException.NOTIFICATION_NOT_FOUND, "Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @KafkaListener(topics = { KafkaTopic.SEND_POST_NOTIFICATION_TO_USER }, groupId = "send_post_notification_group")
    public void sendPostNotification(String message) {
        System.out.println("------------> Received message from Kafka: " + message);
        Map<String, Object> map = JSONUtils.readJSONString(message);
        UUID userId = UUID.fromString((String) map.get("userId"));
        UUID postId = UUID.fromString((String) map.get("postId"));
        String type = (String) map.get("type");
        User user = userService.getUserById(userId);
        List<User> friendList = userService.getFriendList(userId);
        // Post post = postService.getPostById(postId);
        if (type.equals("upload_post")) {
            String content = user.getName() + " uploaded a new post. View it now!";
            for (User friend : friendList) {
                Notification notification = Notification.builder()
                    .user(friend)
                    .content(content)
                    .title("New post")
                    .targetId(postId.toString())
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .type(type).build();
                Notification savedNotification = notificationRepository.save(notification);
                messagingTemplate.convertAndSend("/queue/notifications/" + friend.getId().toString(), savedNotification);
            }
            System.out.println("Sending notification to friends");
        }
    }

}
