package com.honghung.chatapp.service.post;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.dto.request.post.AddPostRequest;
import com.honghung.chatapp.entity.Post;

@Service
public interface PostService {
    Page<Post> getAllPost(int page, int size, String sortBy);

    Post addNewPost(AddPostRequest request);

    Post getPostById(UUID postId);

    void deletePost(UUID id);
}
