package com.honghung.chatapp.repository; 

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honghung.chatapp.entity.Notification;
import com.honghung.chatapp.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findAllByUser(User user, PageRequest pageRequest);
}
