package com.honghung.chatapp.dto.response.chat;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostAttachmentResponse {
    private String fileId;
    private String fileUrl;
    private String fileName;
    private String format; 
}

