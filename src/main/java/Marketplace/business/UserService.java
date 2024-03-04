package Marketplace.business;

import Marketplace.domain.User.CreateUserRequest;
import Marketplace.domain.User.CreateUserResponse;
import Marketplace.domain.User.UpdateUserRequest;
import Marketplace.domain.User.User;

import java.util.Optional;

public interface UserService {
    public CreateUserResponse createUser(CreateUserRequest request);

    public Optional<User> getUser(long userId);

    public boolean updateUser(UpdateUserRequest request);

    public boolean deleteById(long userId);

}
