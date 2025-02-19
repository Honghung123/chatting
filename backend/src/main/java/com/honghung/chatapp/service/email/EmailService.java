package com.honghung.chatapp.service.email;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface EmailService {
    boolean sendEmail(String receiver, String subject, String content);
    boolean sendEmail(String receiver, String subject, String content, MultipartFile files[]);
}
