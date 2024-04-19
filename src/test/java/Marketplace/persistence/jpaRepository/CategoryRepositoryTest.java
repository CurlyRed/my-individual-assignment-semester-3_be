package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AttributeRepository attributeRepository;

    @Test
    void save_shouldSaveCategoryWithAttributes(){
        // Given
        List<AttributeEntity> attributes = new ArrayList<>();
        attributes.add(AttributeEntity.builder().name("Test1").build());
        attributes.add(AttributeEntity.builder().name("Test2").build());

        CategoryEntity category = CategoryEntity.builder()
                .name("CategoryTest")
                .attributes(attributes)
                .build();

        // When
        CategoryEntity savedCategory = categoryRepository.save(category);

        // Then
        assertEquals(category, savedCategory);
    }

    @Test
    void delete_shouldDeleteCategoryWithAttributes(){
        // Given
        List<AttributeEntity> attributes = new ArrayList<>();
        attributes.add(AttributeEntity.builder().name("Test1").build());
        attributes.add(AttributeEntity.builder().name("Test2").build());

        CategoryEntity category = CategoryEntity.builder()
                .name("CategoryTest")
                .attributes(attributes)
                .build();

        CategoryEntity savedCategory = categoryRepository.save(category);

        // When
        categoryRepository.deleteById(savedCategory.getId());

        // Then
        assertFalse(categoryRepository.findById(savedCategory.getId()).isPresent());

        for (AttributeEntity attribute : attributes){
            assertFalse(attributeRepository.findById(attribute.getId()).isPresent());
        }
    }

    @Test
    void findById_shouldReturnCategoryWithAttributes(){
        // Given
        List<AttributeEntity> attributes = new ArrayList<>();
        attributes.add(AttributeEntity.builder().name("Test1").build());
        attributes.add(AttributeEntity.builder().name("Test2").build());

        CategoryEntity category = CategoryEntity.builder()
                .name("CategoryTest")
                .attributes(attributes)
                .build();

        CategoryEntity savedCategory = categoryRepository.save(category);

        // When
        Optional<CategoryEntity> foundCategoryOptional = categoryRepository.findById(savedCategory.getId());

        // Then
        assertTrue(foundCategoryOptional.isPresent());
        CategoryEntity foundCategory = foundCategoryOptional.get();
        assertEquals(category.getName(), foundCategory.getName());
        assertEquals(category.getAttributes().size(), foundCategory.getAttributes().size());
        assertTrue(foundCategory.getAttributes().containsAll(category.getAttributes()));
    }

    @Test
    void findAll_shouldReturnAllCategoriesWithAttributes(){
        // Given
        List<AttributeEntity> attributes1 = new ArrayList<>();
        attributes1.add(AttributeEntity.builder().name("Test1").build());
        attributes1.add(AttributeEntity.builder().name("Test2").build());
        CategoryEntity category1 = CategoryEntity.builder()
                .name("Category1")
                .attributes(attributes1)
                .build();
        category1 = categoryRepository.save(category1);

        List<AttributeEntity> attributes2 = new ArrayList<>();
        attributes2.add(AttributeEntity.builder().name("Test3").build());
        attributes2.add(AttributeEntity.builder().name("Test4").build());
        CategoryEntity category2 = CategoryEntity.builder()
                .name("Category2")
                .attributes(attributes2)
                .build();
        category2 = categoryRepository.save(category2);

        // When
        List<CategoryEntity> allCategories = categoryRepository.findAll();

        // Then
        assertEquals(2, allCategories.size());
        assertTrue(allCategories.contains(category1));
        assertTrue(allCategories.contains(category2));
    }
}
