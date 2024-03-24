package Marketplace.business.impl;

import Marketplace.business.UserService;
import Marketplace.domain.User.*;
import Marketplace.persistence.UserRepository;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.domain.Location.LocationConverter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final LocationConverter locationConverter;

    @Override
    public CreateUserResponse createUser(CreateUserRequest request){
        if (request == null) {
            return null;
        }

        UserEntity createdUser = UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .location(locationConverter.convertToEntity(request.getLocation()))
                .build();

        createdUser = userRepository.saveUser(createdUser);

        return CreateUserResponse.builder()
                .userId(createdUser.getId())
                .build();
    }

    @Override
    public Optional<User> getUser(long userId){

        return userRepository.findById(userId).map(userConverter::convert);
    }

    @Override
    public boolean updateUser(UpdateUserRequest request){
        Optional<UserEntity> userOptional = this.userRepository.findById(request.getId());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setLocation(locationConverter.convertToEntity(request.getLocation()));

            userRepository.saveUser(user);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean deleteById(long userId){

        return this.userRepository.deleteById(userId);
    }
}
