package Marketplace.business.dto.walletOperations;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchasePromotionRequest {
    @NotNull
    private Double amount;
    @NotNull
    private Long productId;
}
