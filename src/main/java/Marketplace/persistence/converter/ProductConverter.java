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
    private final ContactInformationConverter contactInformationConverter;

    public Product toDomain(ProductEntity productEntity){
        if(productEntity == null){
            return null;
        }

        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .date_of_post(productEntity.getDate_of_post())
                .promoted(productEntity.getPromoted())
                .category(categoryConverter.toDomain(productEntity.getCategory()))
                .productAttributes(productEntity.getProduct_attributes().stream()
                        .map(productAttributeConverter::toDomain)
                        .collect(Collectors.toList()))
                .contact_information(contactInformationConverter.toDomain(productEntity.getContact_information()))
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
                .price(product.getPrice())
                .date_of_post(product.getDate_of_post())
                .promoted(product.getPromoted())
                .category(categoryConverter.toEntity(product.getCategory()))
                .product_attributes(product.getProductAttributes().stream()
                        .map(productAttributeConverter::toEntity)
                        .collect(Collectors.toList()))
                .contact_information(contactInformationConverter.toEntity(product.getContact_information()))
                .build();
    }
}


