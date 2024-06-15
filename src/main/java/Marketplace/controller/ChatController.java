package Marketplace.controller;

import Marketplace.business.ChatService;
import Marketplace.business.dto.messaging.MessageRequest;
import Marketplace.domain.Chat;
import Marketplace.domain.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/chats")
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/message")
    public ResponseEntity<String> sendMessage(@Payload MessageRequest messageRequest) {
        chatService.sendMessage(messageRequest);
        return ResponseEntity.ok("Message sent.");
    }

    @PostMapping("/create")
    public ResponseEntity<Chat> createChat(@RequestBody MessageRequest messageRequest) {
        Chat newChat = chatService.createChat(
                messageRequest.getBuyerId(),
                messageRequest.getSellerId(),
                messageRequest.getProductId());
        return ResponseEntity.ok(newChat);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getChatsByUserId(@PathVariable long userId) {
        List<Chat> chats = chatService.getChatsByUserId(userId);
        return ResponseEntity.ok(chats);
    }

    @PostMapping("/delete/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable long chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.ok("Chat deleted");
    }

    @PostMapping("/recover/{chatId}")
    public ResponseEntity<String> recoverChat(@PathVariable long chatId) {
        chatService.recoverChat(chatId);
        return ResponseEntity.ok("Chat recovered");
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable long chatId) {
        List<Message> messages = chatService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }
}
