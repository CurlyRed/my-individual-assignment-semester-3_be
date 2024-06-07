package Marketplace.controller;

import Marketplace.business.ChatService;
import Marketplace.business.MessageService;
import Marketplace.domain.Message;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RolesAllowed("USER")
public class ChatController {
    private SimpMessagingTemplate messagingTemplate;
    private MessageService messageService;
    private ChatService chatService;

    @MessageMapping("/message")
    @SendToUser("/topic/messages")
    public void sendMessage(Message message) {
        // Save the message using MessageService
        Message savedMessage = messageService.saveMessage(message);

        // Notify the sender and receiver
        messagingTemplate.convertAndSendToUser(savedMessage.getChat().getBuyer().getUserInformation().getFirstName(), "/topic/messages", savedMessage);
        messagingTemplate.convertAndSendToUser(savedMessage.getChat().getSeller().getUserInformation().getFirstName(), "/topic/messages", savedMessage);
    }
}
