package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.RoleEntity;
import Marketplace.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void save_shouldSaveUser(){
        // Given
        RoleEntity role = RoleEntity.builder()
                .name("USER")
                .build();
        roleRepository.save(role);

        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(role)
                .build();

        // When
        UserEntity savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals(savedUser.getRole().getId(), role.getId());
        assertEquals(savedUser.getRole().getName(), role.getName());
        assertEquals(savedUser, user);
    }

    @Test
    void delete_shouldDeleteUser(){
        // Given
        RoleEntity role = RoleEntity.builder()
                .name("USER")
                .build();
        roleRepository.save(role);

        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(role)
                .build();
        user = userRepository.save(user);

        // When
        userRepository.delete(user);

        // Then
        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void findById_shouldReturnUser(){
        // Given
        RoleEntity role = RoleEntity.builder()
                .name("USER")
                .build();
        roleRepository.save(role);

        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(role)
                .build();
        user = userRepository.save(user);

        // When
        UserEntity foundUser = userRepository.findById(user.getId()).orElse(null);

        // Then
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.getFirstName(), foundUser.getFirstName());
        assertEquals(user.getLastName(), foundUser.getLastName());
        assertEquals(user.getRole(), foundUser.getRole());
    }

    @Test
    void findAll_shouldReturnAllUsers(){
        // Given
        RoleEntity role = RoleEntity.builder()
                .name("USER")
                .build();
        roleRepository.save(role);

        UserEntity user1 = UserEntity.builder()
                .email("test1@example.com")
                .password("password1")
                .firstName("John")
                .lastName("Doe")
                .role(role)
                .build();
        userRepository.save(user1);

        UserEntity user2 = UserEntity.builder()
                .email("test2@example.com")
                .password("password2")
                .firstName("Jane")
                .lastName("Smith")
                .role(role)
                .build();
        userRepository.save(user2);

        // When
        List<UserEntity> allUsers = userRepository.findAll();

        // Then
        assertNotNull(allUsers);
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }

}
