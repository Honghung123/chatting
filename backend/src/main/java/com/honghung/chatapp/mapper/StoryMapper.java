package com.honghung.chatapp.mapper;

import com.honghung.chatapp.dto.response.story.StoryResponse;
import com.honghung.chatapp.entity.Story;

public final class StoryMapper {
    public static final StoryResponse convertToPostResponse(Story story) {
        return StoryResponse.builder()
                .id(story.getId())
                .caption(story.getCaption())
                .attachment(story.getAttachments().stream().map(AttachmentMapper::convertToAttachmentResponse).toList())
                .createdAt(story.getCreatedAt())
                .updatedAt(story.getUpdatedAt())
                .user(UserMapper.convertToUserQuickInfo(story.getUser()))
                .totalLikes(10)
                .totalComments(0)
                .totalShares(5)
                .build();
    }
}
