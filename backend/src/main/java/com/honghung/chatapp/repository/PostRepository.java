package com.honghung.chatapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.honghung.chatapp.entity.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {
    
}
