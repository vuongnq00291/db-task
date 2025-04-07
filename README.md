## Random Facts API

A REST API for managing, retrieving, and shortening random facts.

## Features

- Fetch random facts from an external API
- Generate shortened URLs for facts
- Retrieve facts by shortened URL
- Redirect to original fact sources
- Get all stored facts
- Get statistics of shortened URLs

##  Environment Requirements
- Java 11+
- Kotlin 1.7.20
- Quarkus 2.15.0.Final
- OpenAPI/Swagger UI
- Docker(optional)

### Docker Deployment

Run the Docker compose:

```bash
docker-compose up -d
```
check on http://localhost:8080/swagger-ui
## Build And Run manually

```bash
   ./gradlew build 
   ./gradlew quarkusDev
```

## API Documentation

The API documentation is available through Swagger UI when the application is running:

http://localhost:8080/swagger-ui

## API Endpoints

| Endpoint                        | Method| Description                                     |
|---------------------------------|-------|-------------------------------------------------|
| `/facts`                        | GET   | Get all facts stored in the system              |
| `/facts`                        | POST  | Create shorten url                              |
| `/facts/{shortenedUrl}`         | GET   | Get a fact by shortened URL                     |
| `/facts/{shortenedUrl}/redirect`| GET   | Redirects to the original permalink of the fact |
| `/admin/statistics`             | GET   | get statistics                                            |


## Project Structure
```
random-facts-api/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/
│   │   │       └── facts/
│   │   │           ├── Application.kt                   # Main application entry point
│   │   │           ├── controller/                      # REST API controllers
│   │   │           │   ├── FactController.kt            # Endpoints for fact operations
│   │   │           │   └── AdminController.kt           # Admin operations (statistics)
│   │   │           ├── client/                          # External API clients
│   │   │           │   ├── UselessFactsClient.kt        # Interface for the external facts API
│   │   │           │   └── exception/                   # Client exception handling
│   │   │           │       ├── UselessFactsClientException.kt
│   │   │           │       └── UselessFactsExceptionMapper.kt
│   │   │           ├── exception/                       # Exception handling
│   │   │           │   ├── ErrorResponse.kt             # Error response model
│   │   │           │   ├── GlobalExceptionMapper.kt     # Global exception handler
│   │   │           │   └── NotFoundException.kt         # Custom exception
│   │   │           ├── dto/                             # dtos
│   │   │           │   ├── ExternalFact.kt              # Fact from external api
│   │   │           │   ├── FactDetailDto.kt             # Fact api response dto
│   │   │           │   ├── FactAccessStatsDto.kt        # Statistics dto
│   │   │           │   └── ShortenedFactDto.kt          # Shortened fact dto
│   │   │           └── service/                         # Business logic
│   │   │               ├── CacheService.kt              # In-memory cache for facts
│   │   │               ├── FactService.kt               # Fact operations
│   │   │               └── UrlShortenerService.kt       # URL shortening logic
│   │   ├── resource/
│   │   │   └── application.properties                   #application configuration
│   └── test/
│       └── kotlin/
│           └── com/
│               └── facts/
│                   └── service/                         # Service tests
│                       ├── CacheServiceTest.kt
│                       ├── FactServiceTest.kt
│                       └── UrlShortenerServiceTest.kt
├── build.gradle.kts                                     # Gradle build configuration
├── settings.gradle.kts                                  # Gradle settings
└── Dockerfile                                           # Dockerfile
└── docker-compose.yml                                   # Docker compose starts application
└── README.md                                            # Project documentation

```

## Key Components

### Controllers:

- **FactController**: Handles requests for creating, retrieving, and redirecting facts
- **AdminController**: Provides statistics about fact usage

### Services:

- **FactService**: Manages fact operations (fetching, retrieving, statistics)
- **CacheService**: In-memory storage for facts with access tracking
- **UrlShortenerService**: Generates shortened URLs for facts

### External API Integration:

- **UselessFactsClient**: REST client for fetching random facts
- Exception handling for external API calls

### Exception Handling:

- Global exception mapper for consistent error responses
- Custom exceptions for specific error scenarios
