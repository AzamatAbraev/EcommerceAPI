# E-Commerce API

This is a backend API for an e-commerce platform built using **Spring Boot**, **PostgreSQL**, and **JWT Authentication**. The application supports user authentication, product management, shopping cart functionality, and a basic admin interface. Integration with payment gateways (e.g., Stripe) is planned but not yet implemented.

## Features

### User Authentication
- User registration and login with JWT tokens.
- Role-based access control for admin and regular users.

### Product Management
- Admin can add, update, or delete products.
- Products have details like name, description, price, and stock quantity.

### Shopping Cart
- Users can add and remove products from their shopping cart.
- View cart contents with product details and total cost.

### Product Search & Listing
- Users can search for products by name or description.
- Products can be browsed in a paginated format.

### Checkout (Payment Not Yet Integrated)
- Cart items can be reviewed before checkout.
- Placeholder endpoint for initiating payments (integration with Stripe pending).

## Tech Stack
- **Backend:** Spring Boot
- **Database:** PostgreSQL
- **Authentication:** JWT (JSON Web Tokens)
- **API Testing:** Postman / curl / any REST client

## Getting Started

### Prerequisites
- Java 17+
- PostgreSQL
- Maven

### Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/AzamatAbraev/EcommerceAPI
   cd EcommerceAPI
   ```

2. Configure PostgreSQL:
   Create a database named `ecommerce_db` and update your `application.properties` or `application.yml` with your credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## TODO
- Integrate Stripe or other payment gateway.
- Add frontend (Jinja, EJS, or React).
- Add email notifications.

Feel free to fork this project and contribute!

