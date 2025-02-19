package com.honghung.chatapp.dto.request.story;

import java.util.List;

import com.honghung.chatapp.dto.request.chat.AttachmentRequest;

public record AddStoryRequest(String caption, List<AttachmentRequest> media) {
    
}
