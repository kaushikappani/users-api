package com.sapient.userapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.userapi.exception.ResourceNotFoundException;
import com.sapient.userapi.model.User;
import com.sapient.userapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/users")
public class UsersController {
	
	@Autowired
    private UserService userService;

    @Operation(summary = "Load users from external API into H2 DB")
    @PostMapping("/load")
    @CrossOrigin
    public ResponseEntity<String> loadUsers() {
        userService.loadUsers();
        return ResponseEntity.ok("Users loaded successfully");
    }

    @Operation(summary = "Search users by free text (firstName, lastName, ssn)")
    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<User>> searchUsers(
        @RequestParam("search") 
        @NotBlank(message = "Search term must not be blank") 
        @Pattern(regexp = "^[a-zA-Z0-9 \\-]+$", message = "Search term contains invalid characters")
        @Size(min = 3, message = "Search term must be at least 3 characters long")

        String search) {
        List<User> users = userService.searchUsers(search);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get a user by id")
    @GetMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<User> getUserById(
        @PathVariable 
        @Min(value = 1, message = "ID must be a positive number") 
        Long id) {
        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    @Operation(summary = "Get a user by email")
    @GetMapping("/email/{email}")
    @CrossOrigin	
    public ResponseEntity<User> getUserByEmail(
        @PathVariable 
        @Email(message = "Invalid email format") 
        String email) {
        return userService.getUserByEmail(email)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }

}
