package com.honghung.chatapp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.honghung.chatapp.entity.ChatMessage; 

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAll(); 
    // Optional<ChatMessage> findBySenderIdAndRecipientId(UUID senderId, UUID recipientId);
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage c WHERE c.messageId = :messageId AND c.conversationId = :conversationId")
    int deleteByMessageIdAndConversationId(Long messageId, UUID conversationId);
    // @Query("SELECT c FROM ChatMember c WHERE c.id. = :userId OR c.recipientId = :userId ORDER BY c.created_at DESC")
    // List<UserConversation> getUserConversations(UUID userId);
    List<ChatMessage> findAllByConversationId(UUID conversationId);
}
