package Marketplace.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import Marketplace.business.UserService;
import Marketplace.domain.Location.Location;
import Marketplace.domain.User.CreateUserRequest;
import Marketplace.domain.User.CreateUserResponse;
import Marketplace.domain.User.UpdateUserRequest;
import Marketplace.domain.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AccountControllerTest {

    @Mock
    private UserService userService;

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountController = new AccountController(userService);
    }

    @Test
    void createUser_ValidRequest_ReturnsCreatedResponse() {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .location(new Location())
                .build();
        CreateUserResponse response = CreateUserResponse.builder()
                .userId(1L)
                .build();
        when(userService.createUser(request)).thenReturn(response);

        // When
        ResponseEntity<CreateUserResponse> responseEntity = accountController.createUser(request);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void getUser_ExistingUserId_ReturnsUser() {
        // Given
        long userId = 1L;
        User user = User.builder()
                .id(userId)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .location(new Location())
                .build();
        when(userService.getUser(userId)).thenReturn(Optional.of(user));

        // When
        ResponseEntity<User> responseEntity = accountController.getUser(userId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    void getUser_NonExistingUserId_ReturnsNotFound() {
        // Given
        long userId = 1L;
        when(userService.getUser(userId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<User> responseEntity = accountController.getUser(userId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void updateUser_ValidRequest_ReturnsNoContent() {
        // Given
        long userId = 1L;
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(userId)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .location(new Location())
                .build();

        // When
        ResponseEntity<Void> responseEntity = accountController.updateUser(userId, request);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void deleteUser_ExistingUserId_ReturnsNoContent() {
        // Given
        int userId = 1;

        // When
        ResponseEntity<Void> responseEntity = accountController.deleteUser(userId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}

