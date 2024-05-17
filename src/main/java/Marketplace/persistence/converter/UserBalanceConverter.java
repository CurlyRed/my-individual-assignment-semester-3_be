package Marketplace.persistence.converter;

import Marketplace.domain.UserBalance;
import Marketplace.persistence.entity.UserBalanceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserBalanceConverter {
    public UserBalance toDomain(UserBalanceEntity userBalance) {
        if (userBalance == null) {
            return null;
        }

        return UserBalance.builder()
                .id(userBalance.getId())
                .balance(userBalance.getBalance())
                .last_update(userBalance.getLast_update())
                .build();
    }
}
