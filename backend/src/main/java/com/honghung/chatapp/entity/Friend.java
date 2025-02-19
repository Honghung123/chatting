package com.honghung.chatapp.entity;


import java.time.LocalDateTime;

import com.honghung.chatapp.entity.compose_id.FriendId;

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
@Table(name = "friends")
public class Friend {
    @EmbeddedId
    private FriendId id;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
