package Marketplace.business.impl;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatServiceImplTest {

    @Mock
    private ChatRepository chatRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatConverter chatConverter;
    @Mock
    private MessageConverter messageConverter;
    @Mock
    private AccessToken requestAccessToken;
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @InjectMocks
    private ChatServiceImpl chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSendMessage_withNullRequest_shouldThrowInvalidRequestException() {
        // When & Then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> chatService.sendMessage(null));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        // Verify
        verifyNoInteractions(chatRepository, messageRepository, userRepository, messagingTemplate);
    }

    @Test
    void testSendMessage_withNonExistentChat_shouldThrowRuntimeException() {
        // Given
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setChatId(1L);

        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> chatService.sendMessage(messageRequest));
        assertEquals("Chat not found", exception.getMessage());

        // Verify
        verify(chatRepository, times(1)).findById(1L);
        verifyNoInteractions(messageRepository, userRepository, messagingTemplate);
    }

    @Test
    void testSendMessage_withNonExistentSender_shouldThrowRuntimeException() {
        // Given
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setChatId(1L);
        messageRequest.setSenderId(1L);

        ChatEntity chatEntity = new ChatEntity();
        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> chatService.sendMessage(messageRequest));
        assertEquals("Sender not found", exception.getMessage());

        // Verify
        verify(chatRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verifyNoInteractions(messageRepository, messagingTemplate);
    }

    @Test
    void testSendMessage_withValidRequest_shouldSendMessage() {
        // Given
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setChatId(1L);
        messageRequest.setSenderId(1L);
        messageRequest.setContent("Hello");

        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(1L);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        MessageEntity messageEntity = MessageEntity.builder()
                .chat(chatEntity)
                .sender(userEntity)
                .content("Hello")
                .timestamp(LocalDateTime.now())
                .build();
        Message message = new Message();

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(messageRepository.save(any(MessageEntity.class))).thenReturn(messageEntity);
        when(messageConverter.toDomain(any(MessageEntity.class))).thenReturn(message);

        // When
        chatService.sendMessage(messageRequest);

        // Verify
        verify(chatRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(messageRepository, times(1)).save(any(MessageEntity.class));
        verify(messageConverter, times(1)).toDomain(any(MessageEntity.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/chat/1"), eq(message));
    }

    @Test
    void testCreateChat_withNonExistentBuyer_shouldThrowRuntimeException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> chatService.createChat(1L, 2L, 3L));
        assertEquals("Buyer not found", exception.getMessage());

        // Verify
        verify(userRepository, times(1)).findById(1L);
        verifyNoInteractions(productRepository, chatRepository, chatConverter);
    }

    @Test
    void testCreateChat_withNonExistentSeller_shouldThrowRuntimeException() {
        // Given
        UserEntity buyer = new UserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> chatService.createChat(1L, 2L, 3L));
        assertEquals("Seller not found", exception.getMessage());

        // Verify
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verifyNoInteractions(productRepository, chatRepository, chatConverter);
    }

    @Test
    void testCreateChat_withNonExistentProduct_shouldThrowRuntimeException() {
        // Given
        UserEntity buyer = new UserEntity();
        UserEntity seller = new UserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(seller));
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> chatService.createChat(1L, 2L, 3L));
        assertEquals("Product not found", exception.getMessage());

        // Verify
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(productRepository, times(1)).findById(3L);
        verifyNoInteractions(chatRepository, chatConverter);
    }

    @Test
    void testCreateChat_withValidRequest_shouldReturnChat() {
        // Given
        UserEntity buyer = new UserEntity();
        UserEntity seller = new UserEntity();
        ProductEntity product = new ProductEntity();
        ChatEntity chatEntity = new ChatEntity();
        Chat chat = new Chat();

        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(seller));
        when(productRepository.findById(3L)).thenReturn(Optional.of(product));
        when(chatRepository.save(any(ChatEntity.class))).thenReturn(chatEntity);
        when(chatConverter.toDomain(chatEntity)).thenReturn(chat);

        // When
        Chat result = chatService.createChat(1L, 2L, 3L);

        // Then
        assertNotNull(result);
        assertEquals(chat, result);

        // Verify
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(productRepository, times(1)).findById(3L);
        verify(chatRepository, times(1)).save(any(ChatEntity.class));
        verify(chatConverter, times(1)).toDomain(chatEntity);
    }

    @Test
    void testGetChatsByUserId_shouldReturnChats() {
        // Given
        ChatEntity chatEntity = new ChatEntity();
        Chat chat = new Chat();

        when(chatRepository.findByBuyerIdOrSellerId(1L, 1L)).thenReturn(List.of(chatEntity));
        when(chatConverter.toDomain(chatEntity)).thenReturn(chat);

        // When
        List<Chat> result = chatService.getChatsByUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chat, result.get(0));

        // Verify
        verify(chatRepository, times(1)).findByBuyerIdOrSellerId(1L, 1L);
        verify(chatConverter, times(1)).toDomain(chatEntity);
    }

    @Test
    void testDeleteChat_withNonExistentChat_shouldThrowRuntimeException() {
        // Given
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> chatService.deleteChat(1L));
        assertEquals("Chat not found", exception.getMessage());

        // Verify
        verify(chatRepository, times(1)).findById(1L);
        verifyNoInteractions(requestAccessToken);
    }

    @Test
    void testDeleteChat_withUnauthorizedUser_shouldThrowUnauthorizedDataAccessException() {
        // Given
        ChatEntity chatEntity = new ChatEntity();
        UserEntity buyer = new UserEntity();
        buyer.setId(1L);
        UserEntity seller = new UserEntity();
        seller.setId(2L);
        chatEntity.setBuyer(buyer);
        chatEntity.setSeller(seller);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(requestAccessToken.getUserId()).thenReturn(3L);

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> chatService.deleteChat(1L));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(chatRepository, times(1)).findById(1L);
        verify(requestAccessToken, times(1)).getUserId();
    }

    @Test
    void testDeleteChat_withAuthorizedUser_shouldMarkChatAsDeleted() {
        // Given
        ChatEntity chatEntity = new ChatEntity();
        UserEntity buyer = new UserEntity();
        buyer.setId(1L);
        chatEntity.setBuyer(buyer);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        // When
        chatService.deleteChat(1L);

        // Then
        assertTrue(chatEntity.getDeleted());

        // Verify
        verify(chatRepository, times(1)).findById(1L);
        verify(requestAccessToken, times(1)).getUserId();
        verify(chatRepository, times(1)).save(chatEntity);
    }

    @Test
    void testRecoverChat_withNonExistentChat_shouldThrowRuntimeException() {
        // Given
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> chatService.recoverChat(1L));
        assertEquals("Chat not found", exception.getMessage());

        // Verify
        verify(chatRepository, times(1)).findById(1L);
        verifyNoInteractions(requestAccessToken);
    }

    @Test
    void testRecoverChat_withUnauthorizedUser_shouldThrowUnauthorizedDataAccessException() {
        // Given
        ChatEntity chatEntity = new ChatEntity();
        UserEntity buyer = new UserEntity();
        buyer.setId(1L);
        UserEntity seller = new UserEntity();
        seller.setId(2L);
        chatEntity.setBuyer(buyer);
        chatEntity.setSeller(seller);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(requestAccessToken.getUserId()).thenReturn(3L);

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> chatService.recoverChat(1L));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(chatRepository, times(1)).findById(1L);
        verify(requestAccessToken, times(1)).getUserId();
    }

    @Test
    void testRecoverChat_withAuthorizedUser_shouldMarkChatAsRecovered() {
        // Given
        ChatEntity chatEntity = new ChatEntity();
        UserEntity buyer = new UserEntity();
        buyer.setId(1L);
        chatEntity.setBuyer(buyer);
        chatEntity.setDeleted(true);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chatEntity));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        // When
        chatService.recoverChat(1L);

        // Then
        assertFalse(chatEntity.getDeleted());

        // Verify
        verify(chatRepository, times(1)).findById(1L);
        verify(requestAccessToken, times(1)).getUserId();
        verify(chatRepository, times(1)).save(chatEntity);
    }

    @Test
    void testGetMessagesByChatId_withNonExistentChat_shouldThrowIllegalArgumentException() {
        // Given
        when(messageRepository.findByChatId(1L)).thenReturn(Collections.emptyList());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> chatService.getMessagesByChatId(1L));
        assertEquals("Chat does not exist.", exception.getMessage());

        // Verify
        verify(messageRepository, times(1)).findByChatId(1L);
        verifyNoInteractions(messageConverter);
    }

    @Test
    void testGetMessagesByChatId_shouldReturnMessages() {
        // Given
        MessageEntity messageEntity = new MessageEntity();
        Message message = new Message();

        when(messageRepository.findByChatId(1L)).thenReturn(List.of(messageEntity));
        when(messageConverter.toDomain(messageEntity)).thenReturn(message);

        // When
        List<Message> result = chatService.getMessagesByChatId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(message, result.get(0));

        // Verify
        verify(messageRepository, times(1)).findByChatId(1L);
        verify(messageConverter, times(1)).toDomain(messageEntity);
    }
}
