package Marketplace.business.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetLocationForProductResponse {
    private String districtName;
    private String cityName;
}
