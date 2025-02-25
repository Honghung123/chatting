package com.honghung.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honghung.chatapp.component.message.Receiver;
import com.honghung.chatapp.constant.AppProperties;
import com.honghung.chatapp.dto.response.PaginationData;
import com.honghung.chatapp.dto.response.SuccessResponseEntity;
import com.honghung.chatapp.dto.response.notification.NotificationResponse;
import com.honghung.chatapp.entity.Notification;
import com.honghung.chatapp.mapper.NotificationMapper;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final RabbitTemplate rabbitTemplate;
    private final AppProperties appProperties;
    private final Receiver receiver;

    @GetMapping("/message")
    public void getMethodName(@RequestParam String m) {
        rabbitTemplate.convertAndSend(appProperties.getRabbitMQ().getTopicExchangeName(), appProperties.getRabbitMQ().getRoutingKey(), m);
        rabbitTemplate.convertAndSend(m);
        try {
            receiver.getLatch().await(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
    

    @GetMapping("/all")
    public SuccessResponseEntity<PaginationData<NotificationResponse>> getLatestNotifications(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        UserPrincipal user = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user == null) return SuccessResponseEntity.from(HttpStatus.UNAUTHORIZED, "Unauthorized");
        Page<Notification> notifications = notificationService.getNotifications(user.getId(), page - 1, size);
        PaginationData<NotificationResponse> notificationsResponses = PaginationData.<NotificationResponse>builder()
                .page(page)
                .pageSize(size)
                .totalElements(notifications.getTotalElements())
                .totalPages(notifications.getTotalPages())
                .data(notifications.getContent().stream().map(NotificationMapper::convertToNotificationResponse).toList()) 
                .build();
        return SuccessResponseEntity.from(HttpStatus.OK, "Get notifications successfully", notificationsResponses);
    }
    

    @PutMapping("/mark-as-read/{id}")
    public SuccessResponseEntity<Void> markAsReadNotification(@PathVariable UUID id) {
        notificationService.markAsRead(id);
        return SuccessResponseEntity.from(HttpStatus.OK, "Mark as read successfully");
    }

    @DeleteMapping("/{id}")
    public SuccessResponseEntity<Void> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return SuccessResponseEntity.from(HttpStatus.NO_CONTENT, "Delete notification successfully");
    }
 
}
