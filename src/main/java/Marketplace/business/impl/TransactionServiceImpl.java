package Marketplace.business.impl;

import Marketplace.business.TransactionService;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.config.security.token.AccessToken;
import Marketplace.domain.Transaction;
import Marketplace.persistence.converter.TransactionConverter;
import Marketplace.persistence.jpaRepository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionConverter transactionConverter;
    private final AccessToken requestAccessToken;
    private final String unauthorizedExceptionMessage = "USER_ID_NOT_FROM_LOGGED_IN_USER";

    @Override
    public List<Transaction> getTransactions() {
        if (!Objects.equals(requestAccessToken.getRole(), "ADMIN")) {
            throw new UnauthorizedDataAccessException(unauthorizedExceptionMessage);
        }
        return this.transactionRepository.findAll().stream()
                .map(transactionConverter::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> getTransactionsForUser(long userId) {
        if (requestAccessToken.getUserId() != userId) {
            throw new UnauthorizedDataAccessException(unauthorizedExceptionMessage);
        }

        return this.transactionRepository.findAllByUserId(userId).stream()
                .map(transactionConverter::toDomain)
                .toList();
    }
}
