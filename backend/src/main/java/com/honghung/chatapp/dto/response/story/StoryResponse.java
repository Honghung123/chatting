package com.honghung.chatapp.dto.response.story;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.honghung.chatapp.dto.response.chat.AttachmentResponse;
import com.honghung.chatapp.model.UserQuickInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoryResponse {
    private UUID id;
    private String caption;
    private List<AttachmentResponse> attachment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserQuickInfo user;
    private long totalLikes;
    private long totalComments;
    private long totalShares;
}
