package Marketplace.business.impl;

import Marketplace.domain.Attribute.Attribute;
import Marketplace.domain.Category.Category;
import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class AttributeConverter {
    public AttributeEntity convertToEntity(Attribute attribute){
        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setName(attribute.getName());
        attributeEntity.setType(attribute.getType());

        return attributeEntity;
    }

    public Attribute convertToDomain(AttributeEntity attributeEntity){
        Attribute attribute = new Attribute();
        attribute.setName(attributeEntity.getName());
        attribute.setType(attributeEntity.getType());

        return attribute;
    }
}
