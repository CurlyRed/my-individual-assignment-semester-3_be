package Marketplace.business.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCategoryResponse {
    private Long categoryId;
}
