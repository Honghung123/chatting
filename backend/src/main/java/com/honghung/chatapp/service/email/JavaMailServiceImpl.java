package com.honghung.chatapp.service.email;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.honghung.chatapp.constant.KafkaTopic;
import com.honghung.chatapp.utils.JSONUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL")
public class JavaMailServiceImpl implements EmailService {
    @Value("${spring.mail.from}")
    private String emailFrom;
    private final JavaMailSender javaMailSender;

    @Override
    public boolean sendEmail(String receiver, String subject, String content) {
        var message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(content);
        message.setSentDate(new Date(System.currentTimeMillis()));
        javaMailSender.send(message);
        return true;
    }

    @Override
    public boolean sendEmail(String recipients, String subject, String content, MultipartFile[] files) {

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(emailFrom, "Chat App Support Team");

            if (recipients.equals("*")) {
                System.out.println("Sending email to all users");
            } else if (recipients.contains(",")) { // send to multiple users
                helper.setTo(InternetAddress.parse(recipients));
            } else { // send to single user
                helper.setTo(recipients);
            }
            // Send attach files
            if (files != null) {
                for (MultipartFile file : files) {
                    helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
                }
            }
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(message);

            log.info("Email has sent to successfully, recipients: {}", recipients);
            return true;
        } catch (MessagingException e) {
            log.error("Failed to format email to sent. Error message: {}", e.getLocalizedMessage(), e);
            return false;
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode content. Error message: {}", e.getLocalizedMessage(), e);
            return false;
        } catch (Exception e) {
            log.error("Failed to send email. Error message: {}", e.getLocalizedMessage(), e);
            return false;
        }

    }

    @KafkaListener(topics = { KafkaTopic.SEND_CONFIRM_ACCOUNT_LINK_VIA_EMAIL }, groupId = "send_email_group")
    public void sendConfirmAccountLinkViaEmail(String message) {
        System.out.println("------------> Received message from Kafka: " + message);
        Map<String, Object> map = JSONUtils.readJSONString(message);
        String receiver = (String) map.get("receiver");
        String subject = (String) map.get("subject");
        String content = (String) map.get("content");
        sendEmail(receiver, subject, content, null); 
    }
}
