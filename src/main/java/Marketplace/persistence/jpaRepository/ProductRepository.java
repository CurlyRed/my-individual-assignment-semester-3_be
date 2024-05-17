package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
    List<ProductEntity> findByUserId(long userId);
    List<ProductEntity> findByCategoryId(long categoryId);
}
