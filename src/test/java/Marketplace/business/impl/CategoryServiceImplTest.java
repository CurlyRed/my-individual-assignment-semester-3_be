package Marketplace.business.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Marketplace.business.CategoryService;
import Marketplace.domain.Attribute.Attribute;
import Marketplace.domain.Attribute.AttributeConverter;
import Marketplace.domain.Category.*;
import Marketplace.persistence.CategoryRepository;

import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryConverter categoryConverter;

    @Mock
    private AttributeConverter attributeConverter;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp(){MockitoAnnotations.openMocks(this);}

    @Test
    void createCategory_ValidRequest_ReturnsCreateCategoryResponse(){
        // Given
        CreateCategoryRequest request = createCategoryRequest("TestCategory", Collections.emptyList());
        CategoryEntity createdCategoryEntity = createCategoryEntity(1L, "TestCategory", Collections.emptyList());
        when(attributeConverter.convertToEntity(any())).thenReturn(new AttributeEntity());
        when(categoryRepository.saveCategory(any())).thenReturn(createdCategoryEntity);

        // When
        CreateCategoryResponse response = categoryService.createCategory(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getCategoryId());
    }

    @Test
    void createCategory_NullRequest_ReturnsNull() {
        // Given
        CreateCategoryRequest request = null;

        // When
        CreateCategoryResponse response = categoryService.createCategory(request);

        // Then
        assertNull(response);
    }

    @Test
    void getCategory_ExistingId_ReturnsCategory(){
        // Given
        long categoryId = 1L;
        CategoryEntity categoryEntity = createCategoryEntity(categoryId, "TestCategory", Collections.emptyList());
        when(categoryRepository.getCategory(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(categoryConverter.convertToDomain(categoryEntity)).thenReturn(new Category());

        // When
        Optional<Category> categoryOptional = categoryService.getCategory(categoryId);

        // Then
        assertTrue(categoryOptional.isPresent());
    }

    @Test
    void getCategory_NonExistingId_ReturnsEmptyOptional(){
        // Given
        long nonExistingCategoryId = 100L;
        when(categoryRepository.getCategory(nonExistingCategoryId)).thenReturn(Optional.empty());

        // When
        Optional<Category> categoryOptional = categoryService.getCategory(nonExistingCategoryId);

        // Then
        assertTrue(categoryOptional.isEmpty());
    }

    @Test
    void updateCategory_ExistingCategory_ReturnsTrue(){
        // Given
        UpdateCategoryRequest request = createUpdateCategoryRequest(1L, Collections.emptyList());
        CategoryEntity categoryEntity = createCategoryEntity(1L, "TestCategory", Collections.emptyList());
        when(categoryRepository.getCategory(request.getId())).thenReturn(Optional.of(categoryEntity));
        when(attributeConverter.convertToEntity(any())).thenReturn(new AttributeEntity());

        // When
        boolean result = categoryService.updateCategory(request);

        // Then
        assertTrue(result);
    }

    @Test
    void updateCategory_NonExistingCategory_ReturnsFalse(){
        // Given
        UpdateCategoryRequest request = createUpdateCategoryRequest(100L, Collections.emptyList());
        long nonExistingCategoryId = 10L;
        when(categoryRepository.getCategory(nonExistingCategoryId)).thenReturn(Optional.empty());

        // When
        boolean result = categoryService.updateCategory(request);

        // Then
        assertFalse(result);
    }

    @Test
    void deleteCategory_ExistingCategoryId_ReturnsTrue(){
        // Given
        long categoryId = 1L;
        when(categoryRepository.deleteCategory(categoryId)).thenReturn(true);

        // When
        boolean result = categoryService.deleteCategory(categoryId);

        // Then
        assertTrue(result);
    }

    @Test
    void deleteCategory_NonExistingCategoryId_ReturnsFalse(){
        // Given
        long nonExistingCategoryId = 100L;
        when(categoryRepository.deleteCategory(nonExistingCategoryId)).thenReturn(false);

        // When
        boolean result = categoryService.deleteCategory(nonExistingCategoryId);

        // Then
        assertFalse(result);
    }

    @Test
    void getCategories_ReturnsListOfCategories(){
        // Given
        CategoryEntity categoryEntity = createCategoryEntity(1L, "TestCategory", Collections.emptyList());
        when(categoryRepository.getCategories()).thenReturn(Collections.singletonList(categoryEntity));
        when(categoryConverter.convertToDomain(categoryEntity)).thenReturn(new Category());

        // When
        List<Category> categories = categoryService.getCategories();

        // Then
        assertFalse(categories.isEmpty());
    }

    private CreateCategoryRequest createCategoryRequest(String name, List<Attribute> attributes) {
        return new CreateCategoryRequest(name, attributes);
    }

    private CategoryEntity createCategoryEntity(Long id, String name, List<AttributeEntity> attributes) {
        return new CategoryEntity(id, name, attributes);
    }

    private UpdateCategoryRequest createUpdateCategoryRequest(Long id, List<Attribute> attributes) {
        return new UpdateCategoryRequest(id, attributes);
    }

}
