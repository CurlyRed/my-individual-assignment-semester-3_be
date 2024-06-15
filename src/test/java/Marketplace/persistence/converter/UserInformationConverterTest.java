package Marketplace.persistence.converter;

import Marketplace.domain.*;
import Marketplace.persistence.entity.*;
import Marketplace.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserInformationConverterTest {

    private UserInformationConverter converter;

    @Mock
    private CityConverter cityConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new UserInformationConverter(cityConverter);
    }

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        CityEntity cityEntity = CityEntity.builder()
                .id(1L)
                .name("Test City")
                .build();

        UserInformationEntity userInformationEntity = UserInformationEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .city(cityEntity)
                .age(30)
                .gender(Gender.MALE)
                .build();

        when(cityConverter.toDomain(cityEntity)).thenReturn(City.builder().id(1L).name("Test City").build());

        // When
        UserInformation userInformation = converter.toDomain(userInformationEntity);

        // Then
        assertNotNull(userInformation);
        assertEquals(userInformationEntity.getId(), userInformation.getId());
        assertEquals(userInformationEntity.getFirstName(), userInformation.getFirstName());
        assertEquals(userInformationEntity.getLastName(), userInformation.getLastName());
        assertNotNull(userInformation.getCity());
        assertEquals(cityEntity.getId(), userInformation.getCity().getId());
        assertEquals(cityEntity.getName(), userInformation.getCity().getName());
        assertEquals(userInformationEntity.getAge(), userInformation.getAge());
        assertEquals(userInformationEntity.getGender(), userInformation.getGender());

    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        UserInformationEntity userInformationEntity = null;

        // When
        UserInformation userInformation = converter.toDomain(userInformationEntity);

        // Then
        assertNull(userInformation);
    }

    @Test
    void testToDomain_givenEntityWithNullFields_shouldHandleGracefully() {
        // Given
        UserInformationEntity userInformationEntity = UserInformationEntity.builder()
                .id(1L)
                .firstName(null)
                .lastName(null)
                .city(null)
                .age(null)
                .gender(null)
                .build();

        // When
        UserInformation userInformation = converter.toDomain(userInformationEntity);

        // Then
        assertNotNull(userInformation);
        assertEquals(userInformationEntity.getId(), userInformation.getId());
        assertNull(userInformation.getFirstName());
        assertNull(userInformation.getLastName());
        assertNull(userInformation.getCity());
        assertNull(userInformation.getAge());
        assertNull(userInformation.getGender());
    }
}
