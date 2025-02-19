package com.honghung.chatapp.mapper;

import com.honghung.chatapp.dto.response.chat.AttachmentResponse;
import com.honghung.chatapp.entity.Attachment;
import com.honghung.chatapp.entity.PostAttachment;
import com.honghung.chatapp.entity.StoryAttachment; 

public class AttachmentMapper {
    public static final AttachmentResponse convertToAttachmentResponse(Attachment attachment) {
        return AttachmentResponse.builder()
                .fileId(attachment.getFileId())
                .fileUrl(attachment.getFileUrl())
                .fileName(attachment.getFileName())
                .format(attachment.getFormat())
                .build();
    }
    public static final AttachmentResponse convertToAttachmentResponse(PostAttachment attachment) {
        return AttachmentResponse.builder()
                .fileId(attachment.getFileId())
                .fileUrl(attachment.getFileUrl())
                .fileName(attachment.getFileName())
                .format(attachment.getFormat())
                .build();
    }
    
    public static final AttachmentResponse convertToAttachmentResponse(StoryAttachment attachment) {
        return AttachmentResponse.builder()
                .fileId(attachment.getFileId())
                .fileUrl(attachment.getFileUrl())
                .fileName(attachment.getFileName())
                .format(attachment.getFormat())
                .build();
    }
}
