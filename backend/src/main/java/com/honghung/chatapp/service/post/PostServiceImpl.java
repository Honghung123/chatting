package com.honghung.chatapp.service.post; 

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.dto.request.post.AddPostRequest;
import com.honghung.chatapp.entity.Post;
import com.honghung.chatapp.entity.PostAttachment;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.repository.PostRepository;
import com.honghung.chatapp.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public Page<Post> getAllPost(int page, int size, String sortBy) {
        Sort sort = sortBy.equals("newest") ? Sort.by("createdAt").descending() : Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        return postRepository.findAll(pageRequest);
    }

    @Override
    public Post addNewPost(AddPostRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(userPrincipal.getId());
        List<PostAttachment> attachments = request.media().stream().map(attachment -> {
            return PostAttachment.builder()
                .fileUrl(attachment.fileUrl())
                .fileId(attachment.fileId())
                .fileName(attachment.fileName())
                .format(attachment.format()).build();
            }).toList();
        Post post = Post.builder()
            .caption(request.caption())
            .attachments(attachments)
            .user(user)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return postRepository.save(post);
    }
}
