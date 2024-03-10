package Marketplace.business.impl;

import Marketplace.domain.Attribute.Attribute;
import Marketplace.domain.Category.Category;
import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CategoryConverter {

    private final AttributeConverter attributeConverter;

    public CategoryEntity convertToEntity(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(category.getId());
        categoryEntity.setName(category.getName());
        List<AttributeEntity> attributeEntities = category.getAttributes().stream()
                .map(attributeConverter::convertToEntity)
                .collect(Collectors.toList());
        categoryEntity.setAttributes(attributeEntities);
        return categoryEntity;
    }

    public Category convertToDomain(CategoryEntity categoryEntity) {
        Category category = new Category();
        category.setId(categoryEntity.getId());
        category.setName(categoryEntity.getName());
        List<Attribute> attributes = categoryEntity.getAttributes().stream()
                .map(attributeConverter::convertToDomain)
                .collect(Collectors.toList());
        category.setAttributes(attributes);
        return category;
    }
}

