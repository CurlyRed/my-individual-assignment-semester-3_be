package Marketplace.controller;

import Marketplace.business.*;
import Marketplace.business.dto.user.CreateUserResponse;
import Marketplace.business.dto.user.CreateUserRequest;
import Marketplace.business.dto.user.UpdateUserRequest;
import Marketplace.domain.User;

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

    @PostMapping()
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") final long userId){
        final Optional<User> userOptional = userService.getUser(userId);
        return userOptional.map(user -> ResponseEntity.ok().body(user)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id,
                                           @RequestBody @Valid UpdateUserRequest request){
        request.setId(id);
        userService.updateUser(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
