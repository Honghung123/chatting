package com.honghung.chatapp.entity;


import java.io.Serializable;

import com.honghung.chatapp.entity.compose_id.ChatMemberId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_members")
public class ChatMember implements Serializable{
    private static final long serialVersionUID = 1L; 
    @EmbeddedId
    private ChatMemberId id;
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;
}
