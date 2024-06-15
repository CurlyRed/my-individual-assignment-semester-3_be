package Marketplace.persistence.converter;

import Marketplace.domain.*;
import Marketplace.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MessageConverterTest {

    private MessageConverter messageConverter;

    @Mock
    private UserConverter userConverter;

    @Mock
    private ChatConverter chatConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        messageConverter = new MessageConverter(userConverter, chatConverter);
    }

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();

        ChatEntity chatEntity = ChatEntity.builder()
                .id(1L)
                .createdAt(new Date())
                .buyer(userEntity)
                .seller(userEntity)
                .product(null)
                .deleted(false)
                .messages(null)
                .build();

        MessageEntity messageEntity = MessageEntity.builder()
                .id(1L)
                .sender(userEntity)
                .content("Test message")
                .timestamp(LocalDateTime.now())
                .chat(chatEntity)
                .build();

        when(userConverter.toDomain(userEntity)).thenReturn(
                User.builder().id(1L).email("test@example.com").password("password").build());
        when(chatConverter.toDomain(chatEntity)).thenReturn(
                Chat.builder().id(1L).created_at(new Date()).buyer(new User()).seller(new User()).deleted(false).build());

        // When
        Message message = messageConverter.toDomain(messageEntity);

        // Then
        assertNotNull(message);
        assertEquals(messageEntity.getId(), message.getId());
        assertNotNull(message.getSender());
        assertEquals(userEntity.getId(), message.getSender().getId());
        assertEquals(messageEntity.getContent(), message.getContent());
        assertEquals(messageEntity.getTimestamp(), message.getTimeStamp());
        assertNotNull(message.getChat());
        assertEquals(chatEntity.getId(), message.getChat().getId());
    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        MessageEntity messageEntity = null;

        // When
        Message message = messageConverter.toDomain(messageEntity);

        // Then
        assertNull(message);
    }

    @Test
    void testToDomain_givenEntityWithNullFields_shouldHandleGracefully() {
        // Given
        MessageEntity messageEntity = MessageEntity.builder()
                .id(1L)
                .sender(null)
                .content(null)
                .timestamp(null)
                .chat(null)
                .build();

        // When
        Message message = messageConverter.toDomain(messageEntity);

        // Then
        assertNotNull(message);
        assertEquals(messageEntity.getId(), message.getId());
        assertNull(message.getSender());
        assertNull(message.getContent());
        assertNull(message.getTimeStamp());
        assertNull(message.getChat());
    }
}