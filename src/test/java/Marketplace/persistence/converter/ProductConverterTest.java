package Marketplace.persistence.converter;

import Marketplace.domain.Category;
import Marketplace.domain.Product;
import Marketplace.persistence.entity.CategoryEntity;
import Marketplace.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class ProductConverterTest {

    private ProductConverter converter;

    @Mock
    private CategoryConverter categoryConverter;

    @Mock
    private ProductAttributeConverter productAttributeConverter;

    @Mock
    private ContactInformationConverter contactInformationConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new ProductConverter(categoryConverter, productAttributeConverter, contactInformationConverter);
    }

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .id(1L)
                .name("Test Category")
                .build();

        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .category(categoryEntity)
                .product_attributes(new ArrayList<>())
                .build();

        when(categoryConverter.toDomain(categoryEntity)).thenReturn(
                Category.builder().id(1L).name("Test Category").build());

        // When
        Product product = converter.toDomain(productEntity);

        // Then
        assertNotNull(product);
        assertEquals(productEntity.getId(), product.getId());
        assertEquals(productEntity.getName(), product.getName());
        assertEquals(productEntity.getDescription(), product.getDescription());
        assertNotNull(product.getCategory());
        assertEquals(categoryEntity.getId(), product.getCategory().getId());
        assertEquals(categoryEntity.getName(), product.getCategory().getName());
    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        ProductEntity productEntity = null;

        // When
        Product product = converter.toDomain(productEntity);

        // Then
        assertNull(product);
    }

    @Test
    void testToEntity_givenNonNullProduct_shouldConvertCorrectly() {
        // Given
        Category category = Category.builder().id(1L).name("Test Category").build();

        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .category(category)
                .productAttributes(new ArrayList<>())
                .build();

        when(categoryConverter.toEntity(category)).thenReturn(
                CategoryEntity.builder().id(1L).name("Test Category").build());

        // When
        ProductEntity productEntity = converter.toEntity(product);

        // Then
        assertNotNull(productEntity);
        assertEquals(product.getId(), productEntity.getId());
        assertEquals(product.getName(), productEntity.getName());
        assertEquals(product.getDescription(), productEntity.getDescription());
        assertNotNull(productEntity.getCategory());
        assertEquals(category.getId(), productEntity.getCategory().getId());
        assertEquals(category.getName(), productEntity.getCategory().getName());
    }

    @Test
    void testToEntity_givenNullProduct_shouldReturnNull() {
        // Given
        Product product = null;

        // When
        ProductEntity productEntity = converter.toEntity(product);

        // Then
        assertNull(productEntity);
    }
}

