package com.honghung.chatapp.service.story;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.dto.request.post.AddPostRequest;
import com.honghung.chatapp.dto.request.story.AddStoryRequest;
import com.honghung.chatapp.entity.Post;
import com.honghung.chatapp.entity.PostAttachment;
import com.honghung.chatapp.entity.Story;
import com.honghung.chatapp.entity.StoryAttachment;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.repository.StoryRepository;
import com.honghung.chatapp.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService{
    private final StoryRepository storyRepository;
    private final UserService userService;

    @Override
    public Page<Story> getAllStories(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return storyRepository.findAll(pageRequest);
    }

    @Override
    public Story addNewStory(AddStoryRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(userPrincipal.getId());
        List<StoryAttachment> attachments = request.media().stream().map(attachment -> {
            return StoryAttachment.builder()
                .fileUrl(attachment.fileUrl())
                .fileId(attachment.fileId())
                .fileName(attachment.fileName())
                .format(attachment.format()).build();
            }).toList();
        Story story = Story.builder()
            .caption(request.caption())
            .attachments(attachments)
            .user(user)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return storyRepository.save(story);
    }
}
