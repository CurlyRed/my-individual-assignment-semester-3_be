package Marketplace.domain;

import Marketplace.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {
    private Long id;
    private String firstName;
    private String lastName;
    private City city;
    private Integer age;
    private Gender gender;
}
