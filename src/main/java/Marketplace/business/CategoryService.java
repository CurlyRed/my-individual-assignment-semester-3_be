package Marketplace.business;

import Marketplace.domain.Category.CreateCategoryRequest;
import Marketplace.domain.Category.CreateCategoryResponse;
import Marketplace.domain.Category.UpdateCategoryRequest;
import Marketplace.domain.Category.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CreateCategoryResponse createCategory(CreateCategoryRequest request);

    Optional<Category> getCategory(long categoryId);

    boolean updateCategory(UpdateCategoryRequest request);

    boolean deleteCategory(long categoryId);

    List<Category> getCategories();

}

