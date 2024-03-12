# Parking System
The Netherlands parking system backend REST APIs are designed and implemented to allow
cars/vehicle to register/unregister a parking session, monitor the registrations and generate the report of unregistered parking sessions.

## Table of Contents
- [Features](#features)
- [Technology Stack](#Technology Stack)
- [Setup](#setup)
- [Endpoints](#endpoints)

## features
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
- Java              21
- Spring Boot       3.2.3
- maven             3.9.6
- Junit 5,Mockito   5.0
- H2 Database       2.2.224
- Hibernate         6.4.1
- Swagger OpenAPI   2.2.0
- Lombok            1.18.30

## Setup
**Clone Repository:** Clone the repo using 
git clone https://github.com/abduladoni/parkingsystem.git

Change the following properties in application.properties file according to your environment
- report.location=C:\\personal\\projects\\reports

**Run command:**
mvn -U clean package spring-boot:run

The application can be accessed on port 8080

## Endpoints
This project uses Swagger OpenAPI specs for dynamic documentation

http://localhost:8080/swagger-ui/index.html

# Database Schema

## 1. ParkingVehicle

- **Table Name:** parking_vehicle
- **Description:** This table stores records of parking sessions.

| Column Name          | Data Type     | Description                              |
|----------------------|---------------|------------------------------------------|
| id                   | BIGINT        | Primary key for the parking record.      |
| license_plate_number | VARCHAR(255)  | License plate number of the vehicle.     |
| street_name          | VARCHAR(255)  | Street Name                              |
| start_time           | TIMESTAMP     | Start time of the parking session.       |
| end_time             | TIMESTAMP     | End time of the parking session.         |
| status               | VARCHAR(255)  | Status of parking session. Active, Ended |
| price                | NUMERIC(38,2) | parking fee when the session is ended    |
| last_updated_ts      | TIMESTAMP     | Last updated ts for the record.          |

## 2. PenaltyNotification

- **Table Name:** penalty_notification
- **Description:** This table stores records of unregistered observations to send penalty notifications .

| Column Name          | Data Type     | Description                                      |
|----------------------|---------------|--------------------------------------------------|
| id                   | BIGINT        | Primary key for the penalty notification record. |
| license_plate_number | VARCHAR(255)  | License plate number of the vehicle.             |
| street_name          | VARCHAR(255)  | Street Name                                      |
| date_time            | TIMESTAMP     | Observation date time.                           |
| penalty_sent         | BOOLEAN       | True/False indicates notification is send.       |
| last_updated_ts      | TIMESTAMP     | Last updated ts for the record.                |

## 3. ParkingTariffMetaData

- **Table Name:** parking_tariff_meta_data
- **Description:** This table stores meta data about street and tariff in cents.

| Column Name          | Data Type    | Description                                 |
|----------------------|--------------|---------------------------------------------|
| id                   | BIGINT       | Primary key for the parking tariff  record. |
| street_name          | VARCHAR(255) | Street Name                                 |
| tariff_in_cents      | INTEGER      | Tariff in cents                             |

# Further Enhancements
- Containerization
- CI/CD DevOps Pipeline
- Adding code quality plugins like Sonar, Fortify/Checkmarx, NexusIq 


