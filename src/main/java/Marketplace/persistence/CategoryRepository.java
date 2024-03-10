package Marketplace.persistence;

import Marketplace.persistence.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    CategoryEntity saveCategory(CategoryEntity categoryEntity);

    boolean deleteCategory(long categoryId);

    Optional<CategoryEntity> getCategory(long categoryId);

    List<CategoryEntity> getCategories();

}
