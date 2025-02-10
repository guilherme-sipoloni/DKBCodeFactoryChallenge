# DKB Code Factory Challenge

 ## Overview
This is a URL shortener application built using Spring Boot and Kotlin. 
It allows users to shorten long URLs into compact, shareable links. 
The application uses PostgreSQL as the database to store the mappings between short codes and original URLs.

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

3. Custom Short Codes:
   - Allow users to specify custom short codes.

4. URL Expiration:
   - Add an expiration date for short URLs.

5. Frontend:
   - Build a user-friendly frontend for the application.

6. Deployment:
   - Deploy the application to a cloud platform (e.g., AWS, Google Cloud, Heroku).