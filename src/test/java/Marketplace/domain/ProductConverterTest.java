package Marketplace.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Optional;

import Marketplace.business.CategoryService;
import Marketplace.domain.Category.Category;
import Marketplace.domain.Product.Product;
import Marketplace.domain.Product.ProductConverter;
import Marketplace.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductConverterTest {

    @Mock
    private CategoryService categoryService;

    private ProductConverter productConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productConverter = new ProductConverter(categoryService);
    }

    @Test
    void convertToEntity_ValidProduct_ReturnsProductEntity() {
        // Given
        Product product = new Product(1L, "TestProduct", "Description", null, new HashMap<>());

        // When
        ProductEntity productEntity = productConverter.convertToEntity(product);

        // Then
        assertNotNull(productEntity);
        assertEquals(1L, productEntity.getId());
        assertEquals("TestProduct", productEntity.getName());
        assertEquals("Description", productEntity.getDescription());
        assertNull(productEntity.getCategoryId());
        assertTrue(productEntity.getAttributeValues().isEmpty());
    }

    @Test
    void convertToDomain_ValidProductEntity_ReturnsProduct() {
        // Given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("TestProduct");
        productEntity.setDescription("Description");
        productEntity.setCategoryId(1L);
        productEntity.setAttributeValues(new HashMap<>());
        Category category = new Category(1L, "TestCategory", null);
        when(categoryService.getCategory(1L)).thenReturn(Optional.of(category));

        // When
        Product product = productConverter.convertToDomain(productEntity);

        // Then
        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("TestProduct", product.getName());
        assertEquals("Description", product.getDescription());
        assertEquals(category, product.getCategory());
        assertTrue(product.getAttributeValues().isEmpty());
    }
}
