package com.honghung.chatapp.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.honghung.chatapp.constant.ConversationType;
import com.honghung.chatapp.constant.MessageContentType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_messages")
public class ChatMessage implements Serializable{
    private static final long serialVersionUID = 1L; 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long messageId;
    
    // @ManyToOne()
    // @JoinColumn(name = "conversation_id")
    // private Conversation conversation;
    
    @Column(name = "conversation_id")
    private UUID conversationId;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sender_id")
    private User sender;
    
    // @Column(name = "recipient_id")
    // private UUID recipientId;
    
    @Column(name = "content")
    private String content;

    @Column(name = "message_content_type")
    @Enumerated(EnumType.STRING)
    private MessageContentType messageContentType;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id", referencedColumnName = "file_id", unique = true)
    private Attachment attachment;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; 

    // @OneToOne(mappedBy = "latestMessage", fetch = FetchType.LAZY)
    // private Conversation latestMessageOfConversation;
}
