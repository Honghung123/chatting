package com.honghung.chatapp.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "story_attachments")
public class StoryAttachment implements Serializable{
    private static final long serialVersionUID = 1L; 
    @Id
    @Column(name = "file_id") 
    private String fileId;
    @Column(name = "file_url")
    private String fileUrl;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "format")
    private String format;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    @JsonIgnore
    private Story story;
}
