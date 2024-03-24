package Marketplace.domain.Product;

import Marketplace.domain.Attribute.Attribute;
import Marketplace.domain.Category.Category;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {
    private Long id;
    private String name;
    private String description;
    private Category category;
    private Map<String, String> attributeValues = new HashMap<>();

    public Product(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        initializeAttributeValues(category);
    }

    private void initializeAttributeValues(Category category) {
        for (Attribute attribute : category.getAttributes()) {
            attributeValues.put(attribute.getName(), "");
        }
    }
}
