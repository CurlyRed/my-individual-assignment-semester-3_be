package Marketplace.business.impl;

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
import Marketplace.enums.Gender;
import Marketplace.persistence.converter.UserConverter;
import Marketplace.persistence.entity.CityEntity;
import Marketplace.persistence.entity.RoleEntity;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.persistence.entity.UserInformationEntity;
import Marketplace.persistence.jpaRepository.CityRepository;
import Marketplace.persistence.jpaRepository.RoleRepository;
import Marketplace.persistence.jpaRepository.UserInformationRepository;
import Marketplace.persistence.jpaRepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private UserInformationRepository userInformationRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private PasswordValidator passwordValidator;
    @Mock
    private AmountValidator amountValidator;
    @Mock
    private AccessToken requestAccessToken;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUser_withValidRequest_shouldReturnCreateUserResponse() {
        // Given
        CreateUserRequest request = createValidUserRequest();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);

        UserEntity savedUserEntity = new UserEntity();
        savedUserEntity.setId(1L);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);
        when(roleRepository.findById(request.getRoleId())).thenReturn(Optional.of(roleEntity));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserEntity);
        when(emailValidator.isValid(request.getEmail())).thenReturn(true);
        when(passwordValidator.isValid(request.getPassword())).thenReturn(true);

        // When
        CreateUserResponse response = userService.createUser(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getUserId());

        // Verify
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(roleRepository, times(1)).findById(request.getRoleId());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userInformationRepository, times(1)).save(any(UserInformationEntity.class));
    }

    @Test
    void testCreateUser_withNullRequest_shouldReturnNull() {
        // When
        CreateUserResponse response = userService.createUser(null);

        // Then
        assertNull(response);

        // Verify
        verifyNoInteractions(userRepository, roleRepository, passwordEncoder, userInformationRepository);
    }

    @Test
    void testCreateUser_withExistingEmail_shouldThrowException() {
        // Given
        CreateUserRequest request = createValidUserRequest();

        when(emailValidator.isValid(request.getEmail())).thenReturn(true);
        when(passwordValidator.isValid(request.getPassword())).thenReturn(true);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(new UserEntity());

        // When & Then
        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(request));

        // Verify
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verifyNoInteractions(roleRepository, passwordEncoder, userInformationRepository);
    }

    @Test
    void testCreateUser_withNonExistentRole_shouldThrowException() {
        // Given
        CreateUserRequest request = createValidUserRequest();

        when(emailValidator.isValid(request.getEmail())).thenReturn(true);
        when(passwordValidator.isValid(request.getPassword())).thenReturn(true);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);
        when(roleRepository.findById(request.getRoleId())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
        assertEquals("Role not found", exception.getMessage());

        // Verify
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(roleRepository, times(1)).findById(request.getRoleId());
        verifyNoInteractions(passwordEncoder, userInformationRepository);
    }

    @Test
    void testCreateUser_withInvalidEmail_shouldThrowException() {
        // Given
        CreateUserRequest request = createValidUserRequest();

        when(emailValidator.isValid(request.getEmail())).thenReturn(false);

        // When & Then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.createUser(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        // Verify
        verify(emailValidator, times(1)).isValid(request.getEmail());
        verifyNoInteractions(userRepository, roleRepository, passwordEncoder, userInformationRepository);
    }

    @Test
    void testCreateUser_withInvalidPassword_shouldThrowException() {
        // Given
        CreateUserRequest request = createValidUserRequest();

        when(emailValidator.isValid(request.getEmail())).thenReturn(true);
        when(passwordValidator.isValid(request.getPassword())).thenReturn(false);

        // When & Then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.createUser(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        // Verify
        verify(emailValidator, times(1)).isValid(request.getEmail());
        verify(passwordValidator, times(1)).isValid(request.getPassword());
        verifyNoInteractions(userRepository, roleRepository, passwordEncoder, userInformationRepository);
    }

    @Test
    void testGetUser_withNonExistentUser_shouldReturnEmpty() {
        // Given
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUser(userId);

        // Then
        assertFalse(result.isPresent());

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(userConverter);
    }

    @Test
    void testUpdateUser_withValidRequest_shouldReturnTrue() {
        // Given
        UpdateUserRequest request = createValidUpdateUserRequest();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setUser(userEntity);

        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(1L);

        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(amountValidator.isValid(Double.valueOf(request.getAge()))).thenReturn(true);
        when(passwordValidator.isValid(request.getPassword())).thenReturn(true);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(userEntity));
        when(cityRepository.findById(request.getCity())).thenReturn(Optional.of(cityEntity));
        when(userInformationRepository.findByUserId(userEntity.getId())).thenReturn(Optional.of(userInformationEntity));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // When
        boolean result = userService.updateUser(request);

        // Then
        assertTrue(result);

        // Verify
        verify(userRepository, times(1)).save(userEntity);
        verify(userInformationRepository, times(1)).save(userInformationEntity);
    }

    @Test
    void testUpdateUser_withUnauthorizedAccess_shouldThrowException() {
        // Given
        UpdateUserRequest request = createValidUpdateUserRequest();
        when(requestAccessToken.getUserId()).thenReturn(2L); // Different user ID

        // When & Then
        assertThrows(UnauthorizedDataAccessException.class, () -> userService.updateUser(request));

        // Verify
        verifyNoInteractions(amountValidator, passwordValidator, userRepository, cityRepository, userInformationRepository);
    }

    @Test
    void testUpdateUser_withInvalidAge_shouldThrowException() {
        // Given
        UpdateUserRequest request = createValidUpdateUserRequest();
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(amountValidator.isValid(Double.valueOf(request.getAge()))).thenReturn(false);

        // When & Then
        assertThrows(InvalidRequestException.class, () -> userService.updateUser(request));

        // Verify
        verify(amountValidator, times(1)).isValid(Double.valueOf(request.getAge()));
        verifyNoInteractions(passwordValidator, userRepository, cityRepository, userInformationRepository);
    }

    @Test
    void testUpdateUser_withInvalidPassword_shouldThrowException() {
        // Given
        UpdateUserRequest request = createValidUpdateUserRequest();
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(amountValidator.isValid(Double.valueOf(request.getAge()))).thenReturn(true);
        when(passwordValidator.isValid(request.getPassword())).thenReturn(false);

        // When & Then
        assertThrows(InvalidRequestException.class, () -> userService.updateUser(request));

        // Verify
        verify(amountValidator, times(1)).isValid(Double.valueOf(request.getAge()));
        verify(passwordValidator, times(1)).isValid(request.getPassword());
        verifyNoInteractions(userRepository, cityRepository, userInformationRepository);
    }

    @Test
    void testUpdateUser_withNonExistentUser_shouldReturnFalse() {
        // Given
        UpdateUserRequest request = createValidUpdateUserRequest();
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(amountValidator.isValid(Double.valueOf(request.getAge()))).thenReturn(true);
        when(passwordValidator.isValid(request.getPassword())).thenReturn(true);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());

        // When
        boolean result = userService.updateUser(request);

        // Then
        assertFalse(result);

        // Verify
        verify(userRepository, times(1)).findById(request.getUserId());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(cityRepository, userInformationRepository);
    }

    @Test
    void testUpdateUser_withNoCity_shouldUpdateUserInformation() {
        // Given
        UpdateUserRequest request = createValidUpdateUserRequest();
        request.setCity(null); // No city

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setUser(userEntity);

        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(amountValidator.isValid(Double.valueOf(request.getAge()))).thenReturn(true);
        when(passwordValidator.isValid(request.getPassword())).thenReturn(true);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(userEntity));
        when(userInformationRepository.findByUserId(userEntity.getId())).thenReturn(Optional.of(userInformationEntity));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // When
        boolean result = userService.updateUser(request);

        // Then
        assertTrue(result);

        // Verify
        verify(userRepository, times(1)).save(userEntity);
        verify(userInformationRepository, times(1)).save(userInformationEntity);
    }

    @Test
    void testDeleteById_withExistingUser_shouldReturnTrue() {
        // Given
        long userId = 1L;

        // When
        boolean result = userService.deleteById(userId);

        // Then
        assertTrue(result);

        // Verify
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteById_withNonExistentUser_shouldReturnFalse() {
        // Given
        long userId = 1L;

        doThrow(new EmptyResultDataAccessException(1)).when(userRepository).deleteById(userId);

        // When
        boolean result = userService.deleteById(userId);

        // Then
        assertFalse(result);

        // Verify
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetUserByProductId_withExistingUser_shouldReturnUser() {
        // Given
        long productId = 1L;
        UserEntity userEntity = new UserEntity();
        User user = new User();

        when(userRepository.findByProductId(productId)).thenReturn(Optional.of(userEntity));
        when(userConverter.toDomain(userEntity)).thenReturn(user);

        // When
        Optional<User> result = userService.getUserByProductId(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        // Verify
        verify(userRepository, times(1)).findByProductId(productId);
        verify(userConverter, times(1)).toDomain(userEntity);
    }

    @Test
    void testGetUserByProductId_withNonExistentUser_shouldReturnEmpty() {
        // Given
        long productId = 1L;

        when(userRepository.findByProductId(productId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUserByProductId(productId);

        // Then
        assertFalse(result.isPresent());

        // Verify
        verify(userRepository, times(1)).findByProductId(productId);
        verifyNoInteractions(userConverter);
    }

    private CreateUserRequest createValidUserRequest() {
        return CreateUserRequest.builder()
                .email("serhii@gmail.com")
                .password("Passwor$1")
                .roleId(1L)
                .build();
    }

    private UpdateUserRequest createValidUpdateUserRequest() {
        return UpdateUserRequest.builder()
                .userId(1L)
                .password("ValidPassword123")
                .age(25)
                .firstName("John")
                .lastName("Doe")
                .city(1L)
                .gender(Gender.MALE)
                .build();
    }
}
