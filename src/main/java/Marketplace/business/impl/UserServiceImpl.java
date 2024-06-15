package Marketplace.business.impl;

import Marketplace.business.UserService;
import Marketplace.business.dto.user.CreateUserRequest;
import Marketplace.business.dto.user.CreateUserResponse;
import Marketplace.business.dto.user.UpdateUserRequest;
import Marketplace.business.exception.EmailAlreadyExistsException;
import Marketplace.business.exception.InvalidRequestException;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.business.validators.AmountValidator;
import Marketplace.business.validators.EmailValidator;
import Marketplace.business.validators.PasswordValidator;
import Marketplace.config.security.token.AccessToken;
import Marketplace.domain.User;
import Marketplace.persistence.converter.UserConverter;
import Marketplace.persistence.entity.CityEntity;
import Marketplace.persistence.entity.RoleEntity;
import Marketplace.persistence.entity.UserInformationEntity;
import Marketplace.persistence.jpaRepository.CityRepository;
import Marketplace.persistence.jpaRepository.RoleRepository;
import Marketplace.persistence.jpaRepository.UserInformationRepository;
import Marketplace.persistence.jpaRepository.UserRepository;
import Marketplace.persistence.entity.UserEntity;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserInformationRepository userInformationRepository;
    private final CityRepository cityRepository;
    private final UserConverter userConverter;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final AmountValidator amountValidator;
    private final PasswordEncoder passwordEncoder;
    private final AccessToken requestAccessToken;

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (request == null) {
            return null;
        }

        if (!emailValidator.isValid(request.getEmail())) {
            throw new InvalidRequestException("Invalid email.");
        }

        if (!passwordValidator.isValid(request.getPassword())) {
            throw new InvalidRequestException("Invalid password.");
        }

        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Email already exists. Please try again.");
        }

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(request.getRoleId());
        RoleEntity roleEntity = optionalRoleEntity.orElseThrow(() -> new IllegalArgumentException("Role not found"));

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .date_of_registry(new Date())
                .role(roleEntity)
                .build();

        userEntity = userRepository.save(userEntity);

        UserInformationEntity userInformationEntity = UserInformationEntity.builder()
                .user(userEntity)
                .firstName(null)
                .lastName(null)
                .city(null)
                .age(null)
                .gender(null)
                .build();

        userInformationRepository.save(userInformationEntity);

        return CreateUserResponse.builder()
                .userId(userEntity.getId())
                .build();
    }

    @Override
    @Transactional
    public Optional<User> getUser(long userId) {
        return userRepository.findById(userId)
                .map(userConverter::toDomain);
    }

    @Override
    @Transactional
    public boolean updateUser(UpdateUserRequest request) {
        if (!Objects.equals(requestAccessToken.getUserId(), request.getUserId())) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        if(!amountValidator.isValid(Double.valueOf(request.getAge()))){
            throw new InvalidRequestException("Invalid age, please try again.");
        }

        if(!passwordValidator.isValid(request.getPassword())) {
            throw new InvalidRequestException("Invalid password.");
        }

        Optional<UserEntity> userOptional = userRepository.findById(request.getUserId());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            userRepository.save(user);

            CityEntity city = null;
            if (request.getCity() != null) {
                Optional<CityEntity> cityOptional = cityRepository.findById(request.getCity());
                city = cityOptional.orElse(null);
            }

            Optional<UserInformationEntity> userInformationOptional = userInformationRepository.findByUserId(user.getId());

            if (userInformationOptional.isPresent()) {
                UserInformationEntity userInformation = userInformationOptional.get();

                boolean isUserInformationUpdated = false;
                if (request.getFirstName() != null) {
                    userInformation.setFirstName(request.getFirstName());
                    isUserInformationUpdated = true;
                }
                if (request.getLastName() != null) {
                    userInformation.setLastName(request.getLastName());
                    isUserInformationUpdated = true;
                }
                if (city != null) {
                    userInformation.setCity(city);
                    isUserInformationUpdated = true;
                }
                if (request.getAge() != null) {
                    userInformation.setAge(request.getAge());
                    isUserInformationUpdated = true;
                }
                if (request.getGender() != null) {
                    userInformation.setGender(request.getGender());
                    isUserInformationUpdated = true;
                }

                if (isUserInformationUpdated) {
                    userInformationRepository.save(userInformation);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteById(long userId){
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Optional<User> getUserByProductId(long productId) {
        return userRepository.findByProductId(productId)
                .map(userConverter::toDomain);
    }
}
