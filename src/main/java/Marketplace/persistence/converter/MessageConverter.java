package Marketplace.persistence.converter;

import Marketplace.domain.Message;
import Marketplace.persistence.entity.MessageEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageConverter {
    private final UserConverter userConverter;
    private final ChatConverter chatConverter;

    public Message toDomain(MessageEntity messageEntity) {
        if (messageEntity == null) {
            return null;
        }

        return Message.builder()
                .id(messageEntity.getId())
                .sender(userConverter.toDomain(messageEntity.getSender()))
                .content(messageEntity.getContent())
                .timeStamp(messageEntity.getTimestamp())
                .chat(chatConverter.toDomain(messageEntity.getChat()))
                .build();
    }
}
