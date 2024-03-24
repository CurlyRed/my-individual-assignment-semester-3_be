package Marketplace.controller;

import Marketplace.business.CategoryService;
import Marketplace.domain.Category.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryController = new CategoryController(categoryService);
    }

    @Test
    void createCategory_ValidRequest_ReturnsCreatedResponse() {
        // Given
        CreateCategoryRequest request = CreateCategoryRequest.builder()
                .categoryName("Test Category")
                .attributes(Collections.emptyList())
                .build();
        CreateCategoryResponse response = CreateCategoryResponse.builder()
                .categoryId(1L)
                .build();
        when(categoryService.createCategory(request)).thenReturn(response);

        // When
        ResponseEntity<CreateCategoryResponse> responseEntity = categoryController.createCategory(request);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void getCategory_ExistingCategoryId_ReturnsCategory() {
        // Given
        long categoryId = 1L;
        Category category = Category.builder()
                .id(categoryId)
                .name("Test Category")
                .attributes(Collections.emptyList())
                .build();
        when(categoryService.getCategory(categoryId)).thenReturn(Optional.of(category));

        // When
        ResponseEntity<Category> responseEntity = categoryController.getCategory(categoryId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(category, responseEntity.getBody());
    }

    @Test
    void getCategory_NonExistingCategoryId_ReturnsNotFound() {
        // Given
        long categoryId = 1L;
        when(categoryService.getCategory(categoryId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Category> responseEntity = categoryController.getCategory(categoryId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertFalse(responseEntity.hasBody());
    }

    @Test
    void updateCategory_ValidRequest_ReturnsNoContent() {
        // Given
        long categoryId = 1L;
        UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                .id(categoryId)
                .attributes(Collections.emptyList())
                .build();

        // When
        ResponseEntity<Void> responseEntity = categoryController.updateCategory(categoryId, request);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void deleteCategory_ExistingCategoryId_ReturnsNoContent() {
        // Given
        int categoryId = 1;

        // When
        ResponseEntity<Void> responseEntity = categoryController.deleteCategory(categoryId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void getCategories_ReturnsListOfCategories() {
        // Given
        List<Category> categories = Collections.singletonList(
                Category.builder()
                        .id(1L)
                        .name("Test Category")
                        .attributes(Collections.emptyList())
                        .build()
        );
        when(categoryService.getCategories()).thenReturn(categories);

        // When
        ResponseEntity<List<Category>> responseEntity = categoryController.getCategories();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categories, responseEntity.getBody());
    }
}

