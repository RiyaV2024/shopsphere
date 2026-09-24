# ShopSphere

A full-featured e-commerce backend REST API built with Spring Boot, featuring JWT authentication, role-based access control, product management, cart, order processing, and simulated payments.

## Overview

ShopSphere replicates the core backend of a typical e-commerce platform (like Amazon/Flipkart) — users can register, browse products, manage a cart, place orders, and complete payments. Built as a hands-on project to apply Spring Boot, Spring Security, and JPA concepts in a realistic, end-to-end system.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.1.1
- **Security:** Spring Security + JWT (jjwt 0.11.5)
- **Persistence:** Spring Data JPA + Hibernate
- **Database:** MySQL
- **Build Tool:** Maven
- **Utilities:** Lombok, BCrypt (password hashing)
- **Testing:** Postman

## Architecture

The project follows a standard layered architecture:
Controller -> Service -> Repository -> Database
(REST APIs) (business (JpaRepository (MySQL)
logic) interfaces)


Each feature module (Auth, Product, Cart, Order, Payment) follows this same pattern for consistency.

## Features

### 1. Authentication (JWT)
- User registration with BCrypt password hashing
- Login returns a JWT token (stateless authentication)
- Custom `JwtAuthFilter` validates tokens on every request
- Role-based claims embedded in the token (`USER` / `ADMIN`)

### 2. Product Management
- Full CRUD operations
- Public read access (`GET`), admin-only write access (`POST` / `PUT` / `DELETE`)
- Enforced via `@PreAuthorize("hasRole('ADMIN')")`

### 3. Cart
- One cart per user (`@OneToOne`)
- Add / remove / update items, with live product pricing
- `@JsonIgnore` used to prevent circular-reference JSON serialization errors between `Cart` and `CartItem`

### 4. Orders (Checkout)
- Converts a user's cart into a permanent order
- **Price snapshotting:** unlike the cart (which reflects live prices), each `OrderItem` stores the price at the time of purchase, so historical orders remain accurate even if product prices change later
- Order status lifecycle: `PENDING -> CONFIRMED -> DELIVERED / CANCELLED`
- Full order history per user

### 5. Payments (Simulated)
- Processes payment against an order and marks it `SUCCESS`
- Updates the linked order's status to `CONFIRMED`
- Structured to be swapped for a real gateway (e.g., Razorpay) with minimal changes

### 6. Security & Data Protection
- **DTOs** (`UserResponseDTO`, `LoginResponseDTO`) ensure sensitive fields like `password` are never exposed in API responses
- **Global exception handling** via `@RestControllerAdvice` returns clean, structured JSON errors instead of generic/empty responses
- **Role-based route protection** using Spring Security + method-level `@PreAuthorize`

## Entity Relationships

| Relationship | Type | Reason |
|---|---|---|
| User - Cart | `@OneToOne` | A user has exactly one cart |
| User - Order | `@OneToMany` | A user can place multiple orders over time |
| Cart - CartItem | `@OneToMany` | A cart holds multiple line items |
| Order - OrderItem | `@OneToMany` | An order holds multiple line items |
| CartItem/OrderItem - Product | `@ManyToOne` | Many line items can reference the same product |
| Order - Payment | `@OneToOne` | Each order has exactly one payment record |

## API Endpoints

### Auth
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |

### Products
| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/products` | Public |
| GET | `/api/products/{id}` | Public |
| POST | `/api/products` | Admin only |
| PUT | `/api/products/{id}` | Admin only |
| DELETE | `/api/products/{id}` | Admin only |

### Cart
| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/cart` | Authenticated |
| POST | `/api/cart/add?productId={id}&quantity={n}` | Authenticated |
| PUT | `/api/cart/update?productId={id}&quantity={n}` | Authenticated |
| DELETE | `/api/cart/remove/{productId}` | Authenticated |

### Orders
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/orders/place` | Authenticated |
| GET | `/api/orders/history` | Authenticated |

### Payments
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/payments/pay/{orderId}` | Authenticated |

> All authenticated endpoints require an `Authorization: Bearer <token>` header, obtained from the login response.

## Setup Instructions

1. **Clone the repository**
```bash
   git clone <repo-url>
   cd shopsphere
```

2. **Create the MySQL database**
```sql
   CREATE DATABASE shopsphere_db;
```

3. **Configure `application.properties`**
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/shopsphere_db
   spring.datasource.username=root
   spring.datasource.password=<your_mysql_password>
   spring.jpa.hibernate.ddl-auto=update
```

4. **Run the application**
```bash
   mvn spring-boot:run
```
The server starts on `http://localhost:8080`.

5. **Test with Postman**
    - Register a user via `/api/auth/register`
    - Log in via `/api/auth/login` to get a JWT token
    - Use the token in the `Authorization` header for all other requests

## Known Limitations / Future Improvements

- JWT signing key is currently generated at runtime, should be moved to an environment variable for token persistence across restarts
- Payment processing is simulated; a real gateway (Razorpay/Stripe) integration would replace `PaymentService`
- No pagination/filtering yet on product listings
- No input validation (`@NotBlank`, `@Positive`, etc.) on request bodies yet

## Author

Built by Riya Verma - B.Tech CSE, as a portfolio project for backend developer roles.