package com.honghung.chatapp.entity.compose_id;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatMemberId implements Serializable {
    @Column(name = "conversation_id")
    private UUID conversationId;
    @Column(name = "user_id")
    private UUID userId;
}
