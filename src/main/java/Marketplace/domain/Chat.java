package Marketplace.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private long id;
    private User buyer;
    private User seller;
    private Product product;
    private Date created_at;
    private Boolean deleted;
}
