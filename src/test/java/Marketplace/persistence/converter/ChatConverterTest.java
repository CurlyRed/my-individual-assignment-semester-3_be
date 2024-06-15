package Marketplace.persistence.converter;

import Marketplace.domain.Chat;
import Marketplace.domain.Product;
import Marketplace.domain.User;
import Marketplace.persistence.entity.ChatEntity;
import Marketplace.persistence.entity.ProductEntity;
import Marketplace.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class ChatConverterTest {

    private ChatConverter chatConverter;

    @Mock
    private UserConverter userConverter;

    @Mock
    private ProductConverter productConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        chatConverter = new ChatConverter(userConverter, productConverter);
    }

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        UserEntity buyerEntity = UserEntity.builder()
                .id(1L)
                .email("buyer@example.com")
                .password("password")
                .build();

        UserEntity sellerEntity = UserEntity.builder()
                .id(2L)
                .email("seller@example.com")
                .password("password")
                .build();

        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("Test Product")
                .build();

        ChatEntity chatEntity = ChatEntity.builder()
                .id(1L)
                .createdAt(new Date())
                .buyer(buyerEntity)
                .seller(sellerEntity)
                .product(productEntity)
                .deleted(false)
                .build();

        when(userConverter.toDomain(buyerEntity)).thenReturn(
                User.builder().id(1L).email("buyer@example.com").password("password").build());
        when(userConverter.toDomain(sellerEntity)).thenReturn(
                User.builder().id(2L).email("seller@example.com").password("password").build());
        when(productConverter.toDomain(productEntity)).thenReturn(
                Product.builder().id(1L).name("Test Product").build());

        // When
        Chat chat = chatConverter.toDomain(chatEntity);

        // Then
        assertNotNull(chat);
        assertEquals(chatEntity.getId(), chat.getId());
        assertNotNull(chat.getBuyer());
        assertEquals(buyerEntity.getId(), chat.getBuyer().getId());
        assertNotNull(chat.getSeller());
        assertEquals(sellerEntity.getId(), chat.getSeller().getId());
        assertNotNull(chat.getProduct());
        assertEquals(productEntity.getId(), chat.getProduct().getId());
        assertEquals(chatEntity.getCreatedAt(), chat.getCreated_at());
        assertEquals(chatEntity.getDeleted(), chat.getDeleted());
    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        ChatEntity chatEntity = null;

        // When
        Chat chat = chatConverter.toDomain(chatEntity);

        // Then
        assertNull(chat);
    }

    @Test
    void testToDomain_givenEntityWithNullFields_shouldHandleGracefully() {
        // Given
        ChatEntity chatEntity = ChatEntity.builder()
                .id(1L)
                .buyer(null)
                .seller(null)
                .product(null)
                .createdAt(null)
                .deleted(null)
                .build();

        // When
        Chat chat = chatConverter.toDomain(chatEntity);

        // Then
        assertNotNull(chat);
        assertEquals(chatEntity.getId(), chat.getId());
        assertNull(chat.getBuyer());
        assertNull(chat.getSeller());
        assertNull(chat.getProduct());
        assertNull(chat.getCreated_at());
        assertNull(chat.getDeleted());
    }
}
