package Marketplace.domain;

import static org.junit.jupiter.api.Assertions.*;

import Marketplace.domain.Attribute.Attribute;
import Marketplace.domain.Attribute.AttributeConverter;
import Marketplace.persistence.entity.AttributeEntity;
import org.junit.jupiter.api.Test;

class AttributeConverterTest {

    private final AttributeConverter attributeConverter = new AttributeConverter();

    @Test
    void convertToEntity_ValidAttribute_ReturnsAttributeEntity() {
        // Given
        Attribute attribute = new Attribute("AttributeName", "AttributeType");

        // When
        AttributeEntity attributeEntity = attributeConverter.convertToEntity(attribute);

        // Then
        assertNotNull(attributeEntity);
        assertEquals("AttributeName", attributeEntity.getName());
        assertEquals("AttributeType", attributeEntity.getType());
    }

    @Test
    void convertToDomain_ValidAttributeEntity_ReturnsAttribute() {
        // Given
        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setName("AttributeName");
        attributeEntity.setType("AttributeType");

        // When
        Attribute attribute = attributeConverter.convertToDomain(attributeEntity);

        // Then
        assertNotNull(attribute);
        assertEquals("AttributeName", attribute.getName());
        assertEquals("AttributeType", attribute.getType());
    }
}

