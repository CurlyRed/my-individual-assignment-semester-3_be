package Marketplace.business.dto.category;

import Marketplace.domain.Attribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {
    @NotNull
    private long id;
    @NotNull
    private List<Attribute> attributes;
}
