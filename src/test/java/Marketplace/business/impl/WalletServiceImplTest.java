package Marketplace.business.impl;

import Marketplace.business.dto.walletOperations.PurchasePromotionRequest;
import Marketplace.business.dto.walletOperations.TopUpRequest;
import Marketplace.business.exception.InsufficientBalanceException;
import Marketplace.business.exception.InvalidRequestException;
import Marketplace.config.security.token.AccessToken;
import Marketplace.enums.TransactionType;
import Marketplace.persistence.entity.*;
import Marketplace.persistence.jpaRepository.*;
import Marketplace.business.validators.AmountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserBalanceRepository userBalanceRepository;
    @Mock
    private AppBalanceRepository appBalanceRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AmountValidator amountValidator;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private TopUpRequest createTopUpRequest(Double amount) {
        return TopUpRequest.builder().amount(amount).build();
    }

    private PurchasePromotionRequest createPurchasePromotionRequest(Double amount, Long productId) {
        return PurchasePromotionRequest.builder().amount(amount).productId(productId).build();
    }

    private UserBalanceEntity createUserBalanceEntity(Long userId, Double balance) {
        UserEntity user = UserEntity.builder().id(userId).build();
        return UserBalanceEntity.builder().user(user).balance(balance).last_update(new Date()).build();
    }

    private ProductEntity createProductEntity(Long productId) {
        return ProductEntity.builder().id(productId).promoted(false).build();
    }

    private AppBalanceEntity createAppBalanceEntity(Long id, Double balance) {
        return AppBalanceEntity.builder().id(id).balance(balance).last_update(new Date()).build();
    }

    @Test
    void topUp_withValidRequest_shouldUpdateBalanceAndLogTransaction() {
        // Given
        Long userId = 1L;
        Double amount = 100.0;
        UserBalanceEntity userBalance = createUserBalanceEntity(userId, 50.0);

        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(amountValidator.isValid(amount)).thenReturn(true);
        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);

        TopUpRequest request = createTopUpRequest(amount);

        // When
        walletService.topUp(request);

        // Then
        assertEquals(150.0, userBalance.getBalance());

        // Verify
        verify(userBalanceRepository, times(1)).save(userBalance);
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    void topUp_withNullRequest_shouldThrowInvalidRequestException() {
        // When & Then
        assertThrows(InvalidRequestException.class, () -> walletService.topUp(null));

        // Verify
        verifyNoInteractions(userRepository, userBalanceRepository, transactionRepository);
    }

    @Test
    void topUp_withNonExistentUser_shouldThrowIllegalArgumentException() {
        // Given
        Long userId = 1L;
        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(amountValidator.isValid(anyDouble())).thenReturn(true);
        when(userBalanceRepository.findByUserId(userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        TopUpRequest request = createTopUpRequest(100.0);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> walletService.topUp(request));

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void purchasePromotion_withValidRequest_shouldUpdateBalancePromoteProductAndLogTransaction() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Double amount = 100.0;

        UserBalanceEntity userBalance = createUserBalanceEntity(userId, 150.0);
        ProductEntity product = createProductEntity(productId);
        AppBalanceEntity appBalance = createAppBalanceEntity(1L, 200.0);

        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(appBalanceRepository.findById(1)).thenReturn(Optional.of(appBalance));

        PurchasePromotionRequest request = createPurchasePromotionRequest(amount, productId);

        // When
        walletService.purchasePromotion(request);

        // Then
        assertEquals(50.0, userBalance.getBalance());
        assertTrue(product.getPromoted());

        // Verify
        verify(userBalanceRepository, times(1)).save(userBalance);
        verify(appBalanceRepository, times(1)).save(appBalance);
        verify(productRepository, times(1)).save(product);
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    void purchasePromotion_withNullRequest_shouldThrowInvalidRequestException() {
        // When & Then
        assertThrows(InvalidRequestException.class, () -> walletService.purchasePromotion(null));

        // Verify
        verifyNoInteractions(userRepository, userBalanceRepository, productRepository, transactionRepository);
    }

    @Test
    void purchasePromotion_withNonExistentUser_shouldThrowIllegalArgumentException() {
        // Given
        Long userId = 1L;
        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(userBalanceRepository.findByUserId(userId)).thenReturn(null);

        PurchasePromotionRequest request = createPurchasePromotionRequest(100.0, 1L);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> walletService.purchasePromotion(request));

        // Verify
        verify(userBalanceRepository, times(1)).findByUserId(userId);
        verifyNoMoreInteractions(userBalanceRepository);
        verifyNoInteractions(transactionRepository, productRepository);
    }

    @Test
    void purchasePromotion_withInsufficientBalance_shouldThrowInsufficientBalanceException() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Double amount = 200.0;

        UserBalanceEntity userBalance = createUserBalanceEntity(userId, 150.0);

        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);

        PurchasePromotionRequest request = createPurchasePromotionRequest(amount, productId);

        // When & Then
        assertThrows(InsufficientBalanceException.class, () -> walletService.purchasePromotion(request));

        // Verify
        verify(userBalanceRepository, times(1)).findByUserId(userId);
        verifyNoMoreInteractions(userBalanceRepository);
        verifyNoInteractions(transactionRepository, productRepository);
    }

    @Test
    void purchasePromotion_withNonExistentProduct_shouldThrowIllegalArgumentException() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Double amount = 100.0;

        UserBalanceEntity userBalance = createUserBalanceEntity(userId, 150.0);

        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        PurchasePromotionRequest request = createPurchasePromotionRequest(amount, productId);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> walletService.purchasePromotion(request));

        // Verify
        verify(userBalanceRepository, times(1)).findByUserId(userId);
        verify(productRepository, times(1)).findById(productId);
    }
}
