package Marketplace.persistence.converter;

import Marketplace.domain.Attribute;
import Marketplace.domain.Category;
import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

    class CategoryConverterTest {

    private CategoryConverter converter;

    @Mock
    private AttributeConverter attributeConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new CategoryConverter(attributeConverter);
    }

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setId(1L);
        attributeEntity.setName("Test Attribute");

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Test Category");
        categoryEntity.setAttributes(Arrays.asList(attributeEntity));

        when(attributeConverter.toDomain(attributeEntity)).thenReturn(new Attribute(1L, "Test Attribute"));

        // When
        Category category = converter.toDomain(categoryEntity);

        // Then
        assertNotNull(category);
        assertEquals(categoryEntity.getId(), category.getId());
        assertEquals(categoryEntity.getName(), category.getName());

        List<Attribute> attributes = category.getAttributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        assertEquals(attributeEntity.getId(), attributes.get(0).getId());
        assertEquals(attributeEntity.getName(), attributes.get(0).getName());
    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        CategoryEntity categoryEntity = null;

        // When
        Category category = converter.toDomain(categoryEntity);

        // Then
        assertNull(category);
    }

    @Test
    void testToEntity_givenNonNullCategory_shouldConvertCorrectly() {
        // Given
        Attribute attribute = new Attribute(1L, "Test Attribute");

        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setAttributes(Arrays.asList(attribute));

        when(attributeConverter.toEntity(attribute)).thenReturn(
                AttributeEntity.builder().id(1L).name("Test Attribute").build()
        );

        // When
        CategoryEntity categoryEntity = converter.toEntity(category);

        // Then
        assertNotNull(categoryEntity);
        assertEquals(category.getId(), categoryEntity.getId());
        assertEquals(category.getName(), categoryEntity.getName());

        List<AttributeEntity> attributeEntities = categoryEntity.getAttributes();
        assertNotNull(attributeEntities);
        assertEquals(1, attributeEntities.size());
        assertEquals(attribute.getId(), attributeEntities.get(0).getId());
        assertEquals(attribute.getName(), attributeEntities.get(0).getName());
    }

    @Test
    void testToEntity_givenNullCategory_shouldReturnNull() {
        // Given
        Category category = null;

        // When
        CategoryEntity categoryEntity = converter.toEntity(category);

        // Then
        assertNull(categoryEntity);
    }
}
