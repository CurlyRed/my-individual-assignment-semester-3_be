package Marketplace.business.impl;

import Marketplace.business.ChatService;
import Marketplace.business.dto.messaging.MessageRequest;
import Marketplace.business.exception.InvalidRequestException;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.config.security.token.AccessToken;
import Marketplace.domain.Chat;
import Marketplace.domain.Message;
import Marketplace.persistence.converter.ChatConverter;
import Marketplace.persistence.converter.MessageConverter;
import Marketplace.persistence.entity.ChatEntity;
import Marketplace.persistence.entity.MessageEntity;
import Marketplace.persistence.entity.ProductEntity;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.persistence.jpaRepository.ChatRepository;
import Marketplace.persistence.jpaRepository.MessageRepository;
import Marketplace.persistence.jpaRepository.ProductRepository;
import Marketplace.persistence.jpaRepository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ChatConverter chatConverter;

    private final AccessToken requestAccessToken;
    private final MessageConverter messageConverter;
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public Message sendMessage(MessageRequest messageRequest) {
        if (messageRequest == null) {
            throw new InvalidRequestException("Message cannot be null");
        }

        ChatEntity chat = chatRepository.findById(messageRequest.getChatId()).orElse(null);

        if (chat == null) {
            UserEntity buyer = userRepository.findById(messageRequest.getBuyerId()).orElseThrow(() -> new RuntimeException("Buyer not found"));
            UserEntity seller = userRepository.findById(messageRequest.getSellerId()).orElseThrow(() -> new RuntimeException("Seller not found"));
            ProductEntity product = productRepository.findById(messageRequest.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

            chat = ChatEntity.builder()
                    .buyer(buyer)
                    .seller(seller)
                    .product(product)
                    .createdAt(new Date())
                    .deleted(false)
                    .build();
            chat = chatRepository.save(chat);
        }

        MessageEntity message = MessageEntity.builder()
                .chat(chat)
                .sender(userRepository.findById(messageRequest.getSenderId()).orElseThrow(() -> new RuntimeException("Sender not found")))
                .content(messageRequest.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        Message domainMessage = messageConverter.toDomain(message);
        messagingTemplate.convertAndSend("/topic/chat/" + chat.getId(), domainMessage);

        return domainMessage;
    }

    @Override
    public List<Chat> getChatsByUserId(long userId) {
        return chatRepository.findByBuyerIdOrSellerId(userId, userId).stream()
                .map(chatConverter::toDomain)
                .toList();
    }

    @Override
    public void deleteChat(long chatId) {
        ChatEntity chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
        long requesterId = requestAccessToken.getUserId();

        if (chat.getBuyer().getId() != requesterId && chat.getSeller().getId() != requesterId) {
            throw new UnauthorizedDataAccessException("You are not allowed to delete this chat");
        }

        chat.setDeleted(true);
        chatRepository.save(chat);
    }

    @Override
    public void recoverChat(long chatId) {
        ChatEntity chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
        long requesterId = requestAccessToken.getUserId();

        if (chat.getBuyer().getId() != requesterId && chat.getSeller().getId() != requesterId) {
            throw new UnauthorizedDataAccessException("You are not allowed to recover this chat");
        }

        chat.setDeleted(false);
        chatRepository.save(chat);
    }

    @Override
    public List<Message> getMessagesByChatId(long chatId) {
        List<MessageEntity> messages = messageRepository.findByChatId(chatId);
        if (messages.isEmpty()) {
            throw new IllegalArgumentException("Chat does not exist.");
        }
        return messages.stream()
                .map(messageConverter::toDomain)
                .toList();
    }
}
