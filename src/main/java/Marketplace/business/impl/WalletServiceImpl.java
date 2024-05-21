package Marketplace.business.impl;

import Marketplace.business.WalletService;
import Marketplace.business.dto.walletOperations.PurchasePromotionRequest;
import Marketplace.business.dto.walletOperations.TopUpRequest;
import Marketplace.business.exception.InsufficientBalanceException;
import Marketplace.business.exception.InvalidRequestException;
import Marketplace.config.security.token.AccessToken;
import Marketplace.enums.TransactionType;
import Marketplace.persistence.entity.*;
import Marketplace.persistence.jpaRepository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final UserBalanceRepository userBalanceRepository;
    private final AppBalanceRepository appBalanceRepository;
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final AccessToken requestAccessToken;

    @Override
    @Transactional
    public void topUp(TopUpRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Top up request cannot be null");
        }

        Long userId = requestAccessToken.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("Invalid access token: USER ID NOT FOUND");
        }

        UserBalanceEntity userBalance = userBalanceRepository.findByUserId(userId);
        if (userBalance == null) {
            Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
            if (!userEntityOptional.isPresent()) {
                throw new IllegalArgumentException("User not found for the provided user ID");
            }
            UserEntity userEntity = userEntityOptional.get();
            userBalance = UserBalanceEntity.builder()
                    .user(userEntity)
                    .balance(0.00)
                    .last_update(new Date())
                    .build();
        }

        double newBalance = userBalance.getBalance() + request.getAmount();
        userBalance.setBalance(newBalance);
        userBalance.setLast_update(new Date());
        userBalanceRepository.save(userBalance);

        TransactionEntity transaction = TransactionEntity.builder()
                .type(TransactionType.TOP_UP)
                .amount(request.getAmount())
                .description("Top-up to user balance")
                .created_at(new Date())
                .user(userBalance.getUser())
                .build();
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void purchasePromotion(PurchasePromotionRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Purchase promotion request cannot be null");
        }

        Long userId = requestAccessToken.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("Invalid access token: USER ID NOT FOUND");
        }

        UserBalanceEntity userBalance = userBalanceRepository.findByUserId(userId);
        if (userBalance == null) {
            throw new IllegalArgumentException("User balance not found for the provided user ID");
        }

        if (userBalance.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance for the promotion purchase");
        }

        double newUserBalance = userBalance.getBalance() - request.getAmount();
        userBalance.setBalance(newUserBalance);
        userBalance.setLast_update(new Date());
        userBalanceRepository.save(userBalance);

        Optional<AppBalanceEntity> appBalanceOptional = appBalanceRepository.findById(1);
        AppBalanceEntity appBalance;
        if (appBalanceOptional.isPresent()) {
            appBalance = appBalanceOptional.get();
        } else {
            appBalance = AppBalanceEntity.builder()
                    .balance(0.00)
                    .last_update(new Date())
                    .build();
        }

        double newAppBalance = appBalance.getBalance() + request.getAmount();
        appBalance.setBalance(newAppBalance);
        appBalance.setLast_update(new Date());
        appBalanceRepository.save(appBalance);

        Optional<ProductEntity> productOptional = productRepository.findById(request.getProductId());
        if (!productOptional.isPresent()) {
            throw new IllegalArgumentException("Product not found for the provided product ID");
        }
        ProductEntity product = productOptional.get();
        product.setPromoted(true);
        productRepository.save(product);

        TransactionEntity transaction = TransactionEntity.builder()
                .type(TransactionType.PROMOTION_PURCHASE)
                .amount(request.getAmount())
                .description("Purchased promotion for product ID: " + request.getProductId())
                .created_at(new Date())
                .user(userBalance.getUser())
                .build();
        transactionRepository.save(transaction);
    }
}
