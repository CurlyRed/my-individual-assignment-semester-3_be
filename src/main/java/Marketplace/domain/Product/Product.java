package Marketplace.domain.Product;

import Marketplace.domain.Attribute.Attribute;
import Marketplace.domain.Category.Category;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Product {
    private String name;
    private Category category;
    private Map<String, String> attributeValues = new HashMap<>();

    public Product(String name, Category category) {
        this.name = name;
        this.category = category;
        initializeAttributeValues(category);
    }

    private void initializeAttributeValues(Category category) {
        for (Attribute attribute : category.getAttributes()) {
            attributeValues.put(attribute.getName(), "");
        }
        for (Category subcategory : category.getSubcategories()) {
            initializeAttributeValues(subcategory);
        }
    }
}
