# FTR IAM Authentication API Documentation

This document describes the authentication API endpoints for the FTR IAM service.

## Base URL
```
http://localhost:7200
```

## Authentication Flow

### 1. User Registration
**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securepassword123"
}
```

**Response (Success):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "john_doe",
    "email": "john@example.com",
    "message": "User registered successfully"
}
```

**Response (Error):**
```json
{
    "token": null,
    "username": null,
    "email": null,
    "message": "Username already exists"
}
```

### 2. User Login
**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
    "username": "john_doe",
    "password": "securepassword123"
}
```

**Response (Success):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "john_doe",
    "email": "john@example.com",
    "message": "Login successful"
}
```

**Response (Error):**
```json
{
    "token": null,
    "username": null,
    "email": null,
    "message": "Invalid username or password"
}
```

### 3. Protected Endpoints
To access protected endpoints, include the JWT token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Example Protected Endpoint:** `GET /api/test/protected`

## Database Schema

The system uses two tables:

### users table
- `user_id` (UUID, Primary Key)
- `username` (String, Unique)
- `email` (String, Unique)
- `created_by` (String)
- `created_date` (Date)
- `updated_by` (String)
- `updated_date` (Date)

### users_info table
- `user_id` (UUID, Primary Key, Foreign Key to users)
- `username` (String)
- `salt` (String) - Random salt for password hashing
- `password_hash` (String) - BCrypt hashed password with salt
- `created_by` (String)
- `created_date` (Date)
- `updated_by` (String)
- `updated_date` (Date)

## Security Features

1. **Password Hashing**: Uses BCrypt with random salt for secure password storage
2. **JWT Tokens**: Stateless authentication with 24-hour token expiration
3. **Input Validation**: Bean validation for all request DTOs
4. **Unique Constraints**: Username and email uniqueness enforced
5. **Transaction Management**: Registration process is transactional

## Testing the API

### Using curl

1. **Register a new user:**
```bash
curl -X POST http://localhost:7200/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

2. **Login:**
```bash
curl -X POST http://localhost:7200/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

3. **Access protected endpoint:**
```bash
curl -X GET http://localhost:7200/api/test/protected \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Using Postman

1. Set the base URL to `http://localhost:7200`
2. For registration/login, use POST requests to `/api/auth/register` or `/api/auth/login`
3. For protected endpoints, add the JWT token to the Authorization header as `Bearer YOUR_TOKEN`

## Error Handling

The API returns appropriate HTTP status codes:
- `200 OK`: Successful operation
- `400 Bad Request`: Validation errors or business logic errors
- `401 Unauthorized`: Invalid credentials or missing token
- `500 Internal Server Error`: Server errors

## Configuration

The application is configured to run on port 7200 and connect to a PostgreSQL database. Update the database connection in `application.properties` if needed.

## Dependencies

- Spring Boot 3.5.0
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (JJWT library)
- Lombok
- Validation API 