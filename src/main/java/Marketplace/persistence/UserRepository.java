package Marketplace.persistence;

import Marketplace.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {

    UserEntity saveUser(UserEntity user);

    boolean deleteById(long userId);

    Optional<UserEntity> findById(long userId);

}
