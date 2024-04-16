package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
}
