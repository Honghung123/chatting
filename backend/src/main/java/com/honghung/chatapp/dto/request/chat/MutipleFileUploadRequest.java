package com.honghung.chatapp.dto.request.chat;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record MutipleFileUploadRequest(List<MultipartFile> files, List<String> types) {
    
}
