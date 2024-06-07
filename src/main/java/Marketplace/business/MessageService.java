package Marketplace.business;

import Marketplace.domain.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessages(long chatId);
    Message saveMessage(Message message);
}
