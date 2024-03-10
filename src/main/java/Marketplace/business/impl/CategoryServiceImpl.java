package Marketplace.business.impl;

import Marketplace.business.CategoryService;
import Marketplace.domain.Category.*;
import Marketplace.persistence.CategoryRepository;

import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;
    private final AttributeConverter attributeConverter;

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
        List<AttributeEntity> attributeEntities = request.getAttributes().stream()
                .map(attributeConverter::convertToEntity)
                .collect(Collectors.toList());

        CategoryEntity createdCategoryEntity = CategoryEntity.builder()
                .name(request.getCategoryName())
                .attributes(attributeEntities)
                .build();

        createdCategoryEntity = categoryRepository.saveCategory(createdCategoryEntity);

        return CreateCategoryResponse.builder()
                .categoryId(createdCategoryEntity.getId())
                .build();
    }

    @Override
    public Optional<Category> getCategory(long categoryId){

        return categoryRepository.getCategory(categoryId)
                        .map(categoryConverter::convertToDomain);
    }

    @Override
    public boolean updateCategory(UpdateCategoryRequest request) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.getCategory(request.getId());

        if (categoryOptional.isPresent()) {
            CategoryEntity category = categoryOptional.get();

            List<AttributeEntity> attributeEntities = request.getAttributes().stream()
                    .map(attributeConverter::convertToEntity)
                    .collect(Collectors.toList());

            category.setAttributes(attributeEntities);

            categoryRepository.saveCategory(category);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteCategory(long categoryId){

        return this.categoryRepository.deleteCategory(categoryId);
    }

    @Override
    public List<Category> getCategories(){
        return this.categoryRepository.getCategories().stream()
                .map(categoryConverter::convertToDomain)
                .collect(Collectors.toList());
    }
}
