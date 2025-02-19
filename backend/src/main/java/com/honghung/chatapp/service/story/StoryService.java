package com.honghung.chatapp.service.story;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.dto.request.post.AddPostRequest;
import com.honghung.chatapp.dto.request.story.AddStoryRequest;
import com.honghung.chatapp.entity.Post;
import com.honghung.chatapp.entity.Story;

@Service
public interface StoryService {
    Page<Story> getAllStories(int page, int size);

    Story addNewStory(AddStoryRequest request);
}
