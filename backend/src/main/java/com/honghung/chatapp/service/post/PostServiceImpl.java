package com.honghung.chatapp.service.post; 

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.honghung.chatapp.component.exception.BusinessException;
import com.honghung.chatapp.component.exception.types.PostException;
import com.honghung.chatapp.constant.KafkaTopic;
import com.honghung.chatapp.dto.request.post.AddPostRequest;
import com.honghung.chatapp.entity.Post;
import com.honghung.chatapp.entity.PostAttachment;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.model.UserPrincipal;
import com.honghung.chatapp.repository.PostRepository;
import com.honghung.chatapp.service.user.UserService;
import com.honghung.chatapp.utils.JSONUtils;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Cacheable(value = "post", key = "#postId")
    public Post getPostById(UUID postId) {
        return postRepository.findById(postId).orElseThrow(() -> BusinessException.from(PostException.POST_NOT_FOUND, "Post not found"));
    }

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
        Post savedPost = postRepository.save(post);
        Map<String, Object> map = Map.of("type", "upload_post", "userId", user.getId(), "postId", savedPost.getId());
        String message = JSONUtils.convertToJSON(map);
        kafkaTemplate.send(KafkaTopic.SEND_POST_NOTIFICATION_TO_USER, message);
        System.out.println("Send message to Kafka: " + message);
        return savedPost;
    }

    @Override
    public void deletePost(UUID id) {
        postRepository.deleteById(id);
    }
}
