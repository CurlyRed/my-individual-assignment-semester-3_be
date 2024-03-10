package Marketplace.persistence.entity;


import Marketplace.domain.Category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private Map<String, String> attributeValues = new HashMap<>();
}