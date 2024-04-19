package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CityRepository cityRepository;

    @Test
    void save_shouldSaveProductWithProductAttributes(){
        // Given
        List<AttributeEntity> attributes = new ArrayList<>();
        attributes.add(AttributeEntity.builder().name("Test1").build());
        attributes.add(AttributeEntity.builder().name("Test2").build());

        CategoryEntity category = CategoryEntity.builder()
                .name("CategoryTest")
                .attributes(attributes)
                .build();
        category = categoryRepository.save(category);

        RoleEntity role = RoleEntity.builder()
                .name("USER")
                .build();
        roleRepository.save(role);

        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(role)
                .build();

        user = userRepository.save(user);

        DistrictEntity district = DistrictEntity.builder()
                .name("Test")
                .build();

        districtRepository.save(district);

        CityEntity city = CityEntity.builder()
                .name("Test")
                .district(district)
                .build();

        city = cityRepository.save(city);

        List<ProductAttributeEntity> productAttributes = new ArrayList<>();
        for (AttributeEntity attribute : category.getAttributes()) {
            ProductAttributeEntity productAttribute = ProductAttributeEntity.builder()
                    .value("Value for " + attribute.getName())
                    .attribute(attribute)
                    .build();
            productAttributes.add(productAttribute);
        }

        ProductEntity product = ProductEntity.builder()
                .name("Test")
                .description("Test")
                .category(category)
                .user(user)
                .product_attributes(productAttributes)
                .city(city)
                .build();

        // When
        ProductEntity savedProduct = productRepository.save(product);

        // Then
        assertNotNull(savedProduct.getId());
        assertEquals(product, savedProduct);
    }

    @Test
    void delete_shouldDeleteProduct(){
        // Given
        List<AttributeEntity> attributes = new ArrayList<>();
        attributes.add(AttributeEntity.builder().name("Test1").build());
        attributes.add(AttributeEntity.builder().name("Test2").build());

        CategoryEntity category = CategoryEntity.builder()
                .name("CategoryTest")
                .attributes(attributes)
                .build();
        category = categoryRepository.save(category);

        RoleEntity role = RoleEntity.builder()
                .name("USER")
                .build();
        roleRepository.save(role);

        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(role)
                .build();

        user = userRepository.save(user);

        DistrictEntity district = DistrictEntity.builder()
                .name("Test")
                .build();

        districtRepository.save(district);

        CityEntity city = CityEntity.builder()
                .name("Test")
                .district(district)
                .build();

        city = cityRepository.save(city);

        List<ProductAttributeEntity> productAttributes = new ArrayList<>();
        for (AttributeEntity attribute : category.getAttributes()) {
            ProductAttributeEntity productAttribute = ProductAttributeEntity.builder()
                    .value("Value for " + attribute.getName())
                    .attribute(attribute)
                    .build();
            productAttributes.add(productAttribute);
        }

        ProductEntity product = ProductEntity.builder()
                .name("Test")
                .description("Test")
                .category(category)
                .user(user)
                .product_attributes(productAttributes)
                .city(city)
                .build();

        ProductEntity savedProduct = productRepository.save(product);
        // When
        productRepository.delete(savedProduct);

        // Then
        assertFalse(productRepository.existsById(savedProduct.getId()));
    }

    @Test
    void findById_shouldReturnProductById(){
        // Given
        List<AttributeEntity> attributes = new ArrayList<>();
        attributes.add(AttributeEntity.builder().name("Test1").build());
        attributes.add(AttributeEntity.builder().name("Test2").build());

        CategoryEntity category = CategoryEntity.builder()
                .name("CategoryTest")
                .attributes(attributes)
                .build();
        category = categoryRepository.save(category);

        RoleEntity role = RoleEntity.builder()
                .name("USER")
                .build();
        roleRepository.save(role);

        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(role)
                .build();

        user = userRepository.save(user);

        DistrictEntity district = DistrictEntity.builder()
                .name("Test")
                .build();

        districtRepository.save(district);

        CityEntity city = CityEntity.builder()
                .name("Test")
                .district(district)
                .build();

        city = cityRepository.save(city);

        List<ProductAttributeEntity> productAttributes = new ArrayList<>();
        for (AttributeEntity attribute : category.getAttributes()) {
            ProductAttributeEntity productAttribute = ProductAttributeEntity.builder()
                    .value("Value for " + attribute.getName())
                    .attribute(attribute)
                    .build();
            productAttributes.add(productAttribute);
        }

        ProductEntity product = ProductEntity.builder()
                .name("Test")
                .description("Test")
                .category(category)
                .user(user)
                .product_attributes(productAttributes)
                .city(city)
                .build();

        ProductEntity savedProduct = productRepository.save(product);
        // When
        ProductEntity foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);

        // Then
        assertNotNull(foundProduct);
        assertEquals(product, foundProduct);
    }

    @Test
    void findAll_shouldReturnAllProducts(){
        // Given
        List<AttributeEntity> attributes = new ArrayList<>();
        attributes.add(AttributeEntity.builder().name("Test1").build());
        attributes.add(AttributeEntity.builder().name("Test2").build());

        CategoryEntity category = CategoryEntity.builder()
                .name("CategoryTest")
                .attributes(attributes)
                .build();
        category = categoryRepository.save(category);

        RoleEntity role = RoleEntity.builder()
                .name("USER")
                .build();
        roleRepository.save(role);

        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(role)
                .build();

        user = userRepository.save(user);

        DistrictEntity district = DistrictEntity.builder()
                .name("Test")
                .build();

        districtRepository.save(district);

        CityEntity city = CityEntity.builder()
                .name("Test")
                .district(district)
                .build();

        city = cityRepository.save(city);

        List<ProductAttributeEntity> productAttributes = new ArrayList<>();
        for (AttributeEntity attribute : category.getAttributes()) {
            ProductAttributeEntity productAttribute = ProductAttributeEntity.builder()
                    .value("Value for " + attribute.getName())
                    .attribute(attribute)
                    .build();
            productAttributes.add(productAttribute);
        }

        ProductEntity product1 = ProductEntity.builder()
                .name("Test")
                .description("Test")
                .category(category)
                .user(user)
                .product_attributes(productAttributes)
                .city(city)
                .build();

        ProductEntity product2 = ProductEntity.builder()
                .name("Test2")
                .description("Test2")
                .category(category)
                .user(user)
                .product_attributes(productAttributes)
                .city(city)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);

        // When
        List<ProductEntity> allProducts = productRepository.findAll();

        // Then
        assertNotNull(allProducts);
        assertTrue(allProducts.contains(product1));
        assertTrue(allProducts.contains(product2));
    }
}
