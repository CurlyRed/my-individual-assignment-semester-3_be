package Marketplace.business;

import Marketplace.business.dto.walletOperations.PurchasePromotionRequest;
import Marketplace.business.dto.walletOperations.TopUpRequest;
import Marketplace.business.exception.InsufficientBalanceException;
import Marketplace.business.exception.InvalidRequestException;
import Marketplace.business.impl.WalletServiceImpl;
import Marketplace.config.security.token.AccessToken;
import Marketplace.persistence.entity.*;
import Marketplace.persistence.jpaRepository.*;
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
    private AccessToken requestAccessToken;
    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void topUp_withValidRequest_shouldUpdateBalanceAndLogTransaction() {
        // Given
        Long userId = 1L;
        Double amount = 100.0;
        UserBalanceEntity userBalance = createUserBalanceEntity(userId, 50.0);

        when(requestAccessToken.getUserId()).thenReturn(userId);
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
        // Given
        // When
        // Then
        assertThrows(InvalidRequestException.class, () -> walletService.topUp(null));

        // Verify
        verifyNoInteractions(userRepository, userBalanceRepository, transactionRepository);
    }

    @Test
    void topUp_withNonExistentUser_shouldThrowIllegalArgumentException() {
        // Given
        Long userId = 1L;
        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(userBalanceRepository.findByUserId(userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        TopUpRequest request = createTopUpRequest(100.0);

        // When
        // Then
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
        AppBalanceEntity appBalance = createAppBalanceEntity(1L, 200.0); // Properly initialized

        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(userBalanceRepository.findByUserId(userId)).thenReturn(userBalance);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(appBalanceRepository.findById(1)).thenReturn(Optional.of(appBalance)); // Return initialized appBalance

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
        // Given
        // When
        // Then
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

        // When
        // Then
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

        // When
        // Then
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

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> walletService.purchasePromotion(request));

        // Verify
        verify(userBalanceRepository, times(1)).findByUserId(userId);
        verify(productRepository, times(1)).findById(productId);
    }

    private TopUpRequest createTopUpRequest(Double amount) {
        TopUpRequest request = new TopUpRequest();
        request.setAmount(amount);
        return request;
    }

    private PurchasePromotionRequest createPurchasePromotionRequest(Double amount, Long productId) {
        PurchasePromotionRequest request = new PurchasePromotionRequest();
        request.setAmount(amount);
        request.setProductId(productId);
        return request;
    }

    private UserBalanceEntity createUserBalanceEntity(Long userId, Double balance) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        UserBalanceEntity userBalance = new UserBalanceEntity();
        userBalance.setUser(user);
        userBalance.setBalance(balance);
        return userBalance;
    }

    private ProductEntity createProductEntity(Long productId) {
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        product.setPromoted(false);
        return product;
    }

    private AppBalanceEntity createAppBalanceEntity(Long id, Double balance) {
        AppBalanceEntity appBalance = new AppBalanceEntity();
        appBalance.setId(id);
        appBalance.setBalance(balance);
        appBalance.setLast_update(new Date());
        return appBalance;
    }}
