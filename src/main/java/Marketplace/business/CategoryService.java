package Marketplace.business;

import Marketplace.business.dto.category.CreateCategoryRequest;
import Marketplace.business.dto.category.CreateCategoryResponse;
import Marketplace.business.dto.category.UpdateCategoryRequest;
import Marketplace.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CreateCategoryResponse createCategory(CreateCategoryRequest request);

    Optional<Category> getCategory(long categoryId);

    boolean deleteCategory(long categoryId);

    List<Category> getCategories();

}

