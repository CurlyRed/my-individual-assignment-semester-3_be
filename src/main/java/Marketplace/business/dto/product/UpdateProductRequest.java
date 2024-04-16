package Marketplace.business.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    @NotNull
    private Long id;
    @NotBlank
    private String productName;
    @NotBlank
    private String productDescription;
    @NotNull
    private Map<String, String> categoryAttributes;

}

