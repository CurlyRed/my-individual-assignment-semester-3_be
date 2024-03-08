package Marketplace.domain.Attribute;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class Attribute {
    private String name;
    private String type;
}
