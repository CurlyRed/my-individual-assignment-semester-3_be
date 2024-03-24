package Marketplace.domain.Product;

import Marketplace.persistence.entity.ProductEntity;
import Marketplace.domain.Category.Category;
import Marketplace.business.CategoryService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ProductConverter {

    private final CategoryService categoryService;

    public ProductEntity convertToEntity(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(product.getId());
        productEntity.setName(product.getName());
        productEntity.setDescription(product.getDescription());
        if (product.getCategory() != null) {
            productEntity.setCategoryId(product.getCategory().getId());
        }
        productEntity.setAttributeValues(product.getAttributeValues());
        return productEntity;
    }

    public Product convertToDomain(ProductEntity productEntity) {
        Product product = new Product();
        product.setId(productEntity.getId());
        product.setName(productEntity.getName());
        product.setDescription(productEntity.getDescription());

        Optional<Category> categoryOptional = categoryService.getCategory(productEntity.getCategoryId());
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            product.setCategory(category);
        }

        for (Map.Entry<String, String> entry : productEntity.getAttributeValues().entrySet()) {
            String attributeName = entry.getKey();
            String attributeValue = entry.getValue();
            product.getAttributeValues().put(attributeName, attributeValue);
        }
        return product;
    }
}


