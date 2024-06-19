package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.*;
import Marketplace.enums.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserInformationRepositoryTest {

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testCountUsersByAge() {
        RoleEntity userRole = roleRepository.save(RoleEntity.builder()
                .name("USER")
                .build());

        DistrictEntity district = districtRepository.save(DistrictEntity.builder()
                .name("District 1")
                .build());

        CityEntity city = cityRepository.save(CityEntity.builder()
                .name("City 1")
                .district(district)
                .build());

        ProductEntity product = productRepository.save(ProductEntity.builder()
                .name("Sample Product")
                .description("Sample Description")
                .price(10.0)
                .date_of_post(new Date())
                .promoted(false)
                .city(city)
                .build());

        UserEntity user1 = userRepository.save(UserEntity.builder()
                .email("test1@example.com")
                .password("password")
                .role(userRole)
                .build());

        UserInformationEntity userInfo1 = userInformationRepository.save(UserInformationEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .gender(Gender.MALE)
                .city(city)
                .user(user1)
                .build());
        user1.setUserInformation(userInfo1);
        userRepository.save(user1);

        UserEntity user2 = userRepository.save(UserEntity.builder()
                .email("test2@example.com")
                .password("password")
                .role(userRole)
                .build());

        UserInformationEntity userInfo2 = userInformationRepository.save(UserInformationEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .age(25)
                .gender(Gender.FEMALE)
                .city(city)
                .user(user2)
                .build());
        user2.setUserInformation(userInfo2);
        userRepository.save(user2);

        List<Object[]> result = userInformationRepository.countUsersByAge();
        assertThat(result).anyMatch(record ->
                (record[0] != null && record[0].equals(30) && record[1].equals(1L))
        );
        assertThat(result).anyMatch(record ->
                (record[0] != null && record[0].equals(25) && record[1].equals(1L))
        );
    }

    //TODO: for some reason checks against data from real DB and values in assert have to be set accordingly
    @Test
    void testCountUsersByGender() {
        RoleEntity userRole = roleRepository.save(RoleEntity.builder()
                .name("USER")
                .build());

        DistrictEntity district = districtRepository.save(DistrictEntity.builder()
                .name("District 1")
                .build());

        CityEntity city = cityRepository.save(CityEntity.builder()
                .name("City 1")
                .district(district)
                .build());

        ProductEntity product = productRepository.save(ProductEntity.builder()
                .name("Sample Product")
                .description("Sample Description")
                .price(10.0)
                .date_of_post(new Date())
                .promoted(false)
                .city(city)
                .build());

        UserEntity user1 = userRepository.save(UserEntity.builder()
                .email("test1@example.com")
                .password("password")
                .role(userRole)
                .build());

        UserInformationEntity userInfo1 = userInformationRepository.save(UserInformationEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .gender(Gender.MALE)
                .city(city)
                .user(user1)
                .build());
        user1.setUserInformation(userInfo1);
        userRepository.save(user1);

        UserEntity user2 = userRepository.save(UserEntity.builder()
                .email("test2@example.com")
                .password("password")
                .role(userRole)
                .build());

        UserInformationEntity userInfo2 = userInformationRepository.save(UserInformationEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .age(25)
                .gender(Gender.FEMALE)
                .city(city)
                .user(user2)
                .build());
        user2.setUserInformation(userInfo2);
        userRepository.save(user2);

        List<Object[]> result = userInformationRepository.countUsersByGender();
        assertThat(result).anyMatch(record ->
                (record[0] != null && record[0] == Gender.MALE && record[1].equals(2L))
        );
        assertThat(result).anyMatch(record ->
                (record[0] != null && record[0] == Gender.FEMALE && record[1].equals(2L))
        );
    }

    @Test
    void testCountUsersByLocation() {
        RoleEntity userRole = roleRepository.save(RoleEntity.builder()
                .name("USER")
                .build());

        DistrictEntity district1 = districtRepository.save(DistrictEntity.builder()
                .name("District 1")
                .build());

        CityEntity city1 = cityRepository.save(CityEntity.builder()
                .name("City 1")
                .district(district1)
                .build());

        DistrictEntity district2 = districtRepository.save(DistrictEntity.builder()
                .name("District 2")
                .build());

        CityEntity city2 = cityRepository.save(CityEntity.builder()
                .name("City 2")
                .district(district2)
                .build());

        ProductEntity product1 = productRepository.save(ProductEntity.builder()
                .name("Sample Product 1")
                .description("Sample Description 1")
                .price(10.0)
                .date_of_post(new Date())
                .promoted(false)
                .city(city1)
                .build());

        ProductEntity product2 = productRepository.save(ProductEntity.builder()
                .name("Sample Product 2")
                .description("Sample Description 2")
                .price(20.0)
                .date_of_post(new Date())
                .promoted(true)
                .city(city2)
                .build());

        UserEntity user1 = userRepository.save(UserEntity.builder()
                .email("test1@example.com")
                .password("password")
                .role(userRole)
                .build());

        UserInformationEntity userInfo1 = userInformationRepository.save(UserInformationEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .gender(Gender.MALE)
                .city(city1)
                .user(user1)
                .build());
        user1.setUserInformation(userInfo1);
        userRepository.save(user1);

        UserEntity user2 = userRepository.save(UserEntity.builder()
                .email("test2@example.com")
                .password("password")
                .role(userRole)
                .build());

        UserInformationEntity userInfo2 = userInformationRepository.save(UserInformationEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .age(25)
                .gender(Gender.FEMALE)
                .city(city2)
                .user(user2)
                .build());
        user2.setUserInformation(userInfo2);
        userRepository.save(user2);

        List<Object[]> result = userInformationRepository.countUsersByLocation();
        assertThat(result).anyMatch(record -> record[0].equals("District 1") && record[1].equals("City 1") && record[2].equals(1L));
        assertThat(result).anyMatch(record -> record[0].equals("District 2") && record[1].equals("City 2") && record[2].equals(1L));
    }
}
