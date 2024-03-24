package Marketplace.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Marketplace.domain.Location.Location;
import Marketplace.domain.Location.LocationConverter;
import Marketplace.domain.User.User;
import Marketplace.domain.User.UserConverter;
import Marketplace.persistence.entity.LocationEntity;
import Marketplace.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserConverterTest {

    @Mock
    private LocationConverter locationConverter;

    private UserConverter userConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userConverter = new UserConverter(locationConverter);
    }

    @Test
    void convert_ValidUserEntity_ReturnsUser() {
        // Given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .location(LocationEntity.builder()
                        .id(1L)
                        .country("Country")
                        .city("City")
                        .address("Address")
                        .build())
                .build();
        when(locationConverter.convert(any())).thenReturn(new Location());

        // When
        User user = userConverter.convert(userEntity);

        // Then
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertNotNull(user.getLocation());
    }

    @Test
    void convertToEntity_ValidUser_ReturnsUserEntity() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .location(new Location())
                .build();
        when(locationConverter.convertToEntity(any())).thenReturn(LocationEntity.builder()
                .id(1L)
                .country("Country")
                .city("City")
                .address("Address")
                .build());

        // When
        UserEntity userEntity = userConverter.convertToEntity(user);

        // Then
        assertNotNull(userEntity);
        assertEquals(1L, userEntity.getId());
        assertEquals("testuser", userEntity.getUsername());
        assertEquals("password", userEntity.getPassword());
        assertEquals("test@example.com", userEntity.getEmail());
        assertEquals("John", userEntity.getFirstName());
        assertEquals("Doe", userEntity.getLastName());
        assertNotNull(userEntity.getLocation());
    }
}

