package Marketplace.business;

import Marketplace.business.dto.user.CreateUserRequest;
import Marketplace.business.dto.user.CreateUserResponse;
import Marketplace.business.dto.user.UpdateUserRequest;
import Marketplace.domain.User;

import java.util.Optional;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);

    Optional<User> getUser(long userId);

    boolean updateUser(UpdateUserRequest request);

    boolean deleteById(long userId);

}
