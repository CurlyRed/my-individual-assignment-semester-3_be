package Marketplace.business;

import Marketplace.business.dto.category.CreateCategoryRequest;
import Marketplace.business.dto.category.CreateCategoryResponse;
import Marketplace.business.impl.CategoryServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private CategoryConverter categoryConverter;
    @Mock
    private AttributeConverter attributeConverter;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createCategory_CreatesCategory(){
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setCategoryName("Test Category");

        List<Attribute> attributes = new ArrayList<>();
        Attribute attribute1 = Attribute.builder()
                .name("Attribute1")
                .build();
        attributes.add(attribute1);
        Attribute attribute2 = Attribute.builder()
                .name("Attribute2")
                .build();
        attributes.add(attribute2);
        request.setAttributes(attributes);

        CategoryEntity savedCategoryEntity = new CategoryEntity();
        savedCategoryEntity.setId(1L);

        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setId(1L);

        //Mock
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(savedCategoryEntity);
        when(attributeConverter.toEntity(any(Attribute.class))).thenReturn(attributeEntity);

        // When
        CreateCategoryResponse response = categoryService.createCategory(request);

        // Verify
        verify(categoryRepository, times(2)).save(any(CategoryEntity.class));
        verify(attributeRepository, times(1)).saveAll(anyList());

        // Then
        assertNotNull(response);
        assertEquals(savedCategoryEntity.getId(), response.getCategoryId());
    }

    @Test
    void getCategory_ReturnsEmptyOptional_WhenCategoryNotFound() {
        // Given
        long categoryId = 1L;

        // Mock
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        Optional<Category> result = categoryService.getCategory(categoryId);

        // Verify
        verify(categoryRepository, times(1)).findById(categoryId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getCategory_ReturnsCategory_WhenCategoryFound() {
        // Given
        long categoryId = 1L;
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .id(categoryId)
                .build();

        Category category = new Category();
        category.setId(categoryId);

        // Mock
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(categoryConverter.toDomain(categoryEntity)).thenReturn(category);

        // When
        Optional<Category> optionalCategory = categoryService.getCategory(categoryId);

        // When
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryConverter, times(1)).toDomain(categoryEntity);

        // Then
        assertTrue(optionalCategory.isPresent());
        assertEquals(categoryEntity.getId(), optionalCategory.get().getId());
    }

    @Test
    void deleteCategory_DeleteCategory_WhenCategoryNotFound() {
        // Given
        long categoryId = 1L;

        // Mock
        doThrow(EmptyResultDataAccessException.class).when(categoryRepository).deleteById(categoryId);

        // When
        boolean deletedCategory = categoryService.deleteCategory(categoryId);

        // Verify
        verify(categoryRepository, times(1)).deleteById(categoryId);

        // Then
        assertFalse(deletedCategory);
    }


    @Test
    void deleteCategory_DeleteCategory_WhenCategoryFound() {
        // Given
        long categoryId = 1L;

        // Mock
        doNothing().when(categoryRepository).deleteById(categoryId);

        // When
        boolean deletedCategory = categoryService.deleteCategory(categoryId);

        // Verify
        verify(categoryRepository, times(1)).deleteById(categoryId);

        // Then
        assertTrue(deletedCategory);
    }

    @Test
    void getCategories_ReturnsEmptyList_WhenCategoriesNotFound(){
        // Mock
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<Category> categories = categoryService.getCategories();

        // Verify
        verify(categoryRepository, times(1)).findAll();

        // Then
        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    void getCategories_ReturnsListOfConvertedCategories_WhenCategoriesFound(){
        // Given
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(CategoryEntity.builder().id(1L).name("Category 1").build());
        categoryEntities.add(CategoryEntity.builder().id(2L).name("Category 2").build());

        // Mock
        when(categoryRepository.findAll()).thenReturn(categoryEntities);
        when(categoryConverter.toDomain(any(CategoryEntity.class)))
                .thenAnswer(invocation -> {
                    CategoryEntity entity = invocation.getArgument(0);
                    return Category.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .build();
                });

        // When
        List<Category> categories = categoryService.getCategories();

        // Verify
        verify(categoryRepository, times(1)).findAll();
        verify(categoryConverter, times(categoryEntities.size())).toDomain(any(CategoryEntity.class));

        // Then
        for (int i = 0; i < categoryEntities.size(); i++) {
            assertEquals(categoryEntities.get(i).getId(), categories.get(i).getId());
            assertEquals(categoryEntities.get(i).getName(), categories.get(i).getName());
        }
        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        assertEquals(categoryEntities.size(), categories.size());
    }
}
