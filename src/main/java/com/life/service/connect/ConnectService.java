package com.life.service.connect;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.life.dto.connect.ConversationDTO;
import com.life.dto.connect.UserSuggestionDTO;
import com.life.entity.User;
import com.life.entity.connect.Conversation;
import com.life.entity.connect.Message;
import com.life.repository.UserRepository;
import com.life.repository.connect.ConversationRepository;
import com.life.repository.connect.MessageRepository;

@Service
public class ConnectService {

	private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ConnectService(
            UserRepository userRepository,
            ConversationRepository conversationRepository,
            MessageRepository messageRepository
    ) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    public List<UserSuggestionDTO> searchUsers(
            Long currentUserId,
            String search
    ) {

        if (search == null || search.trim().isEmpty()) {
            return List.of();
        }

        List<User> users =
                userRepository.searchUsers(
                        currentUserId,
                        search.trim()
                );

        return users.stream()
                .map(user ->
                        new UserSuggestionDTO(
                                user.getId(),
                                user.getUsername(),
                                user.getFirstName(),
                                user.getLastName()
                        )
                )
                .toList();
    }

    public Conversation getOrCreateConversation(
            Long currentUserId,
            Long otherUserId
    ) {

        if (currentUserId.equals(otherUserId)) {
            throw new RuntimeException(
                "You cannot create a conversation with yourself"
            );
        }

        User currentUser =
                userRepository.findById(currentUserId)
                .orElseThrow(() ->
                    new RuntimeException("Current user not found")
                );

        User otherUser =
                userRepository.findById(otherUserId)
                .orElseThrow(() ->
                    new RuntimeException("User not found")
                );

        User userOne;
        User userTwo;

        if (currentUserId < otherUserId) {
            userOne = currentUser;
            userTwo = otherUser;
        } else {
            userOne = otherUser;
            userTwo = currentUser;
        }

        return conversationRepository
                .findByUserOneAndUserTwo(userOne, userTwo)
                .orElseGet(() -> {

                    Conversation conversation =
                            new Conversation();

                    conversation.setUserOne(userOne);
                    conversation.setUserTwo(userTwo);

                    return conversationRepository.save(
                            conversation
                    );
                });
    }

    public ConversationDTO createOrGetConversationDTO(
            Long currentUserId,
            Long otherUserId
    ) {
        Conversation conversation = getOrCreateConversation(currentUserId, otherUserId);
        User otherUser;
        if (conversation.getUserOne().getId().equals(currentUserId)) {
            otherUser = conversation.getUserTwo();
        } else {
            otherUser = conversation.getUserOne();
        }

        Message lastMessage = messageRepository
                .findTopByConversationIdOrderByCreatedAtDesc(conversation.getId())
                .orElse(null);

        return new ConversationDTO(
                conversation.getId(),
                otherUser.getId(),
                otherUser.getUsername(),
                otherUser.getFirstName(),
                otherUser.getLastName(),
                lastMessage != null ? lastMessage.getContent() : "",
                lastMessage != null ? lastMessage.getCreatedAt() : null
        );
    }


    public List<ConversationDTO> getMyConversations(
            Long currentUserId
    ) {

        User currentUser =
                userRepository.findById(currentUserId)
                .orElseThrow(() ->
                    new RuntimeException("User not found")
                );

        List<Conversation> conversations =
                conversationRepository
                .findByUserOneOrUserTwoOrderByUpdatedAtDesc(
                    currentUser,
                    currentUser
                );

        List<ConversationDTO> result =
                new ArrayList<>();

        for (Conversation conversation : conversations) {

            User otherUser;

            if (conversation.getUserOne()
                    .getId()
                    .equals(currentUserId)) {

                otherUser =
                        conversation.getUserTwo();

            } else {

                otherUser =
                        conversation.getUserOne();
            }

            Message lastMessage =
                    messageRepository
                            .findTopByConversationIdOrderByCreatedAtDesc(
                                    conversation.getId()
                            )
                            .orElse(null);

            String lastMessageText =
                    lastMessage != null
                    ? lastMessage.getContent()
                    : "";

            result.add(
                new ConversationDTO(
                    conversation.getId(),
                    otherUser.getId(),
                    otherUser.getUsername(),
                    otherUser.getFirstName(),
                    otherUser.getLastName(),
                    lastMessageText,
                    lastMessage != null
                        ? lastMessage.getCreatedAt()
                        : null
                )
            );
        }

        return result;
    }

    public Long deleteConversation(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Conversation not found"
                ));

        if (!conversation.getUserOne().getId().equals(userId) &&
                !conversation.getUserTwo().getId().equals(userId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "You are not authorized to delete this conversation"
            );
        }

        Long otherUserId = conversation.getUserOne().getId().equals(userId)
                ? conversation.getUserTwo().getId()
                : conversation.getUserOne().getId();

        messageRepository.deleteByConversationId(conversationId);
        conversationRepository.delete(conversation);

        return otherUserId;
    }
}
