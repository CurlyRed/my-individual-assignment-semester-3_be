package Marketplace.persistence.converter;

import Marketplace.domain.AppBalance;
import Marketplace.persistence.entity.AppBalanceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppBalanceConverter {
    public AppBalance toDomain(AppBalanceEntity balance){
        if (balance == null) {
            return null;
        }

        return AppBalance.builder()
                .id(balance.getId())
                .balance(balance.getBalance())
                .last_update(balance.getLast_update())
                .build();
    }
}
