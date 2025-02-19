package com.honghung.chatapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.honghung.chatapp.entity.Post;
import com.honghung.chatapp.entity.Story;

public interface StoryRepository extends JpaRepository<Story, UUID> {
    
}
