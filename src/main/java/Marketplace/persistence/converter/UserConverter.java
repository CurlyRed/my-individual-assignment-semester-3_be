package Marketplace.persistence.converter;

import Marketplace.domain.User;
import Marketplace.persistence.entity.UserEntity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;


@AllArgsConstructor
@Component
public class UserConverter {
    public final RoleConverter roleConverter;
    public final ProductConverter productConverter;

    public User toDomain(UserEntity userEntity){
        if(userEntity == null){
            return null;
        }

        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .role(roleConverter.toDomain(userEntity.getRole()))
                .products(userEntity.getProducts().stream()
                        .map(productConverter::toDomain)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .build();
    }
}
