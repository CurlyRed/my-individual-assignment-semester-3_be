package Marketplace.persistence.converter;

import Marketplace.domain.Transaction;
import Marketplace.persistence.entity.TransactionEntity;
import Marketplace.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionConverterTest {

    private TransactionConverter transactionConverter;
    private UserConverter userConverter;

    @BeforeEach
    void setUp() {
        userConverter = mock(UserConverter.class);
        transactionConverter = new TransactionConverter(userConverter);
    }

    @Test
    void testToDomain_withValidTransactionEntity_shouldReturnTransaction() {
        // Given
        UserEntity userEntity = new UserEntity();
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(1L);
        transactionEntity.setType("Credit");
        transactionEntity.setAmount(100.0);
        transactionEntity.setDescription("Test transaction");
        transactionEntity.setCreated_at(new Date());
        transactionEntity.setUser(userEntity);

        when(userConverter.toDomain(userEntity)).thenReturn(new Marketplace.domain.User());

        // When
        Transaction transaction = transactionConverter.toDomain(transactionEntity);

        // Then
        assertNotNull(transaction);
        assertEquals(transactionEntity.getId(), transaction.getId());
        assertEquals(transactionEntity.getType(), transaction.getType());
        assertEquals(transactionEntity.getAmount(), transaction.getAmount());
        assertEquals(transactionEntity.getDescription(), transaction.getDescription());
        assertEquals(transactionEntity.getCreated_at(), transaction.getCreated_at());
        assertNotNull(transaction.getUser());

        verify(userConverter, times(1)).toDomain(userEntity);
    }

    @Test
    public void testToDomain_withNullTransactionEntity_shouldReturnNull() {
        // Given
        TransactionEntity transactionEntity = null;

        // When
        Transaction transaction = transactionConverter.toDomain(transactionEntity);

        // Then
        assertNull(transaction);
    }
}
