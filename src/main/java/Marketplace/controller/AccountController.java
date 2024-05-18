package Marketplace.controller;

import Marketplace.business.*;
import Marketplace.business.dto.authentication.LoginRequest;
import Marketplace.business.dto.authentication.LoginResponse;
import Marketplace.business.dto.user.CreateUserResponse;
import Marketplace.business.dto.user.CreateUserRequest;
import Marketplace.business.dto.user.UpdateUserRequest;
import Marketplace.business.exception.EmailAlreadyExistsException;
import Marketplace.business.exception.InvalidCredentialsException;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import Marketplace.domain.User;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class AccountController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    //CRUD OF USERS
    @PostMapping()
    public ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserRequest request) {
        try {
            CreateUserResponse response = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Email already exists. Please use another one.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unknown error occurred:" + e.getMessage());
        }
    }

    @RolesAllowed({"USER", "ADMIN", "SUPPORT"})
    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") final long userId){
        try{
            final Optional<User> userOptional = userService.getUser(userId);
            return userOptional.map(user -> ResponseEntity.ok().body(user))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (UnauthorizedDataAccessException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RolesAllowed({"USER"})
    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id,
                                           @RequestBody @Valid UpdateUserRequest request){
        request.setId(id);
        userService.updateUser(request);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"USER", "ADMIN"})
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Object> getUserByProductId(@PathVariable("productId") long productId) {
        try {
            final Optional<User> userOptional = userService.getUserByProductId(productId);
            return userOptional
                    .map(user -> ResponseEntity.ok().<Object>body(user))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // AUTHENTICATION & AUTHORIZATION
    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest request) {
        try {
            LoginResponse loginResponse = authenticationService.login(request);
            return ResponseEntity.status(HttpStatus.CREATED).body((Object) loginResponse);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(e.getStatusCode()).body((Object) "Invalid credentials. Please try again.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((Object) e.getMessage());
        }
    }
}