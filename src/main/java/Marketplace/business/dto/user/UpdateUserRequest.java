package Marketplace.business.dto.user;

import Marketplace.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private Long userId;
    private String password;
    private String firstName;
    private String lastName;
    private Long city;
    private Integer age;
    private Gender gender;
}

