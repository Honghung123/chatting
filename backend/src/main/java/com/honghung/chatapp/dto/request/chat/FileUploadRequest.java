package com.honghung.chatapp.dto.request.chat;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(MultipartFile file, String type) {
    
}
