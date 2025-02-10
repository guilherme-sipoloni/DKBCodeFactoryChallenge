# DKB Code Factory Challenge

 ## Overview
This is a URL shortener application built using Spring Boot and Kotlin. 
It allows users to shorten long URLs into compact, shareable links. 
The application uses PostgreSQL as the database to store the mappings between short codes and original URLs.

The short code is a 6-character random combination of letters upper case, lower case, and digits (62^6 around 56 billion possibilities)
To handle the rare case of a duplicate short code (collision), I implemented a retry mechanism. 
Instead of checking for duplicates before insertion, I chose to handle collisions by catching the database exception 
and retrying with a new short code. This approach is more efficient because The probability of a collision is extremely low, 
and It avoids the overhead of pre-validation, which would require an additional database query for every short code generation.

## Technologies Used
- Backend: Spring Boot, Kotlin

- Database: PostgreSQL

- Testing: JUnit 5, MockK

- Containerization: Docker, Docker Compose

- Build Tool: Gradle

---

## How It Works
The application provides two main endpoints:

1. Shorten a URL: `POST http://localhost:8080/api/shorten`
   - Converts a long URL into a short code.

2. Redirect to Original URL: `GET http://localhost:8080/api/abc123`
   - Redirects the user to the original URL using the short code.

--- 

## Getting Started
### Prerequisites
- Java 21
- Docker and Docker Compose
- Gradle (optional, as the project includes a Gradle wrapper)
--- 
## Building the Application
To build the application, run the following command:

```bash
./gradlew build
```
--- 

## Running Tests
To run the unit and integration tests, use the following command:

```bash
./gradlew test
```
---

## Running with Docker Compose
Please build the project before build the Docker images and start the services

```bash
docker-compose up --build
```
Access the application at http://localhost:8080.

To stop the services, run:

```bash
docker-compose down
```

---

## API Endpoints

### 1. Shorten a URL
#### Request
```bash
curl -X POST http://localhost:8080/api/shorten \
-H "Content-Type: application/json" \
-d '{"url": "https://google.com"}'
```
#### Response
```json
{
  "shortUrl": "http://localhost:8080/api/abc123"
}
```
### 2. Redirect to Original URL
#### Request
```bash
curl -v http://localhost:8080/api/abc123
```
   #### Response
```bash
< HTTP/1.1 302 Found
< Location: https://google.com
```

--- 
## Future Improvements
Here are some improvements that I think we could do for the project:

1. User Authentication:
    - Allow users to create accounts and manage their shortened URLs.

2. Analytics:
   - Track how many times a short URL is accessed.

3. Flyway migration:
   -  Allow a better control of new database changes

4. Custom Short Codes:
   - Allow users to specify custom short codes.

5. URL Expiration:
   - Add an expiration date for short URLs.

6. Frontend:
   - Build a user-friendly frontend for the application.

7. Deployment:
   - Deploy the application to a cloud platform (e.g., AWS, Google Cloud, Heroku).