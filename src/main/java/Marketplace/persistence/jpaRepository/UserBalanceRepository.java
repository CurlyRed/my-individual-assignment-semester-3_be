package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.UserBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalanceEntity, Long> {
    UserBalanceEntity findByUserId(Long userId);
}
