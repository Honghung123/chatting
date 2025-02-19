package com.honghung.chatapp.dto.request.post;

import java.util.List;

import com.honghung.chatapp.dto.request.chat.AttachmentRequest;

public record AddPostRequest(String caption, List<AttachmentRequest> media) {
    
}
