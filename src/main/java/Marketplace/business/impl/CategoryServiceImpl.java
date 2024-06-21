package Marketplace.business.impl;

import Marketplace.business.CategoryService;
import Marketplace.business.dto.category.CreateCategoryRequest;
import Marketplace.business.dto.category.CreateCategoryResponse;
import Marketplace.business.exception.DuplicateCategoryNameException;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.config.security.token.AccessToken;
import Marketplace.domain.Category;
import Marketplace.persistence.converter.AttributeConverter;
import Marketplace.persistence.converter.CategoryConverter;
import Marketplace.persistence.jpaRepository.AttributeRepository;
import Marketplace.persistence.jpaRepository.CategoryRepository;

import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class
CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final CategoryConverter categoryConverter;
    private final AttributeConverter attributeConverter;
    private final AccessToken requestAccessToken;

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest request) {
        if (request == null) {
            return null;
        }


        if (!requestAccessToken.hasRole("ADMIN")){
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        if(categoryRepository.findByName(request.getCategoryName()).isPresent()){
            throw new DuplicateCategoryNameException("This category name already exists.");
        }

        CategoryEntity categoryEntity = CategoryEntity.builder()
                .name(request.getCategoryName())
                .build();
        final CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);

        List<AttributeEntity> attributeEntities = request.getAttributes().stream()
                .map(attribute -> {
                    AttributeEntity attributeEntity = attributeConverter.toEntity(attribute);
                    attributeEntity.setCategory(savedCategoryEntity);
                    return attributeEntity;
                })
                .toList();
        attributeRepository.saveAll(attributeEntities);

        savedCategoryEntity.setAttributes(attributeEntities);
        categoryRepository.save(savedCategoryEntity);

        return CreateCategoryResponse.builder()
                .categoryId(savedCategoryEntity.getId())
                .build();
    }

    @Override
    public Optional<Category> getCategory(long categoryId){
        return categoryRepository.findById(categoryId)
                .map(categoryConverter::toDomain);
    }

    @Override
    public boolean deleteCategory(long categoryId){
        if (!requestAccessToken.hasRole("ADMIN")){
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }
        try {
            categoryRepository.deleteById(categoryId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public List<Category> getCategories(){
        return categoryRepository.findAll().stream()
                .map(categoryConverter::toDomain)
                .toList();
    }
}

