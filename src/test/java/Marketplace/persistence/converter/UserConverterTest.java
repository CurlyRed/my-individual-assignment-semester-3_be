package Marketplace.persistence.converter;

import Marketplace.domain.Role;
import Marketplace.domain.User;
import Marketplace.persistence.entity.RoleEntity;
import Marketplace.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class UserConverterTest {

    private UserConverter converter;

    @Mock
    private RoleConverter roleConverter;

    @Mock
    private ProductConverter productConverter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new UserConverter(roleConverter, productConverter);
    }

    @Test
    public void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        RoleEntity roleEntity = RoleEntity.builder()
                .id(1L)
                .name("Test Role")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(roleEntity)
                .products(new ArrayList<>())
                .build();

        when(roleConverter.toDomain(roleEntity)).thenReturn(
                Role.builder().id(1L).name("Test Role").build());

        // When
        User user = converter.toDomain(userEntity);

        // Then
        assertNotNull(user);
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getPassword(), user.getPassword());
        assertEquals(userEntity.getFirstName(), user.getFirstName());
        assertEquals(userEntity.getLastName(), user.getLastName());
        assertNotNull(user.getRole());
        assertEquals(roleEntity.getId(), user.getRole().getId());
        assertEquals(roleEntity.getName(), user.getRole().getName());
    }

    @Test
    public void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        UserEntity userEntity = null;

        // When
        User user = converter.toDomain(userEntity);

        // Then
        assertNull(user);
    }
}

