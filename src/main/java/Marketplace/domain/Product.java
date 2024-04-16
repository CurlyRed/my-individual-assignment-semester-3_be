package Marketplace.domain;

import lombok.*;
import java.util.List;

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
    private List<ProductAttribute> productAttributes;
}
