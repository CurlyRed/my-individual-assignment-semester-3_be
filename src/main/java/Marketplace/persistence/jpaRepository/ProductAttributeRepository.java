package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.ProductAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttributeEntity, Long> {
}
