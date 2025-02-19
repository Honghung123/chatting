package com.honghung.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honghung.chatapp.dto.request.post.AddPostRequest;
import com.honghung.chatapp.dto.response.PaginationData;
import com.honghung.chatapp.dto.response.SuccessResponseEntity;
import com.honghung.chatapp.dto.response.post.PostResponse;
import com.honghung.chatapp.entity.Post;
import com.honghung.chatapp.mapper.PostMapper;
import com.honghung.chatapp.service.post.PostService;
import com.honghung.chatapp.utils.JSONUtils;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    @GetMapping("/get-all")
    public SuccessResponseEntity<PaginationData<PostResponse>> getAllPost(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size, @RequestParam(required = false, defaultValue = "newest") String sortBy) {
        Page<Post> post = postService.getAllPost(page, size, sortBy); 
        PaginationData<PostResponse> postResponse = PaginationData.<PostResponse>builder()
                .page(page)
                .pageSize(size) 
                .totalElements(post.getTotalElements())
                .totalPages(post.getTotalPages())
                .isSorted(post.getSort().isSorted())
                .data(post.map(PostMapper::convertToPostResponse).getContent())
                .build();
        return SuccessResponseEntity.from(HttpStatus.OK, "Get all post successfully", postResponse);
    }
    
    @PostMapping("/new")
    public SuccessResponseEntity<PostResponse> addNewPost(@RequestBody AddPostRequest request) {
        Post post = postService.addNewPost(request);
        return SuccessResponseEntity.from(HttpStatus.CREATED, "Add new post successfully", PostMapper.convertToPostResponse(post));
    }
    
}
