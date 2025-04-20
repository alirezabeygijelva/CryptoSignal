
# Financial Market Signal

## Overview
This service is built using Spring Boot with Kotlin (version 1.9.24) and Java 21. The project is managed using Maven (including Maven Wrapper) and is designed to provide a set of APIs for interacting with a financial market application. The service includes OpenAPI documentation and a Swagger UI for easy API exploration.

## Prerequisites
- **Java 21**: Ensure Java 21 is installed and configured on your machine.
- **Maven**: If you are not using Maven Wrapper, make sure Maven is installed.
- **Kotlin**: Kotlin 1.9.24 should be available in your development environment.
- **Docker**: Docker should be installed and running if you plan to use Docker for building and deploying the application.
- **Docker Compose**: Required if using Docker Compose to manage multi-container applications.


## Getting Started

### Config the Project
Please specify and set values to `src/main/resources/application.properties` and `src/main/resources/config` and the relevant configurations including the active profile, properties and environment variables.

### Building the Project
#### Locally
To build the project locally, you can use the following command:
```bash
./mvnw clean install
```


### Running the Application
#### Locally
To run the application locally without Docker:
```bash
./mvnw spring-boot:run
```

#### Using Docker
To build and run the application using Docker, follow these steps:

1. **Build the Docker Image:**
   ```bash
   docker build -t fms:latest .
   ```

2. **Run the Docker Container:**
   ```bash
   docker run -d -p 8080:8080 fms:latest
   ```
   
   This will start the application in a Docker container, accessible at `http://localhost:8080`.

#### Using Docker Compose
If you are using Docker Compose, you can build and run the application along with other services (e.g., a database & redis) using the following commands:

1. **Build and Start the Services:**
   ```bash
   docker-compose up --build
   ```

2. **Stop the Services:**
   ```bash
   docker-compose down
   ```

### Accessing the APIs
Once the application is up and running, you can access the Swagger UI to explore the available APIs:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### OpenAPI Specification
The OpenAPI specification is available in the following location:
- `src/main/resources/api/spec.yaml`

This file can be used with OpenAPI tools or imported into Postman to generate API collections.

### Postman Collection
You can find the Postman collection for testing the APIs in this repository. Simply import the collection into Postman and start interacting with the service.

### Configuration
All application configurations can be adjusted in the `application.properties` or `application.yml` files located under `src/main/resources/`.

## Dependencies
- **Spring Boot 3.3.2**: Core framework for building the service.
- **OpenAPI Codegen**: Used to generate API stubs and models from the OpenAPI specification.
- **Swagger UI**: For interactive API documentation.
- **JOOQ**: jOOQ generates Java code from your database and lets you build type safe SQL queries through its fluent API.
- **Binance Connector Java**: This is a lightweight library that works as a connector to the Binance public API.

## Contributing
If you wish to contribute to this project, please follow the standard fork and pull request process. Ensure all changes are well-tested and documented.

## License
No License found at the moment.