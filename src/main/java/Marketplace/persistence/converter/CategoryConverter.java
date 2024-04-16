package Marketplace.persistence.converter;

import Marketplace.domain.Category;
import Marketplace.persistence.entity.CategoryEntity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CategoryConverter {

    private final AttributeConverter attributeConverter;

    public Category toDomain(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }

        return Category.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .attributes(categoryEntity.getAttributes().stream()
                        .map(attributeConverter::toDomain)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .build();
    }

    public CategoryEntity toEntity(Category category){
        if(category == null){
            return null;
        }

        return CategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .attributes(category.getAttributes().stream()
                        .map(attributeConverter::toEntity)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .build();
    }
}

