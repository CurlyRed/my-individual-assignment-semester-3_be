package Marketplace.domain.User;

import Marketplace.persistence.entity.UserEntity;
import Marketplace.domain.Location.LocationConverter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserConverter {

    private LocationConverter locationConverter;

    public  User convert(UserEntity user){
        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .location(locationConverter.convert(user.getLocation()))
                .build();
    }

    public UserEntity convertToEntity(User user){
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .location(locationConverter.convertToEntity(user.getLocation()))
                .build();
    }
}
