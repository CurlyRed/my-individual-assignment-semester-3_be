package Marketplace.business;

import Marketplace.business.dto.authentication.LoginRequest;
import Marketplace.business.dto.authentication.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
}
