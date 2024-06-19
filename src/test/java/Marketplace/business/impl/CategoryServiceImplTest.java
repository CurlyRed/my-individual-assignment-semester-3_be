package Marketplace.business.impl;

import Marketplace.business.dto.category.CreateCategoryRequest;
import Marketplace.business.dto.category.CreateCategoryResponse;
import Marketplace.business.exception.DuplicateCategoryNameException;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.config.security.token.AccessToken;
import Marketplace.domain.Attribute;
import Marketplace.domain.Category;
import Marketplace.persistence.converter.AttributeConverter;
import Marketplace.persistence.converter.CategoryConverter;
import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;
import Marketplace.persistence.jpaRepository.AttributeRepository;
import Marketplace.persistence.jpaRepository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private AttributeConverter attributeConverter;
    @Mock
    private CategoryConverter categoryConverter;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private CategoryServiceImpl categoriesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCategory_withValidRequest_shouldReturnCreateCategoryResponse() {
        // Given
        CreateCategoryRequest request = createValidCategoryRequest();

        CategoryEntity savedCategoryEntity = new CategoryEntity();
        savedCategoryEntity.setId(1L);

        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setCategory(savedCategoryEntity);

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(true);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(savedCategoryEntity);
        when(attributeConverter.toEntity(any(Attribute.class))).thenReturn(attributeEntity);

        // When
        CreateCategoryResponse response = categoriesService.createCategory(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getCategoryId());

        // Verify
        verify(requestAccessToken, times(1)).hasRole("ADMIN");
        verify(categoryRepository, times(2)).save(any(CategoryEntity.class));
        verify(attributeConverter, times(1)).toEntity(any(Attribute.class));
        verify(attributeRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testCreateCategory_withNullRequest_shouldReturnNull() {
        // When
        CreateCategoryResponse response = categoriesService.createCategory(null);

        // Then
        assertNull(response);

        // Verify
        verifyNoInteractions(requestAccessToken, categoryRepository, attributeRepository, attributeConverter);
    }

    @Test
    public void testCreateCategory_withUnauthorizedUser_shouldThrowException() {
        // Given
        CreateCategoryRequest request = createValidCategoryRequest();

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(false);

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> categoriesService.createCategory(request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(requestAccessToken, times(1)).hasRole("ADMIN");
        verifyNoInteractions(categoryRepository, attributeRepository, attributeConverter);
    }

    @Test
    void testCreateCategory_withDuplicateCategoryName_shouldThrowException() {
        // Given
        CreateCategoryRequest request = createValidCategoryRequest();
        CategoryEntity existingCategoryEntity = new CategoryEntity();
        existingCategoryEntity.setName(request.getCategoryName());

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(true);
        when(categoryRepository.findByName(request.getCategoryName())).thenReturn(Optional.of(existingCategoryEntity));

        // When & Then
        assertThrows(DuplicateCategoryNameException.class, () -> categoriesService.createCategory(request));

        // Verify
        verify(categoryRepository, times(1)).findByName(request.getCategoryName());
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(attributeRepository);
    }

    @Test
    public void testGetCategory_withExistingCategory_shouldReturnCategory() {
        // Given
        long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();
        Category category = new Category();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(categoryConverter.toDomain(categoryEntity)).thenReturn(category);

        // When
        Optional<Category> result = categoriesService.getCategory(categoryId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(category, result.get());

        // Verify
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryConverter, times(1)).toDomain(categoryEntity);
    }

    @Test
    public void testGetCategory_withNonExistentCategory_shouldReturnEmpty() {
        // Given
        long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        Optional<Category> result = categoriesService.getCategory(categoryId);

        // Then
        assertFalse(result.isPresent());

        // Verify
        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoInteractions(categoryConverter);
    }

    @Test
    public void testDeleteCategory_withExistingCategory_shouldReturnTrue() {
        // Given
        long categoryId = 1L;

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(true);

        // When
        boolean result = categoriesService.deleteCategory(categoryId);

        // Then
        assertTrue(result);

        // Verify
        verify(requestAccessToken, times(1)).hasRole("ADMIN");
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    public void testDeleteCategory_withNonExistentCategory_shouldReturnFalse() {
        // Given
        long categoryId = 1L;

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(true);
        doThrow(new EmptyResultDataAccessException(1)).when(categoryRepository).deleteById(categoryId);

        // When
        boolean result = categoriesService.deleteCategory(categoryId);

        // Then
        assertFalse(result);

        // Verify
        verify(requestAccessToken, times(1)).hasRole("ADMIN");
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    public void testDeleteCategory_withUnauthorizedUser_shouldThrowException() {
        // Given
        long categoryId = 1L;

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(false);

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> categoriesService.deleteCategory(categoryId));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(requestAccessToken, times(1)).hasRole("ADMIN");
        verifyNoInteractions(categoryRepository);
    }

    @Test
    public void testGetCategories_withExistingCategories_shouldReturnCategoryList() {
        // Given
        CategoryEntity categoryEntity1 = new CategoryEntity();
        CategoryEntity categoryEntity2 = new CategoryEntity();
        Category category1 = new Category();
        Category category2 = new Category();

        List<CategoryEntity> categoryEntities = Arrays.asList(categoryEntity1, categoryEntity2);
        when(categoryRepository.findAll()).thenReturn(categoryEntities);
        when(categoryConverter.toDomain(categoryEntity1)).thenReturn(category1);
        when(categoryConverter.toDomain(categoryEntity2)).thenReturn(category2);

        // When
        List<Category> result = categoriesService.getCategories();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(category1, result.get(0));
        assertEquals(category2, result.get(1));

        // Verify
        verify(categoryRepository, times(1)).findAll();
        verify(categoryConverter, times(2)).toDomain(categoryEntity1);
        verify(categoryConverter, times(2)).toDomain(categoryEntity2);
    }

    @Test
    public void testGetCategories_withNoCategories_shouldReturnEmptyList() {
        // Given
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Category> result = categoriesService.getCategories();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify
        verify(categoryRepository, times(1)).findAll();
        verify(categoryConverter, times(0)).toDomain(any(CategoryEntity.class));
    }

    private CreateCategoryRequest createValidCategoryRequest() {
        return CreateCategoryRequest.builder()
                .categoryName("Test Category")
                .attributes(Collections.singletonList(
                        Attribute.builder()
                                .name("Test Attribute")
                                .build()
                ))
                .build();
    }
}
