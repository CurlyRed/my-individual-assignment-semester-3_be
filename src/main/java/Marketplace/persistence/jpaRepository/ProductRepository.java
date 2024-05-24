package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
    List<ProductEntity> findByUserId(long userId);
    List<ProductEntity> findByCategoryId(long categoryId);
    @Query("SELECT COUNT(p) FROM ProductEntity p")
    long countAllProducts();

    @Query("SELECT COUNT(p) FROM ProductEntity p WHERE p.promoted = TRUE")
    long countPromotedProducts();

    @Query("SELECT c.name, COUNT(p) " +
            "FROM ProductEntity p " +
            "JOIN p.category c " +
            "GROUP BY c.name")
    List<Object[]> countProductsByCategory();
}
