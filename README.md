# Social Chat Application

## 🚀 Overview

A **real-time social chat application** inspired by Facebook, built with **Spring Boot** (backend) and **Next.js** (frontend). The application provides real-time messaging, user authentication, notifications, file uploads, and more.
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
│   ├── src/main/java/com/honghung/chatapp/
│   │   ├── component/      # Custom components
│   │   ├── config/         # Security, JWT, WebSocket configurations
│   │   ├── constant/       # Constants for configuration and global variables
│   │   ├── controller/     # REST APIs
│   │   ├── dto/     # Data Transfer Objects for requests and responses
│   │   ├── service/        # Business logic
│   │   ├── entity/         # Database entities
│   │   ├── model/          # DTO for transfering among internal services
│   │   ├── repository/     # Database access
│   │   ├── utils/          # Utility classes
│   ├── resources/          # Configuration files (application.yml)
│   ├── .env             # Environment variables for docker compose
│   ├── pom.xml             # Maven dependencies
│   │
│── frontend/               # Next.js frontend
│   │
│   ├── public/                  # Static assets like images, fonts, etc.
│   ├── src/
│   |   ├── apis/                # Call request API (e.g., axios services)
│   │   ├── app/                 # Main application components
│   │   ├    ├── (auth)/         # Authentication pages
│   │   ├    ├── (mainlayout)/   # Components for homepage
│   │   ├    └── (chat)/         # Chat page UI
│   │   ├── components/          # Reusable components including Shadcn, Custom components
│   │   ├── assets/              # Static assets like images, fonts, etc.
│   │   ├── hooks/               # Custom React hooks
│   │   ├── utils/               # Utility functions
│   │   ├── lib/                 # Utility libraries (leaflet for maps, react-query for data fetching, redux-toolkit for state management, ...)
│   |   └── schema/              # Type definitions, validation schemas, etc.
│   ├── .env.local               # Environment variables
│   ├── tailwind.config.js       # TailwindCSS configuration
│   ├── next.config.js           # Next.js configuration
│   ├── package.json             # Project dependencies and scripts
│   └── tsconfig.json            # TypeScript configuration
│── README.md                    # Documentation
```

---

## 🏗️ Setup Instructions

### 1️⃣ Prerequisites

-   **Java 17+**
-   **Node.js 18+**
-   **Docker**
-   **PostgreSQL & Redis**
-   **Kafka & Zookeeper**
-   **Cloudinary account**

---

### 2️⃣ Backend Setup

#### 1. Clone the repository:

```sh
git clone <THIS REPOSITORY URL>
```

#### 2. Configure config in `.env`:

You need to override some configs as follows:

```properties
# Postgres
DB_USER=YOUR_POSTGRES_USER
DB_PASSWORD=YOUR_POSTGRES_PASSWORD
DB_NAME=YOUR_POSTGRES_DB_NAME
DB_DRIVER_CLASS_NAME=org.postgresql.Driver

# Rabbit MQ
RABBITMQ_DEFAULT_USER=YOUR_RABBITMQ_USER
RABBITMQ_DEFAULT_PASS=YOUR_RABBITMQ_PASSWORD

# Mail Sender
MAIL_FROM=YOUR_EMAIL
MAIL_USERNAME=YOUR_EMAIL or YOUR_USERNAME
MAIL_PASSWORD=YOUR_PASSWORD

# Cloudinary
CLOUDINARY_API_KEY=YOUR_CLOUDINARY_API_KEY
CLOUDINARY_URL=YOUR_CLOUDINARY_URL
```

-> Others can be omitted

#### 3. Build and run the backend:

You can run the backend server using Maven:

```sh
mvn clean install
mvn spring-boot:run
```

Or run with Docker compose:

```sh
docker-compose up -d
```

---

### 3️⃣ Frontend Setup

#### 1. Navigate to the frontend directory:

```sh
cd ../frontend
```

#### 2. Install dependencies:

```sh
npm install
```

#### 3. Environment Configuration

Create a `.env.local` file in the `frontend/` directory and add the following environment variables:

```env
NEXT_PUBLIC_BASE_URL=http://localhost:6000
```

Replace values with your actual backend API URL

#### 4. Run the Development Server

Start the frontend server:

```sh
npm run dev
```

The application should now be running at **`http://localhost:3000`**.

#### 5. Build for Production

To create an optimized production build:

```sh
npm run build
```

To preview the production build:

```sh
npm run start
```

#### 6. Run with Docker

You can also containerize the frontend using Docker. First, create a `Dockerfile`:

```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package.json package.lock ./
RUN npm install
COPY . .
RUN npm run build
CMD ["npm", "run", "start"]
EXPOSE 3000
```

Then, build and run the Docker container:

```sh
docker build -t social-chat-frontend .
docker run -p 3000:3000 social-chat-frontend
```
 
