package Marketplace.persistence.converter;

import Marketplace.domain.ProductAttribute;
import Marketplace.persistence.entity.ProductAttributeEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductAttributeConverter {
    public ProductAttribute toDomain(ProductAttributeEntity productAttributeEntity){
        if(productAttributeEntity == null){
            return null;
        }

        return ProductAttribute.builder()
                .id(productAttributeEntity.getId())
                .value(productAttributeEntity.getValue())
                .build();
    }

    public ProductAttributeEntity toEntity(ProductAttribute productAttribute){
        if(productAttribute == null){
            return null;
        }

        return ProductAttributeEntity.builder()
                .id(productAttribute.getId())
                .value(productAttribute.getValue())
                .build();
    }
}
