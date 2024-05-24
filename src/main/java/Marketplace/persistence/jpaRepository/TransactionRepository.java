package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllByUserId(Long userId);

    @Query("SELECT SUM(t.amount) FROM TransactionEntity t WHERE t.type = 'PROMOTION_PURCHASE' AND t.created_at BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalSales(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT AVG(t.amount) FROM TransactionEntity t WHERE t.type = 'PROMOTION_PURCHASE' AND t.created_at BETWEEN :startDate AND :endDate")
    BigDecimal avgOrderValue(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT c.name, SUM(t.amount) " +
            "FROM TransactionEntity t " +
            "JOIN t.product p " +
            "JOIN p.category c " +
            "WHERE t.type = 'PROMOTION_PURCHASE' " +
            "AND t.created_at BETWEEN :startDate AND :endDate " +
            "GROUP BY c.name")
    List<Object[]> sumSalesByCategory(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT FUNCTION('DATE_FORMAT', t.created_at, '%Y-%m'), SUM(t.amount) FROM TransactionEntity t WHERE t.type = 'PROMOTION_PURCHASE' AND t.created_at BETWEEN :startDate AND :endDate GROUP BY FUNCTION('DATE_FORMAT', t.created_at, '%Y-%m')")
    List<Object[]> sumMonthlyRevenue(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT t.type, COUNT(t) FROM TransactionEntity t WHERE t.created_at BETWEEN :startDate AND :endDate GROUP BY t.type")
    List<Object[]> countTransactionsByType(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

