package com.honghung.chatapp.dto.response.notification;

import java.time.LocalDateTime;
import java.util.UUID;

import com.honghung.chatapp.model.UserQuickInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private UUID id;
    private String title;
    private String content;
    private boolean isRead;
    private UserQuickInfo user;
    private String type;
    private String targetId;
    private LocalDateTime createdAt;
}
