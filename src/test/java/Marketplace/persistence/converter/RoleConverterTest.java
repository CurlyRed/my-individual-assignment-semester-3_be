package Marketplace.persistence.converter;

import Marketplace.domain.Role;
import Marketplace.persistence.entity.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleConverterTest {

    private RoleConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new RoleConverter();
    }

    @Test
    public void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Test Role");

        // When
        Role role = converter.toDomain(roleEntity);

        // Then
        assertNotNull(role);
        assertEquals(roleEntity.getId(), role.getId());
        assertEquals(roleEntity.getName(), role.getName());
    }

    @Test
    public void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        RoleEntity roleEntity = null;

        // When
        Role role = converter.toDomain(roleEntity);

        // Then
        assertNull(role);
    }
}

