# 🛒 Buyzen - E-Commerce Backend API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org/)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-green.svg)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-JJWT%200.13.0-blueviolet.svg)](https://github.com/jwtk/jjwt)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

**Buyzen** is a robust, production-ready RESTful E-Commerce Backend built using **Spring Boot 3.5**, **Java 21**, and **PostgreSQL**. It delivers complete e-commerce lifecycle features including JWT & Google OAuth2 authentication, role-based access control, dynamic product filtering and pagination, persistent shopping cart management, transactional order processing, automated email notifications, and admin analytics.

---

## 📑 Table of Contents

- [Features](#-features)
- [Architecture & Tech Stack](#-architecture--tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
  - [1. Clone Repository](#1-clone-repository)
  - [2. PostgreSQL Setup](#2-postgresql-setup)
  - [3. Configure Environment Variables](#3-configure-environment-variables)
  - [4. Build & Run Application](#4-build--run-application)
- [Default Seed Data](#-default-seed-data)
- [API Documentation](#-api-documentation)
  - [Authentication & User Endpoints](#authentication--user-endpoints)
  - [Product Catalog Endpoints](#product-catalog-endpoints)
  - [Cart Endpoints](#cart-endpoints)
  - [Order & Checkout Endpoints](#order--checkout-endpoints)
  - [Admin & Analytics Endpoints](#admin--analytics-endpoints)
- [Security & Authentication Flow](#-security--authentication-flow)
- [Email Service](#-email-service)
- [Contributing & License](#-contributing--license)

---

## ✨ Features

- **🔐 Dual Authentication & Security:**
  - Standard email/password registration and login with BCrypt password hashing.
  - Stateless authentication via **JWT (JSON Web Tokens)**.
  - **Google OAuth2 Single Sign-On (SSO)** with automatic user creation/account linking.
  - Role-Based Access Control (`ROLE_USER`, `ROLE_ADMIN`).
- **🛍️ Product Catalog & Navigation:**
  - Paginated product browsing.
  - Featured products showcase.
  - Case-insensitive keyword search.
  - Category-based product filtering via URL slugs.
- **🛒 Shopping Cart System:**
  - Per-user persistent shopping cart.
  - Dynamic quantity increments, decrements, and automatic zero-quantity removal.
  - Instant subtotal and grand total calculations.
  - Bulk/batch item deletion.
- **📦 Transactional Checkout & Orders:**
  - Multi-item checkout in an atomic transaction.
  - Real-time inventory & stock verification.
  - Order status tracking (`PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`).
  - Order history retrieval per user.
- **📊 Admin Analytics & Management:**
  - Administrative user creation.
  - View all registered users and delete user accounts.
  - Aggregated purchase statistics (total items ordered per user).
  - Pre-seeded default admin account (`AdminSeeder`).
- **✉️ Automated Email Notifications:**
  - Asynchronous HTML welcome email sent via SMTP (Google Mail) upon successful registration.

---

## 🛠 Architecture & Tech Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Java 21 (LTS) |
| **Framework** | Spring Boot 3.5.11 |
| **Database** | PostgreSQL |
| **ORM / Data Access** | Spring Data JPA / Hibernate |
| **Security** | Spring Security 6, JJWT 0.13.0 |
| **OAuth2** | Spring Security OAuth2 Client (Google Provider) |
| **Mailing** | Spring Boot Starter Mail (`JavaMailSender`) |
| **Validation** | Spring Boot Starter Validation (Jakarta Validation) |
| **Utilities** | Project Lombok |
| **Build Tool** | Apache Maven 3.9+ |

---

## 📂 Project Structure

```text
Buyzen/
├── src/
│   ├── main/
│   │   ├── java/com/example/Nap/Buyzen/
│   │   │   ├── config/               # Security, CORS & Seeder Configurations
│   │   │   │   ├── AdminSeeder.java
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── WebSecurityConfig.java
│   │   │   ├── controller/           # REST API Controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── CartController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   ├── entities/             # JPA Database Entities
│   │   │   │   ├── Cart.java
│   │   │   │   ├── CartItems.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Product.java
│   │   │   │   └── User.java
│   │   │   ├── enums/                # Enums (Role, OrderStatus, AuthProviderType)
│   │   │   ├── OAuth2/               # OAuth2 Provider Adapters
│   │   │   ├── repository/           # Spring Data JPA Repositories
│   │   │   ├── security/             # JWT Filter, CustomUserDetails, AuthUtils
│   │   │   ├── service/              # Core Business Logic Services
│   │   │   └── BuyzenApplication.java# Main Application Class
│   │   └── resources/
│   │       ├── application.properties# Database & JWT Config
│   │       └── application.yml       # OAuth2 & SMTP Mail Config
│   └── test/                         # Unit & Integration Tests
├── pom.xml                           # Maven Dependencies & Plugins
└── README.md
```

---

## 📋 Prerequisites

Before running the application, make sure you have the following installed:

- **JDK 21** or higher: [Download OpenJDK 21](https://adoptium.net/)
- **PostgreSQL 15+**: [Download PostgreSQL](https://www.postgresql.org/download/)
- **Maven 3.9+** (or use the included `mvnw` wrapper)
- A **Google Cloud Console** project (optional, for Google OAuth2 login)
- A **Gmail App Password** (optional, for SMTP email delivery)

---

## 🚀 Getting Started

### 1. Clone Repository

```bash
git clone https://github.com/your-username/Buyzen.git
cd Buyzen/Buyzen
```

### 2. PostgreSQL Setup

Create a PostgreSQL database named `buyzen`:

```sql
CREATE DATABASE buyzen;
```

### 3. Configure Environment Variables

Update `src/main/resources/application.properties` and `src/main/resources/application.yml` or set environment variables:

#### `application.properties`
```properties
spring.application.name=Buyzen

# PostgreSQL Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/buyzen
spring.datasource.username=postgres
spring.datasource.password=your_postgres_password

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Secret (Base64-encoded 256-bit key)
jwt.secretKey=RK1cn2Uthl7AM7+JKFM/bfkjqXmEslRm2Kwhte/v1k4=
```

#### Environment Variables for `application.yml`
Set the following environment variables on your system or IDE:

```bash
# Google OAuth2 Credentials
export GOOGLE_CLIENT_ID="your-google-client-id"
export GOOGLE_CLIENT_SECRET="your-google-client-secret"

# Gmail SMTP Mail Credentials
export MAIL_USERNAME="your-email@gmail.com"
export MAIL_PASSWORD="your-gmail-app-password"
```

*(On Windows PowerShell, use `$env:GOOGLE_CLIENT_ID="your-value"`)*

---

### 4. Build & Run Application

#### Using Maven Wrapper:
**Linux / macOS:**
```bash
./mvnw clean spring-boot:run
```

**Windows (PowerShell / Command Prompt):**
```powershell
.\mvnw.cmd clean spring-boot:run
```

The server will start on port **`8080`** by default (`http://localhost:8080`).

---

## 👤 Default Seed Data

When the application boots for the first time, `AdminSeeder` automatically creates an admin account if one does not exist:

- **Email:** `Admin362@gmail.com`
- **Password:** `admin128270`
- **Role:** `ADMIN`
- **Provider:** `LOCAL`

---

## 📡 API Documentation

### Base URL: `http://localhost:8080`

### Authentication & User Endpoints

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/signup` | Public | Register a new user (receives welcome email) |
| `POST` | `/login` | Public | Login with email & password, returns JWT |
| `GET` | `/oauth2/authorization/google` | Public | Initiate Google OAuth2 login redirect |
| `GET` | `/user/profile` | `USER`, `ADMIN` | Fetch authenticated user profile |
| `PATCH` | `/user/password` | `USER`, `ADMIN` | Update account password |

#### Example: User Signup (`POST /signup`)
```json
{
  "name": "John Doe",
  "email": "johndoe@example.com",
  "password": "SecurePassword123"
}
```

#### Example: User Login (`POST /login`)
```json
{
  "email": "johndoe@example.com",
  "password": "SecurePassword123"
}
```
**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 2
}
```

---

### Product Catalog Endpoints

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/products` | Public | Get featured products or search by `keyword` with pagination (`pageNum`, `pageSize`) |
| `GET` | `/products/category/{slug}` | Public | Get products under a category slug with pagination |
| `GET` | `/products/{id}` | Public | Get single product details by product ID |

#### Query Parameters for `/products`:
- `keyword` *(optional)*: Search query string. If omitted, returns featured products.
- `pageNum` *(optional, default: `1`)*: Page number.
- `pageSize` *(optional, default: `20`)*: Number of items per page.

---

### Cart Endpoints
> ⚠️ **Requires `Authorization: Bearer <JWT>` header**

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/cart/addtocart` | Authenticated | Add item to cart or increment/decrement quantity |
| `GET` | `/cart/view_cart` | Authenticated | View current user's cart and item subtotals |
| `DELETE` | `/cart/delete_cartItem` | Authenticated | Remove products from cart by ID list |

#### Example: Add to Cart (`POST /cart/addtocart`)
```json
{
  "id": 5,
  "quantity": 2
}
```

#### Example: Batch Delete Items (`DELETE /cart/delete_cartItem`)
```json
[1, 5, 8]
```

---

### Order & Checkout Endpoints
> ⚠️ **Requires `Authorization: Bearer <JWT>` header**

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/order/checkout` | Authenticated | Create a new order with stock validation |
| `GET` | `/order/view_order` | Authenticated | View all past orders of the current user |

#### Example: Checkout (`POST /order/checkout`)
```json
[
  {
    "productId": 2,
    "quantity": 1
  },
  {
    "productId": 4,
    "quantity": 3
  }
]
```

---

### Admin & Analytics Endpoints
> ⚠️ **Requires `Authorization: Bearer <JWT>` with role `ADMIN`**

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/admin/create_admin` | `ADMIN` | Register a new user with `ADMIN` privileges |
| `GET` | `/admin/view_users` | `ADMIN` | View list of all registered users |
| `DELETE` | `/admin/delete_user/{id}` | `ADMIN` | Delete a specific user by ID |
| `GET` | `/admin/user_purchase` | `ADMIN` | Retrieve aggregated purchase report per user |

#### Example: Admin Analytics Output (`GET /admin/user_purchase`)
```json
[
  {
    "name": "Jane Smith",
    "email": "jane@example.com",
    "totalPurchased": 14
  },
  {
    "name": "John Doe",
    "email": "johndoe@example.com",
    "totalPurchased": 5
  }
]
```

---

## 🔒 Security & Authentication Flow

1. **Local Authentication:**
   - Client sends credentials to `/login`.
   - `AuthenticationManager` verifies credentials.
   - Server responds with a signed JWT.
   - Client includes JWT in subsequent requests:
     ```http
     Authorization: Bearer <JWT_TOKEN>
     ```
2. **Google OAuth2 Authentication:**
   - Client directs user to `/oauth2/authorization/google`.
   - After user approval, Google redirects back to the backend.
   - `OAuth2SuccessHandler` captures attributes, finds or creates the user in the database, and issues a JWT token.
   - User is redirected to frontend callback URL:
     ```text
     http://localhost:5173/oauth2/success?token=<JWT_TOKEN>
     ```

---

## 📧 Email Service

The application integrates with **Jakarta Mail** and **Spring Mail** to send formatted HTML emails:
- Triggered on new user registration (`/signup` and first-time Google OAuth2 login).
- Utilizes Google's SMTP server (`smtp.gmail.com:587`) with TLS encryption.

---

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 License

This project is licensed under the MIT License.
