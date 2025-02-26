# 🖥️ Social Chat Application

This is the **frontend** of the Social Chat Application, built using **Next.js, TypeScript, TailwindCSS**, and **Shadcn**. It provides a modern and responsive **real-time chat** interface, user authentication, and seamless integration with the backend.

---

## 🚀 Overview

This frontend application serves as the user interface for the **real-time chat system**, inspired by Facebook Messenger. It enables users to **sign up, log in, send real-time messages**, and **receive notifications asynchronously**.

### 🔑 Key Features:

-   **User Authentication:** OAuth2, username/password, JWT-based session management.
-   **Real-time Messaging:** WebSocket integration for instant updates.
-   **State Management:** Redux Toolkit for global state management.
-   **Efficient Data Fetching:** TanStack Query for optimized API requests.
-   **Appealing Design:** TailwindCSS & Shadcn for a clean and intuitive UI.
-   **File Uploads:** Cloudinary integration for seamless media uploads.

---

## 🛠 Technologies Used

-   **Next.js** - React framework for server-side rendering (SSR) and static site generation (SSG).
-   **TypeScript** - Strongly typed JavaScript for better maintainability.
-   **TailwindCSS** - Utility-first CSS framework for rapid UI development.
-   **Shadcn** - Modern UI components for a beautiful and accessible design.
-   **Redux Toolkit** - State management solution for handling global state.
-   **TanStack Query** - Efficient and performant data fetching.
-   **Axios** - API client for making HTTP requests.

---

## 📂 Project Structure

```
.
├── public/                  # Static assets like images, fonts, etc.
├── src/
|   ├── apis/                # Call request API (e.g., axios services)
│   ├── app/                 # Main application components
│   ├    ├── (auth)/         # Authentication pages
│   ├    ├── (mainlayout)/         # Components for homepage
│   ├    └── (chat)/    # Chat page UI
│   ├── components/          # Reusable components including Shadcn, Custom components
│   ├── assets/              # Static assets like images, fonts, etc.
│   ├── hooks/               # Custom React hooks
│   ├── utils/               # Utility functions
│   ├── lib/                 # Utility libraries (leaflet for maps, react-query for data fetching, redux-toolkit for state management, ...)
|   └── schema/              # Type definitions, validation schemas, etc.
├── .env.local               # Environment variables
├── tailwind.config.js       # TailwindCSS configuration
├── next.config.js           # Next.js configuration
├── package.json             # Project dependencies and scripts
└── tsconfig.json            # TypeScript configuration
```

---

## 🏗️ Setup Instructions

### 1️⃣ Prerequisites

-   **Node.js 18+**
-   **Npm**
-   **Backend API** running on `http://localhost:8080` (or your configured backend URL)

---

### 2️⃣ Install Dependencies

Clone the repository and navigate to the frontend directory:

```sh
git clone <git repository URL>
```

Then, install dependencies:

```sh
npm install
```

---

### 3️⃣ Environment Configuration

Create a `.env.local` file in the `frontend/` directory and add the following environment variables:

````env
    NEXT_PUBLIC_BASE_URL=http://localhost:6000
    ```

Replace values with your actual backend API URL

---

### 4️⃣ Run the Development Server

Start the frontend server:

```sh
npm run dev
````

The application should now be running at **`http://localhost:3000`**.

---

### 5️⃣ Build for Production

To create an optimized production build:

```sh
npm run build
```

To preview the production build:

```sh
npm run start
```

---

### 6️⃣ Run with Docker

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

---

## 🎨 UI & Screenshots

> _(Add screenshots or GIFs showcasing the UI here.)_

---

## 📌 Notes & Future Improvements

-   Add **dark mode** support with TailwindCSS.
-   Improve **WebSocket reconnection** in case of connection loss.
-   Implement **progressive web app (PWA)** features for better mobile experience.
