package com.sapient.userapi.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.sapient.userapi.model.ExceptionResponse;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

	@InjectMocks
	private GlobalExceptionHandler exceptionHandler;

	@BeforeEach
	void setUp() {
		exceptionHandler = new GlobalExceptionHandler();
	}

	@Test
	void testHandleNotFound() {
		String errorMessage = "Resource not found error";
		ResourceNotFoundException ex = new ResourceNotFoundException(errorMessage);

		ResponseEntity<ExceptionResponse> responseEntity = exceptionHandler.handleNotFound(ex);
		ExceptionResponse responseBody = responseEntity.getBody();

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.toString());
		assertThat(responseBody.getMessage()).isEqualTo(errorMessage);
	}

	@Test
	void testHandlerMethodValidationException() {
		String errorMessage = "Invalid Request Error";

		HandlerMethodValidationException ex = Mockito.mock(HandlerMethodValidationException.class);
		when(ex.getMessage()).thenReturn(errorMessage);

		ResponseEntity<ExceptionResponse> responseEntity = exceptionHandler.handlerMethodValidationException(ex);
		ExceptionResponse responseBody = responseEntity.getBody();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.toString());
		assertThat(responseBody.getMessage()).isEqualTo(errorMessage);
	}

	@Test
	void testHandleGenericException() {
		String errorMessage = "Something went wrong";
		Exception ex = new Exception(errorMessage);

		ResponseEntity<ExceptionResponse> responseEntity = exceptionHandler.handleGeneric(ex);
		ExceptionResponse responseBody = responseEntity.getBody();

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(responseBody).isNotNull();
		assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		assertThat(responseBody.getMessage()).isEqualTo("An Error Occured");
	}

}
