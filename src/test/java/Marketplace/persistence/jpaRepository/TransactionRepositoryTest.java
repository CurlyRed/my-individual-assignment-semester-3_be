package Marketplace.persistence.jpaRepository;

import Marketplace.enums.TransactionType;
import Marketplace.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
     void setUp() {
        RoleEntity userRole = roleRepository.save(RoleEntity.builder().name("USER").build());

        CategoryEntity category = categoryRepository.save(CategoryEntity.builder().name("Category 1").build());

        UserEntity user = userRepository.save(UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .role(userRole)
                .build());

        DistrictEntity district = districtRepository.save(DistrictEntity.builder()
                .name("test")
                .build());

        CityEntity cityEntity = cityRepository.save(CityEntity.builder()
                .name("test")
                .district(district)
                .build());

        ProductEntity product = productRepository.save(ProductEntity.builder()
                .name("Product 1")
                .description("Description")
                .price(100.0)
                .category(category)
                .date_of_post(new Date())
                .promoted(false)
                .city(cityEntity)
                .build());

        transactionRepository.save(TransactionEntity.builder()
                .amount(100.0)
                .type(TransactionType.PROMOTION_PURCHASE)
                .created_at(new Date())
                .product(product)
                .user(user)
                .build());

        transactionRepository.save(TransactionEntity.builder()
                .amount(200.0)
                .type(TransactionType.PROMOTION_PURCHASE)
                .created_at(new Date())
                .product(product)
                .user(user)
                .build());
    }

    @Test
    void testFindAllByUserId() {
        UserEntity user = userRepository.findByEmail("test@example.com");
        List<TransactionEntity> transactions = transactionRepository.findAllByUserId(user.getId());
        assertThat(transactions).hasSize(2);
    }

    @Test
    void testSumTotalSales() {
        Date startDate = new Date(System.currentTimeMillis() - 100000000);
        Date endDate = new Date();
        BigDecimal totalSales = transactionRepository.sumTotalSales(startDate, endDate);
        assertThat(totalSales).isEqualByComparingTo(BigDecimal.valueOf(300));
    }

    @Test
    void testAvgOrderValue() {
        Date startDate = new Date(System.currentTimeMillis() - 100000000);
        Date endDate = new Date();
        BigDecimal avgOrderValue = transactionRepository.avgOrderValue(startDate, endDate);
        assertThat(avgOrderValue).isEqualByComparingTo(BigDecimal.valueOf(150));
    }

    @Test
    void testSumSalesByCategory() {
        Date startDate = new Date(System.currentTimeMillis() - 100000000);
        Date endDate = new Date();
        List<Object[]> result = transactionRepository.sumSalesByCategory(startDate, endDate);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)[0]).isEqualTo("Category 1");
        assertThat(result.get(0)[1]).isEqualTo(300.0);
    }

    @Test
    void testSumMonthlyRevenue() {
        Date startDate = new Date(System.currentTimeMillis() - 100000000);
        Date endDate = new Date();
        List<Object[]> result = transactionRepository.sumMonthlyRevenue(startDate, endDate);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)[1]).isEqualTo(300.0);
    }

    @Test
    void testCountTransactionsByType() {
        Date startDate = new Date(System.currentTimeMillis() - 100000000);
        Date endDate = new Date();
        List<Object[]> result = transactionRepository.countTransactionsByType(startDate, endDate);
        assertThat(result).anyMatch(record -> record[0] == TransactionType.PROMOTION_PURCHASE && record[1].equals(2L));
    }
}
