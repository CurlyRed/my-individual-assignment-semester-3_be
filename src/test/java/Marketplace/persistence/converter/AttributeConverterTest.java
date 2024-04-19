package Marketplace.persistence.converter;

import Marketplace.domain.Attribute;
import Marketplace.persistence.entity.AttributeEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AttributeConverterTest {

    final private AttributeConverter converter = new AttributeConverter();

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        AttributeEntity entity = new AttributeEntity();
        entity.setId(1L);
        entity.setName("Test Attribute");

        // When
        Attribute attribute = converter.toDomain(entity);

        // Then
        assertNotNull(attribute);
        assertEquals(entity.getId(), attribute.getId());
        assertEquals(entity.getName(), attribute.getName());
    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        AttributeEntity entity = null;

        // When
        Attribute attribute = converter.toDomain(entity);

        // Then
        assertNull(attribute);
    }

    @Test
    void testToEntity_givenNonNullAttribute_shouldConvertCorrectly() {
        // Given
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("Test Attribute");

        // When
        AttributeEntity entity = converter.toEntity(attribute);

        // Then
        assertNotNull(entity);
        assertEquals(attribute.getId(), entity.getId());
        assertEquals(attribute.getName(), entity.getName());
    }

    @Test
    void testToEntity_givenNullAttribute_shouldReturnNull() {
        // Given
        Attribute attribute = null;

        // When
        AttributeEntity entity = converter.toEntity(attribute);

        // Then
        assertNull(entity);
    }
}
