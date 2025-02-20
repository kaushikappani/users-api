package com.sapient.userapi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.sapient.userapi.exception.ResourceNotFoundException;
import com.sapient.userapi.model.ExternalUser;
import com.sapient.userapi.model.User;
import com.sapient.userapi.model.UserResponse;
import com.sapient.userapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    private User user;
    private ExternalUser externalUser;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Appani");
        user.setLastName("Kaushik");
        user.setSsn("123-45-6789");
        user.setEmail("kaushikappani@gmail.com");

        externalUser = new ExternalUser();
        externalUser.setFirstName("Appani");
        externalUser.setLastName("Kaushik");
        externalUser.setSsn("123-45-6789");
        externalUser.setEmail("kaushikappani@gmail.com");
        
        ReflectionTestUtils.setField(userService, "url", "http://mocked-url.com");

    }

    @Test
    void testLoadUsers_Success() {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsers(Arrays.asList(externalUser));
        ResponseEntity<UserResponse> responseEntity = new ResponseEntity<>(userResponse, HttpStatus.OK);
        
        when(restTemplate.getForEntity(anyString(), eq(UserResponse.class))).thenReturn(responseEntity);
        when(userRepository.saveAll(anyList())).thenReturn(Arrays.asList(user));

        assertDoesNotThrow(() -> userService.loadUsers());
        verify(userRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testLoadUsers_ThrowsExceptionWhenNoUsers() {
        UserResponse userResponse = new UserResponse();
        ResponseEntity<UserResponse> responseEntity = new ResponseEntity<>(userResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(UserResponse.class))).thenReturn(responseEntity);
        
        assertThrows(ResourceNotFoundException.class, () -> userService.loadUsers());
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("Appani", foundUser.get().getFirstName());
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("kaushikappani@gmail.com")).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.getUserByEmail("kaushikappani@gmail.com");

        assertTrue(foundUser.isPresent());
        assertEquals("Appani", foundUser.get().getFirstName());
    }

    @Test
    void testSearchUsers() {
        when(userRepository.searchUsers("Appani")).thenReturn(List.of(user));
        List<User> result = userService.searchUsers("Appani");
        
        assertEquals(1, result.size());
        assertEquals("Appani", result.get(0).getFirstName());
    }
}
