package Marketplace.persistence.converter;

import Marketplace.domain.Chat;
import Marketplace.persistence.entity.ChatEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChatConverter {
    private final UserConverter userConverter;
    private final ProductConverter productConverter;

    public Chat toDomain(ChatEntity chatEntity) {
        if (chatEntity == null) {
            return null;
        }

        return Chat.builder()
                .id(chatEntity.getId())
                .buyer(userConverter.toDomain(chatEntity.getBuyer()))
                .seller(userConverter.toDomain(chatEntity.getSeller()))
                .product(productConverter.toDomain(chatEntity.getProduct()))
                .created_at(chatEntity.getCreatedAt())
                .deleted(chatEntity.getDeleted())
                .build();
    }
}
