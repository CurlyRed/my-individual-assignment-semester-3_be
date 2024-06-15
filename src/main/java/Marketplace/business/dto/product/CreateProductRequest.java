package Marketplace.business.dto.product;

import Marketplace.domain.ProductAttribute;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank
    private String productName;
    @NotBlank
    private String productDescription;
    @NotNull
    private Double productPrice;
    @NotNull
    private Date dateOfPost;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long cityId;
    @NotBlank
    private String contact_person;
    @NotBlank
    private String email;
    @NotBlank
    private String phone_number;
    @NotNull
    private List<ProductAttribute> attributes;
}
