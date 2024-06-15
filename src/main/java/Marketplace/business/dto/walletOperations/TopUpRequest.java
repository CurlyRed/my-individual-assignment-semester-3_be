package Marketplace.business.dto.walletOperations;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopUpRequest {
    @NotNull
    @Min(value = 1, message = "Amount should be greater than zero.")
    private Double amount;
}
