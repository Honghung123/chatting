package com.honghung.chatapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honghung.chatapp.entity.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {
    Optional<Attachment> findByFileId(String fileId);
}
