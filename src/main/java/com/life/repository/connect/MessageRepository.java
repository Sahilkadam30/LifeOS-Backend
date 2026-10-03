package com.life.repository.connect;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.connect.Conversation;
import com.life.entity.connect.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(
            Long conversationId
    );

    List<Message> findByConversationIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(
            Long conversationId,
            String content
    );

    Optional<Message> findTopByConversationIdOrderByCreatedAtDesc(
            Long conversationId
    );

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("DELETE FROM Message m WHERE m.conversationId = :conversationId")
    void deleteByConversationId(@org.springframework.data.repository.query.Param("conversationId") Long conversationId);
}