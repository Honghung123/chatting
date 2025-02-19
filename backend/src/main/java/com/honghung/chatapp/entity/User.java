package com.honghung.chatapp.entity;   

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore; 

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User implements Serializable{
    private static final long serialVersionUID = 1L; 
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String username;
    private String email; 
    private String password;
    private Boolean status;
    @Column(name = "is_activated")
    private Boolean isActivated;
    private String role;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "avatar_id")
    private String avatarId;
    @Column(name = "cover_url")
    private String coverUrl;
    @Column(name = "cover_id")
    private String coverId;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    // Non-owning side
    @OneToOne(mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonIgnore
    private Notification notificationSender;
    
    // Non-owning side
    @OneToOne(mappedBy = "recipient", fetch = FetchType.LAZY)
    @JsonIgnore
    private Notification notificationRecipient;
  
    // Non-owning side
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ChatMessage> chatMessageSender;
  
    // Non-owning side
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> post;
}