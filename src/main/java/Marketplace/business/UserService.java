package Marketplace.business;

import Marketplace.domain.User.CreateUserRequest;
import Marketplace.domain.User.CreateUserResponse;
import Marketplace.domain.User.UpdateUserRequest;
import Marketplace.domain.User.User;

import java.util.Optional;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);

    Optional<User> getUser(long userId);

    boolean updateUser(UpdateUserRequest request);

    boolean deleteById(long userId);

}
