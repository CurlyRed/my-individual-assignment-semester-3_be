package Marketplace.business;

import Marketplace.business.dto.messaging.MessageRequest;
import Marketplace.domain.Chat;
import Marketplace.domain.Message;

import java.util.List;

public interface ChatService {
    List<Chat> getChatsByUserId(long userId);
    void deleteChat(long chatId);
    void recoverChat(long chatId);
    void sendMessage(MessageRequest message);
    List<Message> getMessagesByChatId(long chatId);
    Chat createChat(Long buyerId, Long sellerId, Long productId);
}
