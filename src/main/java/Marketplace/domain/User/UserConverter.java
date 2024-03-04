package Marketplace.domain.User;

import  Marketplace.domain.User.User;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.domain.Location.LocationConverter;

public final class UserConverter {

    public UserConverter(){

    }

    public static User convert(UserEntity user){
        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .location(LocationConverter.convert(user.getLocation()))
                .build();
    }

    public static UserEntity convertToEntity(User user){
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .location(LocationConverter.convertToEntity(user.getLocation()))
                .build();
    }
}
