# Metasnap Backend

This is the backend for the Metasnap project, a SEO meta tag analyzer built with Spring Boot.

## Features
- RESTful API for analyzing SEO meta tags
- Built with Spring Boot 3.5
- Input validation and web endpoints
- Uses jsoup for HTML parsing

## Getting Started

### Prerequisites
- [Java 17+](https://adoptopenjdk.net/)
- [Maven 3.6+](https://maven.apache.org/)

### Installation & Running
1. **Clone the repository** (if you haven't already):
   ```sh
   git clone <your-repo-url>
   cd metasnap-backend
   ```

2. **Build the project:**
   ```sh
   mvn clean install
   ```

3. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   The backend will start on [http://localhost:8080](http://localhost:8080) by default.

### API Usage
- The backend exposes REST endpoints for meta tag analysis.
- See the controller classes in `src/main/java/com/metasnap/metasnap_backend/controller/` for available endpoints.

### Running Tests
```sh
mvn test
```

### Test Coverage
The project includes comprehensive unit tests for:
- **Service Layer**: `MetaAnalyzerServiceTest` - Tests URL analysis, meta tag extraction, scoring, and error handling
- **Controller Layer**: `MetaAnalyzerControllerTest` - Tests REST endpoints, request/response handling, and error scenarios

## Project Structure
- `src/main/java` - Java source code
- `src/test/java` - Test code
- `pom.xml` - Maven project configuration

## Useful References
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/guides/index.html)
- [jsoup HTML Parser](https://jsoup.org/)

## License
MIT (or specify your license) 