package com.honghung.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honghung.chatapp.entity.ChatMember;
import com.honghung.chatapp.entity.compose_id.ChatMemberId;

@Repository
public interface ChatMemberRespository extends JpaRepository<ChatMember, ChatMemberId> {
    
}
