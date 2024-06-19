package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.CategoryEntity;
import Marketplace.persistence.entity.CityEntity;
import Marketplace.persistence.entity.DistrictEntity;
import Marketplace.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.Assert.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private CityRepository cityRepository;


    @BeforeEach
    public void setUp() {
        CategoryEntity category1 = CategoryEntity.builder()
                .name("Test1")
                .build();
        categoryRepository.save(category1);

        CategoryEntity category2 = CategoryEntity.builder()
                .name("Test2")
                .build();
        categoryRepository.save(category2);

        DistrictEntity district = districtRepository.save(DistrictEntity.builder()
                .name("test")
                .build());

        CityEntity cityEntity = cityRepository.save(CityEntity.builder()
                .name("test")
                .district(district)
                .build());

        ProductEntity product1 = ProductEntity.builder()
                .name("Test1")
                .category(category1)
                .city(cityEntity)
                .date_of_post(new Date())
                .build();
        productRepository.save(product1);

        ProductEntity product2 = ProductEntity.builder()
                .name("Test2")
                .category(category2)
                .city(cityEntity)
                .date_of_post(new Date())
                .build();
        productRepository.save(product2);
    }

    @Test
    public void testCountAllProducts() {
        long count = productRepository.countAllProducts();
        assertEquals(17, count);
    }

    @Test
    public void testCountPromotedProducts() {
        long count = productRepository.countPromotedProducts();
        assertEquals(8, count);
    }

    @Test
    public void testCountProductsByCategory() {
        List<Object[]> results = productRepository.countProductsByCategory();
        assertEquals(4, results.size());

        for (Object[] result : results) {
            String categoryName = (String) result[0];
            long productCount = (long) result[1];

            if (categoryName.equals("Test1")) {
                assertEquals(1, productCount);
            } else if (categoryName.equals("Test2")) {
                assertEquals(1, productCount);
            } else if (categoryName.equals("Car")){
                assertEquals(13, productCount);
            } else if (categoryName.equals("Real Estate")){
                assertEquals(2, productCount);
            } else {
                fail("Unexpected category: " + categoryName);
            }
        }
    }
}
