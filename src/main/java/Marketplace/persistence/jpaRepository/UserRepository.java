package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN u.products p WHERE p.id = :productId")
    Optional<UserEntity> findByProductId(@Param("productId") Long productId);
}
