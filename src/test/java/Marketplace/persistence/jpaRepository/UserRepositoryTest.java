package Marketplace.persistence.jpaRepository;

import Marketplace.enums.TransactionType;
import Marketplace.persistence.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private DistrictRepository districtRepository;

    @Test
    void testFindByProductId() {
        AttributeEntity attribute = attributeRepository.save(AttributeEntity.builder()
                .name("Test")
                .build());
        CategoryEntity category = CategoryEntity.builder()
                .name("Test")
                .build();
        category.setAttributes(List.of(attribute));
        categoryRepository.save(category);

        RoleEntity role =  roleRepository.save(RoleEntity.builder()
                .name("test")
                .build());

        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .date_of_registry(new Date())
                .role(role)
                .build();

        DistrictEntity district = districtRepository.save(DistrictEntity.builder()
                .name("Test")
                .build());

        CityEntity city = cityRepository.save (CityEntity.builder()
                .name("Test")
                .district(district)
                .build());


        ProductEntity product = ProductEntity.builder()
                .name("Test Product")
                        .description("Test")
                        .price(100.0)
                        .date_of_post(new Date())
                        .promoted(false)
                        .user(user)
                        .city(city)
                .build();

        user.setProducts(List.of(product));
        userRepository.save(user);

        Optional<UserEntity> foundUser = userRepository.findByProductId(product.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testCountAllUsers() {
        RoleEntity userRole = roleRepository.save(RoleEntity.builder()
                .name("USER")
                .build());

        userRepository.save(UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .role(userRole)
                .build());

        long userCount = userRepository.countAllUsers();
        assertThat(userCount).isGreaterThan(0);
    }

    @Test
    void testCountNewUsersSince() {
        RoleEntity userRole = roleRepository.save(RoleEntity.builder()
                .name("USER")
                .build());

        userRepository.save(UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .role(userRole)
                .date_of_registry(new Date())
                .build());

        long newUsersCount = userRepository.countNewUsersSince(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24));
        assertThat(newUsersCount).isGreaterThan(0);
    }

    @Test
    void testCountActiveUsersBetweenDates() {
        RoleEntity userRole = roleRepository.save(RoleEntity.builder()
                .name("USER")
                .build());

        UserEntity user = userRepository.save(UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .role(userRole)
                .build());

        transactionRepository.save(TransactionEntity.builder()
                .user(user)
                .amount(100.0)
                .created_at(new Date())
                .type(TransactionType.TOP_UP)
                .build());

        long activeUsersCount = userRepository.countActiveUsersBetweenDates(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7), new Date());
        assertThat(activeUsersCount).isGreaterThan(0);
    }
}
