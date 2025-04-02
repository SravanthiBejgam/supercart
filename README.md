# SuperCart
SuperCart Application manage packages consisting of one or more products.
This is a Spring Boot application for managing product packages with currency conversion capabilities.

## Description
This application has below API endpoints to do all the operations, new products can be added but never removed.
 * Create a package 
 * Retrieve a package
 * Update a package
 * Delete a package
 * List all packages
 
API default currency is USD . Used API services such as https://frankfurter.app/ offer the ability to query existing
exchange rates.

## Technologies
- Java 21
- Spring Boot 3.1+
- H2 Database (in-memory)
- Lombok
- JUnit 5 & Mockito
- Gradle
- Docker

### Installation
1. Clone repository:
   ```bash
   git clone https://github.com/SravanthiBejgam/supercart.git

2. Build Application:
   ```bash
   ./gradlew clean build

3. Run application:
   ```bash
   java -Dserver.port=8080 -Dproduct.service.username=<username> -Dproduct.service.password=<password> -jar build/libs/supercart-0.0.1-SNAPSHOT.jar

### API Documentation
Base URL
http://localhost:8080/api/packages

### Endpoints: 
1. Create Package:
   ```bash
   curl -X POST -H "Content-Type: application/json" \
   -d '{
   "name": "Starter Pack",
   "description": "Beginner package",
   "productIds": ["VqKb4tyj9V6i", "DXSQpv6XVeJm"]
   }' \
   http://localhost:8080/api/packages

2. Get Package with Currency Conversion:
   ```bash
   curl "http://localhost:8080/api/packages/1?currency=EUR"

3. Available Products:
   ```bash
   Shield  (ID: VqKb4tyj9V6i) - $1149
   Helmet  (ID: DXSQpv6XVeJm) - $999
   Sword   (ID: 7dgX6XzU3Wds) - $899
   Axe     (ID: PKM5pGAh9yGm) - $799

### Build and Run the Docker Image:
1. Build docker image:
   ```bash
   docker build -t package-service .
2. Run docker Container:
   ```bash
   docker run -d -p 8080:8080 --name package-service-container package-service

### Database Console:
Access H2 console during development:
1. URL: http://localhost:8080/h2-console
2. JDBC URL: jdbc:h2:mem:package_db
3. Username: sa
4. Password: (empty)

### Further improvements which can be added to the application:
1. Rate Limiting
2. Circuit Breaker
3. Audit Logging
4. Monitoring
5. Pagination
6. API Versioning
7. API Documentation
8. Caching and Retry Mechanism
9. Integration Tests
