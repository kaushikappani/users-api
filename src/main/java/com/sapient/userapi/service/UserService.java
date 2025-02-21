package com.sapient.userapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.sapient.userapi.exception.ResourceNotFoundException;
import com.sapient.userapi.model.ExternalUser;
import com.sapient.userapi.model.User;
import com.sapient.userapi.model.UserResponse;
import com.sapient.userapi.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${api.user.GET.url}")
	private String url;

	@Transactional
	@CircuitBreaker(name = "userService", fallbackMethod = "fallbackLoadUsers")
	@Retryable(retryFor = { RestClientException.class,
			ResourceNotFoundException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
	public void loadUsers() {
		log.info("Attempting to load users...");
		ResponseEntity<UserResponse> responseEntity = restTemplate.getForEntity(url, UserResponse.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			UserResponse response = responseEntity.getBody();
			if (response == null || response.getUsers() == null || response.getUsers().isEmpty()) {
				throw new ResourceNotFoundException("No users found in the external API response.");
			}
			List<User> users = response.getUsers().stream().map(this::mapToUser).collect(Collectors.toList());
			userRepository.saveAll(users);
			log.info("Loaded {} users into the database.", users.size());
		} else {
			throw new ResourceNotFoundException(
					"Failed to fetch users: HTTP status code " + responseEntity.getStatusCode());
		}
	}

	public void fallbackLoadUsers(Throwable ex) {
		log.error("Fallback method called due to: {}", ex.getMessage());
	}

	@Transactional
	public List<User> searchUsers(String text) {
		log.info("Searching users for text: {}", text);

		return userRepository.searchUsers(text);
	}

	private User mapToUser(ExternalUser extenalUser) {
		User user = new User();
		user.setFirstName(extenalUser.getFirstName());
		user.setLastName(extenalUser.getLastName());
		user.setSsn(extenalUser.getSsn());
		user.setEmail(extenalUser.getEmail());
		user.setBirthDate(extenalUser.getBirthDate());
		user.setImage(extenalUser.getImage());
		user.setPhone(extenalUser.getPhone());
		user.setUniversity(extenalUser.getUniversity());
		return user;
	}

	public Optional<User> getUserById(Long id) {
		log.info("Searching users for id: {}", id);

		return userRepository.findById(id);
	}

	public Optional<User> getUserByEmail(String email) {
		log.info("Searching users for email: {}", email);

		return userRepository.findByEmail(email);
	}

}
