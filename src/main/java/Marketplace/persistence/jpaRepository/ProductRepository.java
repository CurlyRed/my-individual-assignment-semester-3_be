package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.ProductEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
    @Modifying
    @Query("DELETE FROM ProductEntity pE where pE.id =:id")
    void bla(@Param("id") Integer id);
}
