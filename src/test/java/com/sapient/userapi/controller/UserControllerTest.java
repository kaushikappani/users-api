package com.sapient.userapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.sapient.userapi.exception.ResourceNotFoundException;
import com.sapient.userapi.model.User;
import com.sapient.userapi.service.UserService;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UsersController usersController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Appani");
        user.setLastName("Kaushik");
        user.setSsn("123-45-6789");
        user.setEmail("kaushikappani@gmail.com");
    }

    @Test
    void testLoadUsers_Success() {
        doNothing().when(userService).loadUsers();
        ResponseEntity<String> response = usersController.loadUsers();
        assertEquals("Users loaded successfully", response.getBody());
    }

    @Test
    void testSearchUsers_Success() {
        when(userService.searchUsers("Appani"))
            .thenReturn(Arrays.asList(user));
        ResponseEntity<List<User>> response = usersController.searchUsers("Appani");
        assertEquals(1, response.getBody().size());
        assertEquals("Appani", response.getBody().get(0).getFirstName());
    }

    @Test
    void testGetUserById_Success() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = usersController.getUserById(1L);
        assertEquals("Appani", response.getBody().getFirstName());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> usersController.getUserById(1L));
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userService.getUserByEmail("kaushikappani@gmail.com"))
            .thenReturn(Optional.of(user));
        ResponseEntity<User> response = usersController.getUserByEmail("kaushikappani@gmail.com");
        assertEquals("Appani", response.getBody().getFirstName());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userService.getUserByEmail("kaushikappani@gmail.com"))
            .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> usersController.getUserByEmail("kaushikappani@gmail.com"));
    }
}
