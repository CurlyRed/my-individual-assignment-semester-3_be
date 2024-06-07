package Marketplace.business;

import Marketplace.domain.Chat;

import java.util.List;

public interface ChatService {
    List<Chat> getChatsById(long userId);

}
