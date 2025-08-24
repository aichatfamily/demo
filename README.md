# Demo Spring Boot Application

A Spring Boot 3.5.5 application with Java 21 that demonstrates custom health check implementation for database monitoring.

## Features

- **Spring Boot Web** - RESTful web services
- **MariaDB Integration** - Primary database with JPA support
- **Redis Integration** - Caching and session storage
- **Custom Health Checks** - Combined health monitoring for both databases
- **Docker Compose** - Containerized development environment
- **Testcontainers** - Integration testing with real database instances

## Getting Started

### Prerequisites

- Java 21
- Docker and Docker Compose
- Gradle (wrapper included)

### Running the Application

1. **Start the application** (Docker Compose will automatically start MariaDB and Redis):
   ```bash
   ./gradlew bootRun
   ```

2. **Access the application**:
   - Main application: http://localhost:8080
   - Health endpoints: http://localhost:8080/actuator/health

### Database Configuration

The application uses Docker Compose to automatically start and configure:
- **MariaDB** on port 3306 (database: `mydatabase`, user: `myuser`)
- **Redis** on port 6379

## Health Check Endpoints

### Main Health Endpoint
```
GET /actuator/health
```

Returns overall application health including all components:

```json
{
  "status": "UP",
  "components": {
    "databases": {
      "status": "UP",
      "details": {
        "redis": {
          "status": "UP",
          "details": {
            "version": "8.2.1"
          }
        },
        "mariadb": {
          "status": "UP",
          "details": {
            "database": "MariaDB",
            "validationQuery": "isValid()"
          }
        }
      }
    },
    "db": { "status": "UP" },
    "redis": { "status": "UP" },
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```

### Databases Health Endpoint
```
GET /actuator/health/databases
```

Returns detailed health information for both MariaDB and Redis:

```json
{
  "status": "UP",
  "details": {
    "redis": {
      "status": "UP",
      "details": {
        "version": "8.2.1"
      }
    },
    "mariadb": {
      "status": "UP",
      "details": {
        "database": "MariaDB",
        "validationQuery": "isValid()"
      }
    }
  }
}
```

## Development

### Build Commands
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

### Testing
The application includes comprehensive integration tests using Testcontainers:
- Health endpoint testing
- Database connectivity verification
- Redis functionality validation

### Architecture

#### Custom Health Indicator
- **DatabasesHealthIndicator**: Combines MariaDB and Redis health checks
- Uses Spring Boot's built-in `DataSourceHealthIndicator` and `RedisHealthIndicator`
- Provides unified status (UP only if both databases are healthy)
- Returns detailed information for troubleshooting

#### Configuration
- **HealthIndicatorConfig**: Provides bean definitions for individual health indicators
- **Application Properties**: Database and Redis connection configuration
- **Docker Compose**: Development environment setup

## Project Structure

```
src/
├── main/java/com/example/demo/
│   ├── DemoApplication.java              # Main application class
│   ├── DatabasesHealthIndicator.java     # Custom health indicator
│   └── HealthIndicatorConfig.java        # Health indicator configuration
├── main/resources/
│   └── application.properties            # Application configuration
└── test/java/com/example/demo/
    ├── DemoApplicationTests.java         # Basic application tests
    ├── DatabasesHealthIndicatorTest.java # Health indicator tests
    ├── TestDemoApplication.java          # Test application runner
    └── TestcontainersConfiguration.java  # Test container setup
```

## Monitoring

The application provides comprehensive health monitoring through Spring Boot Actuator:
- Database connectivity status
- Redis connection and version information
- System resources (disk space, etc.)
- Custom combined database health status

For production monitoring, the health endpoints can be integrated with monitoring tools like Prometheus, Grafana, or application performance monitoring (APM) systems.