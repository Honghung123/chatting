package com.honghung.chatapp.repository;  

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.honghung.chatapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("""
        SELECT u 
        FROM Friend f 
        JOIN User u ON (f.id.userFriendId = u.id AND f.id.userId = :userId) 
                    OR (f.id.userId = u.id AND f.id.userFriendId = :userId)
        LEFT JOIN ChatMember cm1 ON cm1.id.userId = :userId
        LEFT JOIN ChatMember cm2 ON cm2.id.userId = u.id AND cm1.id.conversationId = cm2.id.conversationId
        WHERE cm2.id IS NULL
    """)
    List<User> getUserFriendListNotInConversationByUserId(UUID userId);

    @Query("""
            SELECT u
            FROM User u
            WHERE u NOT IN (
                SELECT u 
                FROM Friend f 
                JOIN User u ON (f.id.userFriendId = u.id AND f.id.userId = :userId) 
                            OR (f.id.userId = u.id AND f.id.userFriendId = :userId)
            ) AND u.id <> :userId
            """)
    Page<User> getSuggestUserList(UUID userId, Pageable pageable);
}
