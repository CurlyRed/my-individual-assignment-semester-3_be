package Marketplace.persistence.converter;

import Marketplace.domain.Attribute;
import Marketplace.persistence.entity.AttributeEntity;

import org.springframework.stereotype.Component;

@Component
public class AttributeConverter {
    public Attribute toDomain(AttributeEntity attributeEntity){
        if(attributeEntity == null){
            return null;
        }
        return Attribute.builder()
                .id(attributeEntity.getId())
                .name(attributeEntity.getName())
                .build();
    }

    public AttributeEntity toEntity(Attribute attribute){
        if(attribute == null){
            return null;
        }

        return AttributeEntity.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .build();
    }
}
