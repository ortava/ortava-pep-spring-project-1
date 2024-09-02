package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Modifying
    int deleteByMessageId(int messageId);

    @Modifying
    @Query("UPDATE Message SET messageText = ?1 WHERE messageId = ?2")
    int updateMessageTextByMessageId(String messageText, int messageId);

    List<Message> findMessagesByPostedBy(int accountId);
}
