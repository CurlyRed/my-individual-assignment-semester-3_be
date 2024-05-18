package Marketplace.business;

import Marketplace.business.dto.user.CreateUserRequest;
import Marketplace.business.dto.user.CreateUserResponse;
import Marketplace.business.dto.user.UpdateUserRequest;
import Marketplace.business.exception.EmailAlreadyExistsException;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.business.impl.UserServiceImpl;
import Marketplace.config.security.token.AccessToken;
import Marketplace.domain.User;
import Marketplace.persistence.converter.UserConverter;
import Marketplace.persistence.entity.RoleEntity;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.persistence.jpaRepository.RoleRepository;
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
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccessToken requestAccessToken;
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
    }

    @Test
    void testCreateUser_withNullRequest_shouldReturnNull() {
        // When
        CreateUserResponse response = userService.createUser(null);

        // Then
        assertNull(response);

        // Verify
        verifyNoInteractions(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void testCreateUser_withExistingEmail_shouldThrowException() {
        // Given
        CreateUserRequest request = createValidUserRequest();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(new UserEntity());

        // When & Then
        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(request));

        // Verify
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verifyNoInteractions(roleRepository, passwordEncoder);
    }

    @Test
    void testCreateUser_withNonExistentRole_shouldThrowException() {
        // Given
        CreateUserRequest request = createValidUserRequest();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);
        when(roleRepository.findById(request.getRoleId())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
        assertEquals("Role not found", exception.getMessage());

        // Verify
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(roleRepository, times(1)).findById(request.getRoleId());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void testGetUser_withAdminRole_shouldReturnUser() {
        // Given
        long userId = 1L;
        UserEntity userEntity = new UserEntity();
        User user = new User();

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userConverter.toDomain(userEntity)).thenReturn(user);

        // When
        Optional<User> result = userService.getUser(userId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        // Verify
        verify(requestAccessToken, times(1)).hasRole("ADMIN");
        verify(userRepository, times(1)).findById(userId);
        verify(userConverter, times(1)).toDomain(userEntity);
    }

    @Test
    void testGetUser_withSameUserId_shouldReturnUser() {
        // Given
        long userId = 1L;
        UserEntity userEntity = new UserEntity();
        User user = new User();

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(false);
        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userConverter.toDomain(userEntity)).thenReturn(user);

        // When
        Optional<User> result = userService.getUser(userId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        // Verify
        verify(requestAccessToken, times(1)).hasRole("ADMIN");
        verify(requestAccessToken, times(1)).getUserId();
        verify(userRepository, times(1)).findById(userId);
        verify(userConverter, times(1)).toDomain(userEntity);
    }

    @Test
    void testGetUser_withDifferentUserId_shouldThrowException() {
        // Given
        long userId = 1L;
        long differentUserId = 2L;

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(false);
        when(requestAccessToken.getUserId()).thenReturn(differentUserId);

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> userService.getUser(userId));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(requestAccessToken, times(1)).hasRole("ADMIN");
        verify(requestAccessToken, times(1)).getUserId();
        verifyNoInteractions(userRepository, userConverter);
    }

    @Test
    void testGetUser_withNonExistentUser_shouldReturnEmpty() {
        // Given
        long userId = 1L;

        when(requestAccessToken.hasRole("ADMIN")).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUser(userId);

        // Then
        assertFalse(result.isPresent());

        // Verify
        verify(requestAccessToken, times(1)).hasRole("ADMIN");
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(userConverter);
    }

    @Test
    void testUpdateUser_withExistingUser_shouldReturnTrue() {
        // Given
        UpdateUserRequest request = createValidUpdateUserRequest();
        UserEntity userEntity = new UserEntity();

        when(userRepository.findById(request.getId())).thenReturn(Optional.of(userEntity));

        // When
        boolean result = userService.updateUser(request);

        // Then
        assertTrue(result);
        assertEquals(request.getEmail(), userEntity.getEmail());
        assertEquals(request.getPassword(), userEntity.getPassword());
        assertEquals(request.getFirstName(), userEntity.getFirstName());
        assertEquals(request.getLastName(), userEntity.getLastName());

        // Verify
        verify(userRepository, times(1)).findById(request.getId());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testUpdateUser_withNonExistentUser_shouldReturnFalse() {
        // Given
        UpdateUserRequest request = createValidUpdateUserRequest();

        when(userRepository.findById(request.getId())).thenReturn(Optional.empty());

        // When
        boolean result = userService.updateUser(request);

        // Then
        assertFalse(result);

        // Verify
        verify(userRepository, times(1)).findById(request.getId());
        verify(userRepository, times(0)).save(any(UserEntity.class));
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
                .email("test@example.com")
                .password("password")
                .roleId(1L)
                .build();
    }

    private UpdateUserRequest createValidUpdateUserRequest() {
        return UpdateUserRequest.builder()
                .id(1L)
                .email("updated@example.com")
                .password("updatedPassword")
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .build();
    }
}
