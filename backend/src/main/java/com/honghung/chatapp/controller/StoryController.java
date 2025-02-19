package com.honghung.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honghung.chatapp.dto.request.post.AddPostRequest;
import com.honghung.chatapp.dto.request.story.AddStoryRequest;
import com.honghung.chatapp.dto.response.PaginationData;
import com.honghung.chatapp.dto.response.SuccessResponseEntity;
import com.honghung.chatapp.dto.response.post.PostResponse;
import com.honghung.chatapp.dto.response.story.StoryResponse;
import com.honghung.chatapp.entity.Post;
import com.honghung.chatapp.entity.Story;
import com.honghung.chatapp.mapper.PostMapper;
import com.honghung.chatapp.mapper.StoryMapper;
import com.honghung.chatapp.service.post.PostService;
import com.honghung.chatapp.service.story.StoryService;
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
@RequestMapping("/story")
public class StoryController {
    private final StoryService storyService;
    @GetMapping("/get-all")
    public SuccessResponseEntity<PaginationData<StoryResponse>> getAllPost(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        Page<Story> story = storyService.getAllStories(page, size); 
        PaginationData<StoryResponse> postResponse = PaginationData.<StoryResponse>builder()
                .page(page)
                .pageSize(size) 
                .totalElements(story.getTotalElements())
                .totalPages(story.getTotalPages())
                .isSorted(story.getSort().isSorted())
                .data(story.map(StoryMapper::convertToPostResponse).getContent())
                .build();
        return SuccessResponseEntity.from(HttpStatus.OK, "Get all story successfully", postResponse);
    }
    
    @PostMapping("/new")
    public SuccessResponseEntity<StoryResponse> addNewPost(@RequestBody AddStoryRequest request) {
        Story story = storyService.addNewStory(request);
        return SuccessResponseEntity.from(HttpStatus.CREATED, "Add new story successfully", StoryMapper.convertToPostResponse(story));
    }
    
}
