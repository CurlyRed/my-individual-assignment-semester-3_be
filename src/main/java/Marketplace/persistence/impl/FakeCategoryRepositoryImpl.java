package Marketplace.persistence.impl;

import Marketplace.persistence.CategoryRepository;
import Marketplace.persistence.entity.CategoryEntity;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FakeCategoryRepositoryImpl implements CategoryRepository {

    private static long NEXT_ID = 1;
    private final List<CategoryEntity> categories;

    public FakeCategoryRepositoryImpl() {
        this.categories = new ArrayList<>();
    }

    @Override
    public CategoryEntity saveCategory(CategoryEntity category) {
        if (category.getId() == null) {
            category.setId(NEXT_ID);
            NEXT_ID++;
            this.categories.add(category);
        }
        return category;
    }

    @Override
    public boolean deleteCategory(long categoryId) {
        return this.categories.removeIf(categoryEntity -> categoryEntity.getId().equals(categoryId));
    }

    @Override
    public Optional<CategoryEntity> getCategory(long categoryId) {
        return this.categories.stream()
                .filter(categoryEntity -> categoryEntity.getId().equals(categoryId))
                .findFirst();
    }

    @Override
    public List<CategoryEntity> getCategories() {
        return new ArrayList<>(this.categories);
    }
}

