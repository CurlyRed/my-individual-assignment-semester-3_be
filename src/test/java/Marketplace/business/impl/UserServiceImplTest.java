package Marketplace.business.impl;

import Marketplace.domain.User.*;
import Marketplace.persistence.UserRepository;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.domain.Location.Location;
import Marketplace.domain.Location.LocationConverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ValidRequest_ReturnsCreateUserResponse() {
        // Given
        CreateUserRequest request = createUserRequest();
        UserEntity createdUserEntity = createUserEntity();
        when(userRepository.saveUser(any(UserEntity.class))).thenReturn(createdUserEntity);

        // When
        CreateUserResponse response = userService.createUser(request);

        // Then
        assertNotNull(response);
        assertEquals(createdUserEntity.getId(), response.getUserId());
    }

    @Test
    void getUser_ExistingUserId_ReturnsUser() {
        // Given
        long userId = 1L;
        UserEntity userEntity = createUserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // When
        Optional<User> userOptional = userService.getUser(userId);

        // Then
        assertTrue(userOptional.isPresent());
        assertEquals(userEntity.getUsername(), userOptional.get().getUsername());
        assertEquals(userEntity.getEmail(), userOptional.get().getEmail());
        assertEquals(userEntity.getPassword(), userOptional.get().getPassword());
        assertEquals(userEntity.getFirstName(), userOptional.get().getFirstName());
        assertEquals(userEntity.getLastName(), userOptional.get().getLastName());
        assertEquals(LocationConverter.convert(userEntity.getLocation()), userOptional.get().getLocation());
    }

    @Test
    void getUser_NonExistingUserId_ReturnsEmptyOptional() {
        // Given
        long nonExistingUserId = 100L;
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // When
        Optional<User> userOptional = userService.getUser(nonExistingUserId);

        // Then
        assertTrue(userOptional.isEmpty());
    }

    @Test
    void updateUser_ExistingUser_ReturnsTrue() {
        // Given
        UpdateUserRequest request = updateUserRequest();
        UserEntity existingUserEntity = createUserEntity();
        when(userRepository.findById(request.getId())).thenReturn(Optional.of(existingUserEntity));
        when(userRepository.saveUser(any(UserEntity.class))).thenReturn(existingUserEntity);

        // When
        boolean result = userService.updateUser(request);

        // Then
        assertTrue(result);
    }

    @Test
    void updateUser_NonExistingUser_ReturnsFalse() {
        // Given
        UpdateUserRequest request = updateUserRequest();
        long nonExistingUserId = 100L;
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // When
        boolean result = userService.updateUser(request);

        // Then
        assertFalse(result);
    }

    @Test
    void deleteById_ExistingUserId_ReturnsTrue() {
        // Given
        long userId = 1L;
        when(userRepository.deleteById(userId)).thenReturn(true);

        // When
        boolean result = userService.deleteById(userId);

        // Then
        assertTrue(result);
    }

    @Test
    void deleteById_NonExistingUserId_ReturnsFalse() {
        // Given
        long nonExistingUserId = 100L;
        when(userRepository.deleteById(nonExistingUserId)).thenReturn(false);

        // When
        boolean result = userService.deleteById(nonExistingUserId);

        // Then
        assertFalse(result);
    }

    private CreateUserRequest createUserRequest() {
        return CreateUserRequest.builder()
                .username("testUser")
                .password("password")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .location(createLocation())
                .build();
    }

    private UpdateUserRequest updateUserRequest() {
        return UpdateUserRequest.builder()
                .id(1L)
                .username("updatedUser")
                .password("newPassword")
                .email("updated@example.com")
                .firstName("Updated")
                .lastName("User")
                .location(createLocation())
                .build();
    }

    private UserEntity createUserEntity() {
        return UserEntity.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .location(LocationConverter.convertToEntity(createLocation()))
                .build();
    }

    private Location createLocation() {
        return Location.builder()
                .id(1L)
                .country("Country")
                .city("City")
                .address("Address")
                .build();
    }
}
