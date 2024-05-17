package Marketplace.domain;

import lombok.*;

import java.util.Date;
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
    private Double price;
    private Date date_of_post;
    private Category category;
    private ContactInformation contact_information;
    private List<ProductAttribute> productAttributes;
}
