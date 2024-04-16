package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<AttributeEntity, Long> {
}
