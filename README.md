# Parking System
This is a simple parking system that allows cars/vehicle to register and unregister a parking session on Netherlands Street.
The system should be able to handle the following scenarios:
- A car can register a parking session.
- A car can unregister a parking session.
- A Car is not allowed to register a parking session if it is already registered and session is active.
- A car is not allowed to unregister a parking session if it is not registered or session is not active.
- A car will be billed per minute for the parking session based on the street parking rate.
- A monitoring service will be able to bulk register the cars and penalize the cars that have not registered the parking session.
- A scheduled report is generated for the monitoring service to penalize the cars that have not registered the parking session.

Three APIs are provided to handle the above scenarios

## Technology Stack
project is build using the following Language/Frameworks:
- Java 21
- Spring Boot
- maven
- Junit 5, Mockito
- H2 Database
- Hibernate
- Swagger OpenAPI
- Caffeine in memory Cache
- Lombok

## How to run the project
After cloning this repository run the following maven command from the root directory to get started

mvn -U clean package spring-boot:run

The application can be accessed on port 8080

## API Documentation
This project uses Swagger OpenAPI specs for dynamic documentation

http://localhost:8080/swagger-ui/index.html

## Further Enhancements
- Containerization
- CI/CD DevOps Pipeline
- Adding code quality plugins like Sonar, Fortify/Checkmarx, NexusIq 


