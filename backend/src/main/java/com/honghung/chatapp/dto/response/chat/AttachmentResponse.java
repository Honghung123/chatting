package com.honghung.chatapp.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentResponse {
    private String fileId;
    private String fileUrl;
    private String fileName;
    private String format;
}
