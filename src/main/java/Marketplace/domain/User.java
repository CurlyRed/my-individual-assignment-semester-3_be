package Marketplace.domain;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String email;
    private String password;
    private Date date_of_registry;
    private Role role;
    private List<Product> products;
    private UserBalance balance;
    private UserInformation userInformation;;
}