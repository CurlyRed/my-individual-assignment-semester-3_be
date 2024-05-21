package Marketplace.domain;

import Marketplace.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Long id;
    private TransactionType type;
    private Double amount;
    private String description;
    private Date created_at;
    private User user;
}
