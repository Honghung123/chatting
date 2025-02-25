# Realtime Chat Application

## Introduction

This is a **Realtime Chat Application** built with **Spring Boot**. It supports real-time messaging, authentication, and media uploads. The application leverages **WebSockets**, **Redis**, **RabbitMQ**, and **Kafka** to ensure efficient real-time communication and scalability.

## Features

- **Real-time messaging** using WebSockets.
- **User authentication & security** with OAuth2 and Spring Security.
- **Message persistence** using PostgreSQL and JPA.
- **Redis caching** for fast data retrieval.
- **RabbitMQ & Kafka** for event-driven communication.
- **Email notifications** using Spring Mail.
- **Media uploads** via Cloudinary.

## Technologies & Dependencies

The application is built with the following dependencies:

- **Spring Boot Starters**:
  - `spring-boot-starter-web` - For building REST APIs.
  - `spring-boot-starter-websocket` - Enables WebSocket-based communication.
  - `spring-boot-starter-security` - Provides authentication and authorization.
  - `spring-boot-starter-oauth2-client` - Supports OAuth2 authentication.
  - `spring-boot-starter-validation` - For input validation.
  - `spring-boot-starter-mail` - Sends email notifications.
  - `spring-boot-starter-data-jpa` - Manages database operations.
  - `spring-boot-starter-data-redis` - Handles caching with Redis.
  - `spring-boot-starter-amqp` - Supports RabbitMQ for messaging.
- **Database & Messaging**:
  - `org.postgresql:postgresql` - PostgreSQL driver.
  - `redis.clients:jedis` - Redis client.
  - `org.springframework.kafka:spring-kafka` - Kafka integration.
- **File & Cloud Storage**:
  - `com.cloudinary:cloudinary-http5` - Uploads media to Cloudinary.
  - `com.cloudinary:cloudinary-taglib` - Cloudinary tag library.
- **Other Utilities**:
  - `com.fasterxml.jackson.datatype:jackson-datatype-jsr310` - JSON serialization for Java 8 Date/Time.
  - `org.projectlombok:lombok` - Reduces boilerplate code.

## Setup & Installation

### Prerequisites

Ensure you have the following installed:

- **Java 17+**
- **Maven**
- **PostgreSQL**
- **Redis**
- **RabbitMQ**
- **Kafka**
- **Cloudinary Account** (for media uploads)

### Installation Steps

1. **Clone the repository**:
   Clone this project from this repo

2. **Configure the application**:
   - Update `application.yml` with your database, Redis, RabbitMQ, Kafka, and Cloudinary credentials.

3. **Build and run the application**:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the application**:
   - API base URL: `http://localhost:8080`
   - WebSocket endpoint: `ws://localhost:8080/chat`
 

## WebSocket Communication

- **Connect to WebSocket**: `ws://localhost:8080/chat`
- **Send Messages** (JSON format):
  ```json
  {
    "sender": "user1",
    "receiver": "user2",
    "message": "Hello, how are you?"
  }
  ```
- **Receive Messages** (JSON response):
  ```json
  {
    "sender": "user1",
    "receiver": "user2",
    "message": "Hello, how are you?",
    "timestamp": "2025-02-25T12:00:00Z"
  }
  ```

## Contributing

If you'd like to contribute, feel free to fork the repository and submit a pull request.
 