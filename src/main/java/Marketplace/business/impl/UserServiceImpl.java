package Marketplace.business.impl;

import Marketplace.business.UserService;
import Marketplace.business.dto.user.CreateUserRequest;
import Marketplace.business.dto.user.CreateUserResponse;
import Marketplace.business.dto.user.UpdateUserRequest;
import Marketplace.domain.User;
import Marketplace.persistence.converter.UserConverter;
import Marketplace.persistence.entity.RoleEntity;
import Marketplace.persistence.jpaRepository.RoleRepository;
import Marketplace.persistence.jpaRepository.UserRepository;
import Marketplace.persistence.entity.UserEntity;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
/*
import org.springframework.security.crypto.password.PasswordEncoder;
*/
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
/*    private final PasswordEncoder passwordEncoder;*/

    @Override
    public CreateUserResponse createUser(CreateUserRequest request){
        if (request == null) {
            return null;
        }

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(request.getRoleId());
        RoleEntity roleEntity = optionalRoleEntity.orElseThrow(() ->new IllegalArgumentException("Role not found"));

/*
        String encodedPassword = passwordEncoder.encode(request.getPassword());
*/

        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(roleEntity)
                .build();

        userEntity = userRepository.save(userEntity);

        return CreateUserResponse.builder()
                .userId(userEntity.getId())
                .build();
    }

    @Override
    public Optional<User> getUser(long userId){
        return userRepository.findById(userId)
                .map(userConverter::toDomain);
    }

    @Override
    public boolean updateUser(UpdateUserRequest request){
        Optional<UserEntity> userOptional = userRepository.findById(request.getId());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());

            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteById(long userId){
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
