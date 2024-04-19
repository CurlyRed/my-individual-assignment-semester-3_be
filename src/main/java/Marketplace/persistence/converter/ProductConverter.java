package Marketplace.persistence.converter;

import Marketplace.domain.Product;
import Marketplace.persistence.entity.ProductEntity;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductConverter {

    private final CategoryConverter categoryConverter;
    private final ProductAttributeConverter productAttributeConverter;
    public Product toDomain(ProductEntity productEntity){
        if(productEntity == null){
            return null;
        }

        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .category(categoryConverter.toDomain(productEntity.getCategory()))
                .productAttributes(productEntity.getProduct_attributes().stream()
                        .map(productAttributeConverter::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public ProductEntity toEntity(Product product){
        if(product == null){
            return null;
        }

        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(categoryConverter.toEntity(product.getCategory()))
                .product_attributes(product.getProductAttributes().stream()
                        .map(productAttributeConverter::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}


