package com.honghung.chatapp.service.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honghung.chatapp.constant.ConversationType;
import com.honghung.chatapp.constant.RedisKey;
import com.honghung.chatapp.dto.request.chat.ConversationRequest;
import com.honghung.chatapp.dto.response.chat.ChatMessageResponse;
import com.honghung.chatapp.entity.Attachment;
import com.honghung.chatapp.entity.ChatMember;
import com.honghung.chatapp.entity.ChatMessage; 
import com.honghung.chatapp.entity.Conversation;
import com.honghung.chatapp.entity.User;
import com.honghung.chatapp.entity.compose_id.ChatMemberId;
import com.honghung.chatapp.mapper.ChatMessageMapper;
import com.honghung.chatapp.model.ChatMessageRequest;
import com.honghung.chatapp.repository.AttachmentRepository;
import com.honghung.chatapp.repository.ChatMemberRespository;
import com.honghung.chatapp.repository.ChatRepository;
import com.honghung.chatapp.repository.ConversationRepository;
import com.honghung.chatapp.service.file.MediaFileUploadService;
import com.honghung.chatapp.service.redis.RedisService;
import com.honghung.chatapp.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class ChatServiceImpl implements ChatService {
    private final UserService userService;
    private final RedisService redisService;
    private final MediaFileUploadService mediaFileUploadService;
    private final ChatRepository chatRepository;
    private final ChatMemberRespository chatMemberRespository;
    private final ConversationRepository conversationRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    public void getAllMessages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllMessages'");
    }

    Conversation addNewConversation(UUID senderId, ConversationType conversationType) {
        Conversation conversation = Conversation.builder()
                .conversationType(conversationType)
                .createdBy(senderId)
                .createdAt(LocalDateTime.now())
                .build();
        return conversationRepository.save(conversation);
    } 

    @Override
    public UUID createNewConversation(ConversationRequest request) {
        Conversation conversation = this.addNewConversation(request.userId(), ConversationType.valueOf(request.conversationType()));
        ChatMember chatMember = ChatMember.builder()
            .id(new ChatMemberId(conversation.getId(), request.userId())).build();
        chatMemberRespository.save(chatMember);
        ChatMember chatMember2 = ChatMember.builder()
            .id(new ChatMemberId(conversation.getId(), request.otherUserId())).build();
        chatMemberRespository.save(chatMember2);
        return conversation.getId();
    }

    @Override
    @Transactional
    public ChatMessageResponse addMessage(ChatMessageRequest chatMessage, ConversationType conversationType, Attachment attachment) {
        User sender = userService.getUserByIdNoCached(chatMessage.getSender());
        Conversation conversation = conversationRepository.findById(chatMessage.getConversationId()).orElseThrow(() -> new RuntimeException("Conversation not found"));
        ChatMessage chatMsg = ChatMessage.builder()
                .conversationId(chatMessage.getConversationId()) 
                .content(chatMessage.getContent())
                .sender(sender)
                // .recipientId(chatMessage.getRecipient())
                .messageContentType(chatMessage.getMessageContentType())
                .attachment(attachment)
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();
        ChatMessage savedChatMsg = chatRepository.save(chatMsg);
        conversation.setLatestMessage(savedChatMsg);
        conversationRepository.save(conversation);
        return ChatMessageMapper.convertToChatMessageResponse(savedChatMsg);
    }

    @Override
    @Transactional
    public ChatMessageResponse addMessage(ChatMessageRequest chatMessage, ConversationType conversationType) {
        if(chatMessage.getAttachment() == null) return addMessage(chatMessage, conversationType, null);
        Attachment attachment = Attachment.builder()
                .fileId(chatMessage.getAttachment().fileId())
                .fileName(chatMessage.getAttachment().fileName())
                .fileUrl(chatMessage.getAttachment().fileUrl())
                .format(chatMessage.getAttachment().format() == null ? "Unknown" : chatMessage.getAttachment().format())
                .build();
        Attachment savedAttachment = attachmentRepository.save(attachment);
        return addMessage(chatMessage, conversationType, savedAttachment);
    }

    @Override
    public Long deleteMessage(UUID conversationId, Long messageId, String fileId) {
        int rowEffected = chatRepository.deleteByMessageIdAndConversationId(messageId, conversationId);
        System.out.println("rowEffected: " + rowEffected);
        if (rowEffected == 0) return -1L;
        if(fileId != null) {
            mediaFileUploadService.deleteFile(fileId);
        };
        return messageId;
    }

    @Override
    public void handleUpdateUserOnlineStatus(UUID userId) {
        redisService.setValue(RedisKey.USER_ONLINE_STATUS + ":" + userId, true);
        redisService.setTimeToLive(RedisKey.USER_ONLINE_STATUS + ":" + userId, 5, TimeUnit.MINUTES);
    }

    @Override
    public List<ChatMessageResponse> getConversationMessages(UUID conversationId) {
        List<ChatMessage> messages = chatRepository.findAllByConversationId(conversationId);
        return messages.stream().map(ChatMessageMapper::convertToChatMessageResponse).toList();
    }

    @RabbitListener(queues = "chatapp_queue")
       public void receiveMessage(String message) {
           System.out.println("Received Message: " + message);
       }
}
