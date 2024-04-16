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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void save_shouldSaveCategoryWithAttributes(){
        List<AttributeEntity> attributes = new ArrayList<>();
        attributes.add(AttributeEntity.builder().name("Test1").build());
        attributes.add(AttributeEntity.builder().name("Test2").build());

        CategoryEntity category = CategoryEntity.builder()
                .name("CategoryTest")
                .attributes(attributes)
                .build();

        CategoryEntity savedCategory = categoryRepository.save(category);
        assertNotNull(savedCategory.getId());

        savedCategory = entityManager.find(CategoryEntity.class, savedCategory.getId());
        CategoryEntity expectedCategory = CategoryEntity.builder()
                .id(1L)
                .name("CategoryTest")
                .attributes(attributes)
                .build();
        assertEquals(expectedCategory, savedCategory);
    }
}
