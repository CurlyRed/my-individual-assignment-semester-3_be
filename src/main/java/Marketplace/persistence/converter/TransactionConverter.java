package Marketplace.persistence.converter;

import Marketplace.domain.Transaction;
import Marketplace.persistence.entity.TransactionEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionConverter {

    private final UserConverter userConverter;
    private final ProductConverter productConverter;
    public Transaction toDomain(TransactionEntity transaction) {
        if (transaction == null) {
            return null;
        }

        return Transaction.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .created_at(transaction.getCreated_at())
                .product(productConverter.toDomain(transaction.getProduct()))
                .user(userConverter.toDomain(transaction.getUser()))
                .build();
    }
}
