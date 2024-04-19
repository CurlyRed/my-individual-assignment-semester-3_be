package Marketplace.business.impl;

import Marketplace.business.ProductService;
import Marketplace.business.dto.product.CreateProductRequest;
import Marketplace.business.dto.product.CreateProductResponse;
import Marketplace.domain.Attribute;
import Marketplace.domain.Product;
import Marketplace.domain.ProductAttribute;
import Marketplace.persistence.converter.ProductConverter;
import Marketplace.persistence.entity.*;
import Marketplace.persistence.jpaRepository.*;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final ProductConverter productConverter;
    private final ProductAttributeRepository productAttributeRepository;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest request) {
        if (request == null) {
            return null;
        }

        CategoryEntity categoryEntity = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        CityEntity cityEntity = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found"));
        UserEntity userEntity = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ProductEntity productEntity = ProductEntity.builder()
                .name(request.getProductName())
                .description(request.getProductDescription())
                .category(categoryEntity)
                .city(cityEntity)
                .user(userEntity)
                .build();

        final ProductEntity savedProductEntity = productRepository.save(productEntity);

        List<ProductAttributeEntity> productAttributes = new ArrayList<>();
        for (int i = 0; i < request.getAttributes().size(); i++) {
            AttributeEntity attribute = categoryEntity.getAttributes().get(i);
            String value = request.getAttributes().get(i).getValue();

            ProductAttributeEntity productAttributeEntity = ProductAttributeEntity.builder()
                    .value(value)
                    .attribute(attribute)
                    .product(savedProductEntity)
                    .build();
            productAttributes.add(productAttributeEntity);
        }

        productAttributeRepository.saveAll(productAttributes);

        savedProductEntity.setProduct_attributes(productAttributes);
        productRepository.save(savedProductEntity);

        return CreateProductResponse.builder()
                .productId(savedProductEntity.getId())
                .categoryId(savedProductEntity.getCategory().getId())
                .build();
    }

    @Override
    public Optional<Product> getProduct(long productId){
        return productRepository.findById(productId)
                .map(productConverter::toDomain);
    }

    @Override
    public boolean deleteProduct(long productId){
        try {
            productRepository.deleteById(productId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public List<Product> getProducts() {
        return this.productRepository.findAll().stream()
                .map(productConverter::toDomain)
                .collect(Collectors.toList());
    }
}
