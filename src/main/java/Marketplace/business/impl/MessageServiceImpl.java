package Marketplace.business.impl;

import Marketplace.business.MessageService;
import Marketplace.domain.Message;
import Marketplace.persistence.converter.MessageConverter;
import Marketplace.persistence.entity.ChatEntity;
import Marketplace.persistence.entity.MessageEntity;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.persistence.jpaRepository.ChatRepository;
import Marketplace.persistence.jpaRepository.MessageRepository;
import Marketplace.persistence.jpaRepository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageConverter messageConverter;

    @Override
    public List<Message> getMessages(long chatId) {
        List<MessageEntity> messageEntities = messageRepository.findByChatId(chatId);
        return messageEntities.stream()
                .map(messageConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Message saveMessage(Message message) {
        UserEntity user = userRepository.findById(message.getSender().getId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        ChatEntity chat = chatRepository.findById(message.getChat().getId())
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        MessageEntity messageEntity = MessageEntity.builder()
                .sender(user)
                .chat(chat)
                .content(message.getContent())
                .timestamp(message.getTimeStamp())
                .build();

        MessageEntity savedMessage = messageRepository.save(messageEntity);

        return messageConverter.toDomain(savedMessage);
    }
}
