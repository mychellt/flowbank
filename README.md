# FlowBank

A Spring Boot-based banking application for managing accounts and transactions.

## Features

- Account and transaction management
- Persistence with Spring Data JPA
- Domain-driven design with mappers and adapters
- RESTful API for account and transaction operations
- Database migration with Flyway
- OpenAPI documentation (Swagger UI)

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Maven
- MapStruct
- Flyway
- PostgreSQL (default, configurable)

## Project Structure

- `domain`: Business models and ports
- `adapters`: Mappers, REST controllers, and persistence adapters
- `application`: Application services and business logic
- `infrastructure`: JPA entities and database configuration
- `configuration`: Spring and security configuration

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- A running SQL database (e.g., PostgreSQL, MySQL)

### Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

### Running with Docker

You can use Docker Compose to start the application and a PostgreSQL database:

```bash
docker-compose up --build
```

### API Documentation

After starting the application, access the OpenAPI documentation at:

```
http://localhost:8080/swagger-ui.html
```

## Configuration

Edit `src/main/resources/application.properties` to configure database connection and other settings.

## Database Migration

Flyway is used for database migrations. Migration scripts are located in `src/main/resources/db/migration`.

## Testing

Run all tests with:

```bash
mvn test
```

## License

This project is licensed under the MIT License.

