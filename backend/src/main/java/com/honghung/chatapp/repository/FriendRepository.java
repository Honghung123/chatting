package com.honghung.chatapp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.honghung.chatapp.entity.Friend;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.entity.compose_id.FriendId;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId>{
    @Query("SELECT u FROM Friend f, User u WHERE (f.id.userId = :userId AND f.id.userFriendId = u.id ) OR (f.id.userId = u.id AND f.id.userFriendId = :userId)")
    List<User> getUserFriendListByUserId(UUID userId);
}
