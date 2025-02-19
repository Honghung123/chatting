package com.honghung.chatapp.repository;
 
import java.util.List; 
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.honghung.chatapp.entity.Conversation;
import com.honghung.chatapp.model.UserConversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {  
    @Query("""
        SELECT new com.honghung.chatapp.model.UserConversation(c, u)
        FROM ChatMember cm
        JOIN Conversation c ON cm.id.conversationId = c.id
        JOIN User u ON cm.id.userId = u.id
        WHERE EXISTS (
            SELECT 1 FROM ChatMember cm2
            WHERE cm2.id.conversationId = cm.id.conversationId
            AND cm2.id.userId = :userId
        )
        AND cm.id.userId <> :userId
    """)
    List<UserConversation> getUserConversations(UUID userId);
    
    @Query("""
        SELECT new com.honghung.chatapp.model.UserConversation(null, u)
        FROM Friend f JOIN User u ON 
        (f.id.userFriendId = u.id AND f.id.userId = :userId) 
        OR 
        (f.id.userId = u.id AND f.id.userFriendId = :userId)
        WHERE NOT EXISTS (
            SELECT cm.id.userId
            FROM ChatMember cm 
            WHERE cm.id.userId = u.id
            AND EXISTS (
                SELECT 1 FROM ChatMember cm2
                WHERE cm2.id.conversationId = cm.id.conversationId
                AND cm2.id.userId = :userId
            ) 
        )
    """)
    List<UserConversation> getUserNotInConversations(UUID userId);
}
