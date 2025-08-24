# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.5 application using Java 21, built with Gradle. The application integrates with MariaDB and Redis databases, and includes comprehensive Testcontainers support for both development and testing.

## Build and Development Commands

### Build and Run
- `./gradlew build` - Build the entire project
- `./gradlew bootRun` - Run the application locally
- `./gradlew clean build` - Clean build from scratch

### Testing
- `./gradlew test` - Run all tests
- `./gradlew test --tests "ClassName"` - Run specific test class
- `./gradlew test --tests "*.methodName"` - Run specific test method

### Docker Compose Integration
- The project includes `compose.yaml` with MariaDB and Redis services
- Spring Boot's Docker Compose support automatically starts these services during development
- Use `TestDemoApplication.java` to run the application with Testcontainers for development

## Architecture

### Core Components
- **DemoApplication.java** - Main Spring Boot application class
- **TestcontainersConfiguration.java** - Configures MariaDB and Redis containers for testing
- **TestDemoApplication.java** - Development runner with embedded Testcontainers

### Database Integration
- MariaDB as primary database with Testcontainers support
- Redis for caching/session storage
- All database connections automatically managed via Spring Boot's ServiceConnection

### Testing Strategy
- JUnit 5 platform with Spring Boot Test
- Testcontainers for integration testing with real database instances
- Tests automatically import TestcontainersConfiguration for consistent test environment

## Key Dependencies
- Spring Boot Starter Web (REST API development)
- Spring Boot Starter Data Redis
- Spring Boot Starter Actuator (monitoring/health checks)
- Testcontainers (MariaDB, Redis)
- MariaDB JDBC driver

## Health Checks

The application includes Spring Boot Actuator for comprehensive health monitoring and status checks.

### Health Check Endpoints
- **Health**: `GET /actuator/health` - Overall application health status
- **Info**: `GET /actuator/info` - Application information and metadata

### Health Check Commands
```bash
# Basic health check
curl http://localhost:8080/actuator/health

# Pretty-printed health check (requires jq)
curl http://localhost:8080/actuator/health | jq

# Access via browser
http://localhost:8080/actuator/health
```

### Component Health Status
The health endpoint provides detailed status for all integrated components:

- **Database (MariaDB)** - Connection status and query validation
- **Redis** - Connection status and ping response
- **Disk Space** - Available disk space and threshold monitoring
- **Application** - Overall Spring Boot application health

### Health Response Format
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": { "database": "MariaDB", "validationQuery": "isValid()" }
    },
    "redis": {
      "status": "UP",
      "details": { "version": "7.x" }
    },
    "diskSpace": {
      "status": "UP",
      "details": { "total": "xxx", "free": "xxx", "threshold": "xxx" }
    }
  }
}
```

### Status Meanings
- **UP** - Component is healthy and operational
- **DOWN** - Component is not responding or failed
- **OUT_OF_SERVICE** - Component is temporarily unavailable
- **UNKNOWN** - Component health cannot be determined

### Integration Notes
- Health checks automatically validate MariaDB and Redis containers when using Docker Compose
- Testcontainers setup ensures health validation during testing
- Configure monitoring tools to poll `/actuator/health` for automated alerting
- Health details are always shown (configured in `application.properties`)

## Development Notes
- Java 21 language features available
- Gradle wrapper included (`./gradlew` on Unix, `gradlew.bat` on Windows)
- Application properties: `src/main/resources/application.properties`
- Base package: `com.example.demo`