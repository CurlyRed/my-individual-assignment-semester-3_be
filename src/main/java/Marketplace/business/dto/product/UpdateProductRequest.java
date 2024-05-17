package Marketplace.business.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    @NotBlank
    private String productName;
    @NotBlank
    private String productDescription;
    @NotNull
    private Double productPrice;
    @NotNull
    private Long cityId;
    @NotBlank
    private String contact_person;
    @NotBlank
    private String email;
    @NotBlank
    private String phone_number;
}

