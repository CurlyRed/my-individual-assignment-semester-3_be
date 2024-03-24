package Marketplace.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import Marketplace.domain.Attribute.Attribute;
import Marketplace.domain.Attribute.AttributeConverter;
import Marketplace.domain.Category.Category;
import Marketplace.domain.Category.CategoryConverter;
import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CategoryConverterTest {

    @Mock
    private AttributeConverter attributeConverter;

    private CategoryConverter categoryConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryConverter = new CategoryConverter(attributeConverter);
    }

    @Test
    void convertToEntity_ValidCategory_ReturnsCategoryEntity() {
        // Given
        Category category = new Category(1L, "TestCategory", Collections.emptyList());
        when(attributeConverter.convertToEntity(any())).thenReturn(new AttributeEntity());

        // When
        CategoryEntity categoryEntity = categoryConverter.convertToEntity(category);

        // Then
        assertNotNull(categoryEntity);
        assertEquals(1L, categoryEntity.getId());
        assertEquals("TestCategory", categoryEntity.getName());
        assertEquals(Collections.emptyList(), categoryEntity.getAttributes());
    }

    @Test
    void convertToDomain_ValidCategoryEntity_ReturnsCategory() {
        // Given
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("TestCategory");
        categoryEntity.setAttributes(Collections.emptyList());
        when(attributeConverter.convertToDomain(any())).thenReturn(new Attribute());

        // When
        Category category = categoryConverter.convertToDomain(categoryEntity);

        // Then
        assertNotNull(category);
        assertEquals(1L, category.getId());
        assertEquals("TestCategory", category.getName());
        assertEquals(Collections.emptyList(), category.getAttributes());
    }
}

