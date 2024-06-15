package Marketplace.business.dto.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Min(value = 0, message = "Product price must be greater than zero")
    private Double productPrice;
    @NotNull
    private Long cityId;
    @NotBlank
    private String contact_person;
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone_number;
}

