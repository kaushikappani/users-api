# User API

This API provides endpoints for managing users, including loading users from an external source, searching users, and retrieving users by ID or email.  It uses Spring Boot, H2 database, and OpenAPI for documentation. 

Hosted Backend BASE URL (https://users-api-fjtt.onrender.com)   
Check The Status By https://users-api-fjtt.onrender.com/health   
Swagger URL https://users-api-fjtt.onrender.com/swagger-ui/index.html

## API Endpoints

All endpoints are located under the `/api/users` base path.
**Swagger URL** http://localhost:8080/swagger-ui/index.html#/

**1. Load Users:**

* **Endpoint:** `/api/users/load`
* **Method:** `POST`
* **Summary:** Loads users from an external API into the H2 database.
* **Response:** `200 OK` with the message "Users loaded successfully".

**2. Search Users:**

* **Endpoint:** `/api/users`
* **Method:** `GET`
* **Summary:** Searches users by free text (firstName, lastName, ssn).
* **Parameters:**
    * `search`: (Required) Search term.  Must contain only alphanumeric characters and spaces.
* **Response:** `200 OK` with a list of matching users.  Returns an empty list if no users match.

**3. Get User by ID:**

* **Endpoint:** `/api/users/{id}`
* **Method:** `GET`
* **Summary:** Retrieves a user by ID.
* **Parameters:**
    * `id`: (Required) User ID. Must be a positive number.
* **Response:** `200 OK` with the user details.  `404 Not Found` if the user is not found.

**4. Get User by Email:**

* **Endpoint:** `/api/users/email/{email}`
* **Method:** `GET`
* **Summary:** Retrieves a user by email.
* **Parameters:**
    * `email`: (Required) User email. Must be a valid email address.
* **Response:** `200 OK` with the user details. `404 Not Found` if the user is not found.


## Error Handling

The API returns standard HTTP status codes to indicate success or failure.  Specific error messages are provided in the response body for `404 Not Found` errors.  Validation errors (e.g., invalid search term, negative ID, invalid email) will return a 400 Bad Request with details.

## Building and Running

**Prerequisites:**

* Java 17+ (or compatible JDK)
* Maven or Gradle (build tool)

**Steps:**

1. **Clone the repository:**  (Assume the code is in a Git repository)
   ```bash
   git clone https://github.com/kaushikappani/users-api
   ```

2. **Navigate to the project directory:**
   ```bash
   cd users-api
   ```

3. **Build the project:** (Using Maven)
   ```bash
   mvn clean install
   ```

4. **Run the application:** (Using Maven)
   ```bash
   mvn spring-boot:run
   ```

5. **Access the API:**  The API will be available at `http://localhost:8080/api/users` (or the port specified in your application.properties/application.yml). You can use tools like Postman or curl to test the endpoints.



