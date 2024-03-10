package Marketplace.config.db;

import Marketplace.persistence.CategoryRepository;

import Marketplace.persistence.entity.AttributeEntity;
import Marketplace.persistence.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DatabaseInitializer{

    private CategoryRepository categoryRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void populateInitialDummyData(){
        AttributeEntity attribute1 = AttributeEntity.builder().name("Engine").type("String").build();
        AttributeEntity attribute2 = AttributeEntity.builder().name("Wheels").type("String").build();
        List<AttributeEntity> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);

        categoryRepository.saveCategory(CategoryEntity.builder().id(1L).name("Car").attributes(attributes).build());
    }
}

