package Marketplace.persistence.converter;

import Marketplace.domain.User;
import Marketplace.persistence.entity.UserEntity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;


@AllArgsConstructor
@Component
public class UserConverter {
    public final RoleConverter roleConverter;
    public final ProductConverter productConverter;
    public final UserBalanceConverter userBalanceConverter;
    public final UserInformationConverter userInformationConverter;

    public User toDomain(UserEntity userEntity){
        if(userEntity == null){
            return null;
        }

        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .date_of_registry(userEntity.getDate_of_registry())
                .userInformation(userInformationConverter.toDomain(userEntity.getUserInformation()))
                .role(roleConverter.toDomain(userEntity.getRole()))
                .products(userEntity.getProducts() == null ? Collections.emptyList() :
                        userEntity.getProducts().stream()
                                .map(productConverter::toDomain)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()))
                .balance(userBalanceConverter.toDomain(userEntity.getUser_balance()))
                .build();
    }
}
