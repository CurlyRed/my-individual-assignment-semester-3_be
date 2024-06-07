package Marketplace.business.impl;

import Marketplace.business.ChatService;
import Marketplace.config.security.token.AccessToken;
import Marketplace.domain.Chat;
import Marketplace.persistence.converter.ChatConverter;
import Marketplace.persistence.entity.ChatEntity;
import Marketplace.persistence.entity.ProductEntity;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.persistence.jpaRepository.ChatRepository;
import Marketplace.persistence.jpaRepository.ProductRepository;
import Marketplace.persistence.jpaRepository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatConverter chatConverter;
    private final ProductRepository productRepository;
    private final AccessToken requestAccessToken;

    @Override
    public List<Chat> getChats(long userId) {
        return chatRepository.findByBuyerIdOrSellerId(userId, userId).stream()
                .map(chatConverter::toDomain)
                .toList();
    }

    @Override
    public Chat saveChat(Chat chat){
        UserEntity sender = userRepository.findById(chat.getBuyer().getId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        UserEntity receiver = userRepository.findById(chat.getSeller().getId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
        ProductEntity product = productRepository.findById(chat.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        ChatEntity chatEntity = ChatEntity.builder()
                .buyer(sender)
                .seller(receiver)
                .product(product)
                .createdAt(chat.getCreated_at())
                .build();

        ChatEntity savedEntity = chatRepository.save(chatEntity);

        return chatConverter.toDomain(savedEntity);
    }

    @Override
    public void deleteChat(long chatId) {
        chatRepository.disableChat(chatId);
    }

    @Override
    public void recoverChat(long chatId){
        chatRepository.recoverChat(chatId);
    }
}
