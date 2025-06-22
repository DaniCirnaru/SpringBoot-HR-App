# HR Management System

A modular HR system built with Spring Boot and Java 17. The system uses a microservices architecture with dedicated services for authentication, user management, and request routing.

## 📦 Project Structure

- **api-gateway/** – Central entry point; routes requests to internal services using Spring Cloud Gateway.
- **auth-service/** – Handles user authentication and issues JWT tokens.
- **user-service/** – Manages users, roles, and profile-related operations.

## 🚀 Getting Started

### Prerequisites
- Java 17
- Maven 3.6+
- MySQL (or another configured database)
- Docker (Kafka container)

### 🛠️ Build & Run

Build all modules:

```bash
mvn clean install
