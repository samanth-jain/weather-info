# Weather API Assignment

A Spring Boot REST API to fetch and cache weather information for a given Indian pincode and date, using OpenWeatherMap APIs and PostgreSQL.

## Features
- Get weather for a pincode and date
- Caches pincode geolocation and weather data in PostgreSQL
- Optimized to minimize external API calls
- Testable via Postman/Swagger
- TDD-ready with sample tests

## Setup

### 1. Prerequisites
- Java 21+
- Maven
- PostgreSQL (running, with a database named `weatherdb`)
- OpenWeatherMap API key

### 2. Environment Variables
Set your OpenWeatherMap API key as an environment variable:

**Windows CMD:**
```
set OPENWEATHERMAP_API_KEY=your_api_key_here
```
**Linux/Mac:**
```
export OPENWEATHERMAP_API_KEY=your_api_key_here
```

### 3. Database Configuration
Edit `src/main/resources/application.properties` if needed:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/weatherdb
spring.datasource.username=postgres
spring.datasource.password=1234
```

### 4. Build & Run
```
mvn clean install
mvn spring-boot:run
```

## API Usage

### Get Weather by Pincode and Date
- **Endpoint:** `GET /weather?pincode=411014&for_date=2020-10-15`
- **Sample cURL:**
```
curl -X GET "http://localhost:8080/weather?pincode=411014&for_date=2020-10-15"
```
- **Sample Response:**
```
{
  "id": 1,
  "pincode": "411014",
  "date": "2020-10-15",
  "weatherJson": "{...}",
  "lastFetched": "2024-07-08T12:34:56.789"
}
```

## Testing

### Run All Tests
```
mvn test
```

### Test Coverage
- Controller API tests (see `WeatherControllerTest`)
- Add more tests for service/repository as needed

## Swagger UI
Once running, visit:
```
http://localhost:8080/swagger-ui/index.html
```

## Notes
- The API caches geolocation and weather data to minimize external calls.
- For production, do not hardcode secrets in properties files. 