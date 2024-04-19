package Marketplace.persistence.converter;

import Marketplace.domain.ProductAttribute;
import Marketplace.persistence.entity.ProductAttributeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductAttributeConverterTest {

    private ProductAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ProductAttributeConverter();
    }

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        ProductAttributeEntity entity = new ProductAttributeEntity();
        entity.setId(1L);
        entity.setValue("Test Value");

        // When
        ProductAttribute attribute = converter.toDomain(entity);

        // Then
        assertNotNull(attribute);
        assertEquals(entity.getId(), attribute.getId());
        assertEquals(entity.getValue(), attribute.getValue());
    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        ProductAttributeEntity entity = null;

        // When
        ProductAttribute attribute = converter.toDomain(entity);

        // Then
        assertNull(attribute);
    }

    @Test
    void testToEntity_givenNonNullAttribute_shouldConvertCorrectly() {
        // Given
        ProductAttribute attribute = new ProductAttribute();
        attribute.setId(1L);
        attribute.setValue("Test Value");

        // When
        ProductAttributeEntity entity = converter.toEntity(attribute);

        // Then
        assertNotNull(entity);
        assertEquals(attribute.getId(), entity.getId());
        assertEquals(attribute.getValue(), entity.getValue());
    }

    @Test
    void testToEntity_givenNullAttribute_shouldReturnNull() {
        // Given
        ProductAttribute attribute = null;

        // When
        ProductAttributeEntity entity = converter.toEntity(attribute);

        // Then
        assertNull(entity);
    }
}

