package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.AppBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppBalanceRepository extends JpaRepository<AppBalanceEntity, Integer> {
}
