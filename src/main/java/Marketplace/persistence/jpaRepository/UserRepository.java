package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN u.products p WHERE p.id = :productId")
    Optional<UserEntity> findByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(u) FROM UserEntity u JOIN u.role r WHERE r.name = 'USER'")
    long countAllUsers();

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.date_of_registry >= :startDate")
    long countNewUsersSince(@Param("startDate") Date startDate);

    @Query("SELECT COUNT(DISTINCT t.user.id) FROM TransactionEntity t WHERE t.created_at BETWEEN :startDate AND :endDate")
    long countActiveUsersBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
