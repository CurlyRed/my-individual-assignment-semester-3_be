package Marketplace.domain.Category;

import Marketplace.domain.Attribute.Attribute;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class Category {
    private String name;
    private List<Attribute> attributes = new ArrayList<>();
    private List<Category> subcategories = new ArrayList<>();
}
