package Marketplace.business.impl;

import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.config.security.token.AccessToken;
import Marketplace.domain.Transaction;
import Marketplace.persistence.converter.TransactionConverter;
import Marketplace.persistence.entity.TransactionEntity;
import Marketplace.persistence.jpaRepository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionConverter transactionConverter;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetTransactions_withNonAdminRole_shouldThrowUnauthorizedException() {
        // Given
        when(requestAccessToken.getRole()).thenReturn("USER");

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> transactionService.getTransactions());
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(requestAccessToken, times(1)).getRole();
        verifyNoInteractions(transactionRepository, transactionConverter);
    }

    @Test
    void testGetTransactions_withAdminRole_shouldReturnTransactions() {
        // Given
        when(requestAccessToken.getRole()).thenReturn("ADMIN");

        TransactionEntity transactionEntity = new TransactionEntity();
        Transaction transaction = new Transaction();

        when(transactionRepository.findAll()).thenReturn(List.of(transactionEntity));
        when(transactionConverter.toDomain(transactionEntity)).thenReturn(transaction);

        // When
        List<Transaction> transactions = transactionService.getTransactions();

        // Then
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));

        // Verify
        verify(requestAccessToken, times(1)).getRole();
        verify(transactionRepository, times(1)).findAll();
        verify(transactionConverter, times(1)).toDomain(transactionEntity);
    }

    @Test
    void testGetTransactionsForUser_withDifferentUserId_shouldThrowUnauthorizedException() {
        // Given
        long userId = 1L;
        when(requestAccessToken.getUserId()).thenReturn(2L);

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> transactionService.getTransactionsForUser(userId));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(requestAccessToken, times(1)).getUserId();
        verifyNoInteractions(transactionRepository, transactionConverter);
    }

    @Test
    void testGetTransactionsForUser_withMatchingUserId_shouldReturnTransactions() {
        // Given
        long userId = 1L;
        when(requestAccessToken.getUserId()).thenReturn(userId);

        TransactionEntity transactionEntity = new TransactionEntity();
        Transaction transaction = new Transaction();

        when(transactionRepository.findAllByUserId(userId)).thenReturn(List.of(transactionEntity));
        when(transactionConverter.toDomain(transactionEntity)).thenReturn(transaction);

        // When
        List<Transaction> transactions = transactionService.getTransactionsForUser(userId);

        // Then
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));

        // Verify
        verify(requestAccessToken, times(1)).getUserId();
        verify(transactionRepository, times(1)).findAllByUserId(userId);
        verify(transactionConverter, times(1)).toDomain(transactionEntity);
    }
}
