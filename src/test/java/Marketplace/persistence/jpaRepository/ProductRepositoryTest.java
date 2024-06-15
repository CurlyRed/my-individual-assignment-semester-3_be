package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.ProductEntity;
import Marketplace.persistence.entity.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Electronics");
        category = categoryRepository.save(category);

        ProductEntity product1 = new ProductEntity();
        product1.setName("Laptop");
        product1.setCategory(category);
        product1.setPromoted(false);

        ProductEntity product2 = new ProductEntity();
        product2.setName("Smartphone");
        product2.setCategory(category);
        product2.setPromoted(true);

        productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    void testCountAllProducts() {
        // When
        long count = productRepository.countAllProducts();

        // Then
        assertEquals(2, count);
    }

    @Test
    void testCountPromotedProducts() {
        // When
        long count = productRepository.countPromotedProducts();

        // Then
        assertEquals(1, count);
    }

    @Test
    void testCountProductsByCategory() {
        // When
        List<Object[]> counts = productRepository.countProductsByCategory();

        // Then
        assertEquals(1, counts.size());
        assertEquals("Electronics", counts.get(0)[0]);
        assertEquals(2L, counts.get(0)[1]);
    }
}
