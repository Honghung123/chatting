# Social Chat Application

A **real-time social chat application** inspired by Facebook, built with **Spring Boot** (backend) and **Next.js** (frontend). The application provides real-time messaging, user authentication, notifications, file uploads, and more.

---

## 🚀 Overview

This project enables users to **chat in real-time**, **manage authentication**, and **receive notifications asynchronously** using **Kafka**. It leverages **Spring Security** for authentication, **Redis for caching**, and **Cloudinary for file storage**. The frontend is built using **Next.js, TailwindCSS, and Shadcn**, ensuring a modern and responsive UI.

### 🔑 Key Features:

-   **User Authentication:** OAuth2, JWT-based session management, role-based access control.
-   **Real-time Messaging:** Implemented using WebSocket and Redis for fast data access.
-   **Asynchronous Processing:** Kafka handles notifications, email sending, and online status updates.
-   **Email Services:** JavaMailSender for sending activation emails and notifications.
-   **File Uploads:** Cloudinary integration for storing images and files.
-   **Database & Caching:** PostgreSQL for structured data storage and Redis for caching.
-   **Containerization:** Docker support for easy deployment.

---

## 🛠 Technologies Used

### Backend:

-   **Spring Boot (Java)**
-   **Spring Security (OAuth2, JWT)**
-   **PostgreSQL**
-   **Redis**
-   **Kafka, RabbitMQ**
-   **WebSocket**
-   **JavaMailSender**
-   **Cloudinary (File Storage)**
-   **Docker (Containerization)**

### Frontend:

-   **Next.js (React)**
-   **TypeScript**
-   **TailwindCSS & Shadcn (UI Framework)**
-   **Redux Toolkit (State Management)**
-   **TanStack Query (Data Fetching)**
-   **Axios (API Requests)**

---

## 📂 Project Structure

```
social-chat-app/
│── backend/                # Spring Boot backend
│   ├── src/main/java/com/example/chat/
│   │   ├── config/         # Security, JWT, WebSocket configurations
│   │   ├── controller/     # REST APIs
│   │   ├── service/        # Business logic
│   │   ├── model/          # Database entities
│   │   ├── repository/     # Database access
│   │   ├── utils/          # Utility classes
│   ├── resources/          # Configuration files (application.yml)
│   ├── pom.xml             # Maven dependencies
│── frontend/               # Next.js frontend
│   ├── components/         # UI components
│   ├── pages/              # Next.js pages
│   ├── store/              # Redux store
│   ├── services/           # API calls (Axios)
│   ├── styles/             # TailwindCSS configuration
│   ├── package.json        # Frontend dependencies
│── docker-compose.yml      # Docker configuration
│── README.md               # Documentation
```

---

## 🏗️ Setup Instructions

### 1️⃣ Prerequisites

-   **Java 17+**
-   **Node.js 18+**
-   **Docker**
-   **PostgreSQL & Redis**
-   **Kafka & Zookeeper**

---

### 2️⃣ Backend Setup

#### Clone the repository:

```sh
git clone https://github.com/your-repo/social-chat-app.git
cd social-chat-app/backend
```

#### Configure `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/social_chat_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

# Redis
spring.redis.host=localhost
spring.redis.port=6379

# Email Service
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_password

# Cloudinary API
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

#### Build and run the backend:

```sh
mvn clean install
mvn spring-boot:run
```

---

### 3️⃣ Frontend Setup

#### Navigate to the frontend directory:

```sh
cd ../frontend
```

#### Install dependencies:

```sh
npm install
```

#### Start the frontend server:

```sh
npm run dev
```

---

### 4️⃣ Run with Docker

To run the backend and required services using Docker:

```sh
docker-compose up --build
```

Ensure `docker-compose.yml` is properly configured for PostgreSQL, Redis, and Kafka.

---

## 🤝 Contributing

1. Fork the repository.
2. Create a new feature branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -m "Add new feature"`).
4. Push to the branch (`git push origin feature-name`).
5. Open a Pull Request.
