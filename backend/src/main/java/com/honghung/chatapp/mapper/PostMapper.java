package com.honghung.chatapp.mapper;

import com.honghung.chatapp.dto.response.post.PostResponse;
import com.honghung.chatapp.entity.Post;

public final class PostMapper {
    public static final PostResponse convertToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .caption(post.getCaption())
                .attachment(post.getAttachments().stream().map(AttachmentMapper::convertToAttachmentResponse).toList())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .user(UserMapper.convertToUserQuickInfo(post.getUser()))
                .totalLikes(10)
                .totalComments(0)
                .totalShares(5)
                .build();
    }
}
