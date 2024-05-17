package Marketplace.business;

import Marketplace.business.dto.user.CreateUserRequest;
import Marketplace.business.dto.user.CreateUserResponse;
import Marketplace.business.dto.user.UpdateUserRequest;
import Marketplace.business.impl.UserServiceImpl;
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

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserConverter userConverter;
    @InjectMocks
    private UserServiceImpl userService;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createUser_CreatesUser(){
        // Given
        long roleId = 1L;
        CreateUserRequest request = CreateUserRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        RoleEntity roleEntity = RoleEntity.builder().id(roleId).name("ROLE_USER").build();

        UserEntity savedUserEntity = UserEntity.builder()
                .id(1L)
                .email(request.getEmail())
                .password(request.getPassword())
                .role(roleEntity)
                .build();

        // Mock
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserEntity);

        // When
        CreateUserResponse response = userService.createUser(request);

        // Verify
        verify(roleRepository, times(1)).findById(roleId);
        verify(userRepository, times(1)).save(any(UserEntity.class));

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
    }

    @Test
    void createUser_ReturnsNull_WhenRequestIsNull() {
        // Given
        CreateUserRequest request = null;

        // When
        CreateUserResponse response = userService.createUser(request);

        // Then
        assertNull(response);
    }

    @Test
    void getUser_ReturnEmptyOptional_WhenUserNotFound(){
        // Given
        long userId = 1L;

        // Mock
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUser(userId);

        // Verify
        verify(userRepository, times(1)).findById(userId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getUser_ReturnsUser_WhenUserFound(){
        // Given
        long userId = 1L;
        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .build();

        User user = new User();
        user.setId(userId);

        // Mock
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userConverter.toDomain(userEntity)).thenReturn(user);

        // When
        Optional<User> result = userService.getUser(userId);

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verify(userConverter, times(1)).toDomain(userEntity);

        //  Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void updateUser_UpdatesUser_ReturnsFalse_WhenUserNotFound(){
        // Given
        long userId = 1L;
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(userId)
                .email("test")
                .password("123")
                .firstName("Serhii")
                .lastName("Sokyrko")
                .build();

        // Mock
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        boolean isUpdated = userService.updateUser(request);

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(UserEntity.class));

        // Then
        assertFalse(isUpdated);
    }

    @Test
    void updateUser_ReturnsTrue_WhenUserFound(){
        // Given
        long userId = 1L;
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(userId)
                .email("test")
                .password("123")
                .firstName("Serhii")
                .lastName("Sokyrko")
                .build();

        UserEntity existingUser = UserEntity.builder()
                .id(userId)
                .email("beforeTest")
                .password("999")
                .firstName("Michael")
                .lastName("Doe")
                .build();

        // Mock
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // When
        boolean isUpdated = userService.updateUser(request);

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);

        // Then
        assertTrue(isUpdated);
    }

    @Test
    void deleteById_DeletesUser_ReturnsFalse_WhenUserNotFound(){
        // Given
        long userId = 1L;

        // Mock
        doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(userId);

        // When
        boolean isDeleted = userService.deleteById(userId);

        // Verify
        verify(userRepository, times(1)).deleteById(userId);

        // Then
        assertFalse(isDeleted);
    }

    @Test
    void deleteById_ReturnsTrue_WhenUserFound(){
        // Given
        long userId = 1L;

        // Mock
        doNothing().when(userRepository).deleteById(userId);

        // When
        boolean isDeleted = userService.deleteById(userId);

        // Verify
        verify(userRepository, times(1)).deleteById(userId);

        // Then
        assertTrue(isDeleted);
    }
}
