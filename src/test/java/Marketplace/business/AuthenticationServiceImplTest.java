package Marketplace.business;

import Marketplace.business.dto.authentication.LoginRequest;
import Marketplace.business.dto.authentication.LoginResponse;
import Marketplace.business.exception.InvalidCredentialsException;
import Marketplace.business.impl.AuthenticationServiceImpl;
import Marketplace.config.security.token.AccessToken;
import Marketplace.config.security.token.AccessTokenEncoder;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.persistence.jpaRepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccessTokenEncoder accessTokenEncoder;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLogin_withValidCredentials_shouldReturnLoginResponse() {
        // Given
        LoginRequest loginRequest = createValidLoginRequest();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail(loginRequest.getEmail());
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(userEntity);
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(any(AccessToken.class))).thenReturn("accessToken");

        // When
        LoginResponse response = authenticationService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());

        // Verify
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getPassword(), userEntity.getPassword());
        verify(accessTokenEncoder, times(1)).encode(any(AccessToken.class));
    }

    @Test
    void testLogin_withNonExistentUser_shouldThrowException() {
        // Given
        LoginRequest loginRequest = createValidLoginRequest();

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(null);

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(loginRequest));

        // Verify
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verifyNoInteractions(passwordEncoder, accessTokenEncoder);
    }

    @Test
    void testLogin_withIncorrectPassword_shouldThrowException() {
        // Given
        LoginRequest loginRequest = createValidLoginRequest();
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(loginRequest.getEmail());
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(userEntity);
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())).thenReturn(false);

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(loginRequest));

        // Verify
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getPassword(), userEntity.getPassword());
        verifyNoInteractions(accessTokenEncoder);
    }

    private LoginRequest createValidLoginRequest() {
        return LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
    }
}
