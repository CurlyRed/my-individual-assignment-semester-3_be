package Marketplace.persistence.converter;

import Marketplace.domain.*;
import Marketplace.persistence.entity.*;
import Marketplace.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserConverterTest {

    private UserConverter converter;

    @Mock
    private RoleConverter roleConverter;

    @Mock
    private ProductConverter productConverter;

    @Mock
    private UserBalanceConverter balanceConverter;

    @Mock
    private UserInformationConverter informationConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new UserConverter(roleConverter, productConverter, balanceConverter, informationConverter);
    }

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        RoleEntity roleEntity = RoleEntity.builder()
                .id(1L)
                .name("Test Role")
                .build();

        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .name("Test Product")
                .build();

        UserBalanceEntity balanceEntity = UserBalanceEntity.builder()
                .balance(100.0)
                .build();

        CityEntity cityEntity = CityEntity.builder()
                .id(1L)
                .name("Test City")
                .build();

        UserInformationEntity informationEntity = UserInformationEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .city(cityEntity)
                .age(30)
                .gender(Gender.MALE)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .date_of_registry(new Date())
                .role(roleEntity)
                .products(Collections.singletonList(productEntity))
                .user_balance(balanceEntity)
                .userInformation(informationEntity)
                .build();

        when(roleConverter.toDomain(roleEntity)).thenReturn(
                Role.builder().id(1L).name("Test Role").build());
        when(productConverter.toDomain(productEntity)).thenReturn(
                Product.builder().id(1L).name("Test Product").build());
        when(balanceConverter.toDomain(balanceEntity)).thenReturn(
                UserBalance.builder().balance(100.0).build());
        when(informationConverter.toDomain(informationEntity)).thenReturn(
                UserInformation.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .city(City.builder().id(1L).name("Test City").build())
                        .age(30)
                        .gender(Gender.MALE)
                        .build());

        // When
        User user = converter.toDomain(userEntity);

        // Then
        assertNotNull(user);
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getPassword(), user.getPassword());
        assertEquals(userEntity.getDate_of_registry(), user.getDate_of_registry());

        assertNotNull(user.getRole());
        assertEquals(roleEntity.getId(), user.getRole().getId());
        assertEquals(roleEntity.getName(), user.getRole().getName());

        assertNotNull(user.getProducts());
        assertEquals(1, user.getProducts().size());
        assertEquals(productEntity.getId(), user.getProducts().get(0).getId());
        assertEquals(productEntity.getName(), user.getProducts().get(0).getName());

        assertNotNull(user.getBalance());
        assertEquals(balanceEntity.getBalance(), user.getBalance().getBalance());

        assertNotNull(user.getUserInformation());
        assertEquals(informationEntity.getId(), user.getUserInformation().getId());
        assertEquals(informationEntity.getFirstName(), user.getUserInformation().getFirstName());
        assertEquals(informationEntity.getLastName(), user.getUserInformation().getLastName());
        assertEquals(cityEntity.getId(), user.getUserInformation().getCity().getId());
        assertEquals(cityEntity.getName(), user.getUserInformation().getCity().getName());
        assertEquals(informationEntity.getAge(), user.getUserInformation().getAge());
        assertEquals(informationEntity.getGender(), user.getUserInformation().getGender());
    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        UserEntity userEntity = null;

        // When
        User user = converter.toDomain(userEntity);

        // Then
        assertNull(user);
    }

    @Test
    void testToDomain_givenEntityWithNullFields_shouldHandleGracefully() {
        // Given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .date_of_registry(new Date())
                .role(null)
                .products(null)
                .user_balance(null)
                .userInformation(null)
                .build();

        // When
        User user = converter.toDomain(userEntity);

        // Then
        assertNotNull(user);
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getPassword(), user.getPassword());
        assertEquals(userEntity.getDate_of_registry(), user.getDate_of_registry());

        assertNull(user.getRole());
        assertNotNull(user.getProducts());
        assertEquals(0, user.getProducts().size());
        assertNull(user.getBalance());
        assertNull(user.getUserInformation());
    }
}


