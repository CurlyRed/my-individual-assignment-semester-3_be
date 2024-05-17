package Marketplace.business.impl;

import Marketplace.business.AuthenticationService;
import Marketplace.business.dto.authentication.LoginRequest;
import Marketplace.business.dto.authentication.LoginResponse;
import Marketplace.business.exception.InvalidCredentialsException;
import Marketplace.config.security.token.AccessTokenEncoder;
import Marketplace.config.security.token.impl.AccessTokenImpl;
import Marketplace.persistence.entity.UserEntity;
import Marketplace.persistence.jpaRepository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail());

        if(user == null){
            throw new InvalidCredentialsException();
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private String generateAccessToken(UserEntity user) {
        Long userId = user != null ? user.getId() : null;
        String role = user.getRole().getName();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), userId, role));
    }
}
