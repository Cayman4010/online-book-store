# <div align="center">Online Bookstore Application</div>


<p align="center">
    <img width="430" src="https://w7.pngwing.com/pngs/202/613/png-transparent-books-school-education-stack-icon-library-pile-college-science-business.png" alt="Bookstore image">
</p>


Welcome to the Online Bookstore Application! </br>
This project aims to provide a seamless platform for managing 📘books, 📚categories, 📦user orders, and 🛒shopping carts.

## Technologies Used
- **🍃 Spring Boot**: Backend development framework.
- **🛡️ Spring Security**: Authentication and authorization.
- **💾 Spring Data JPA**: Data persistence and database operations.
- **🗺️ MapStruct**: Object mapping between DTOs and models.
- **📘 Swagger**: API documentation generation.
- **✅ Jakarta Validation**: Ensuring data integrity with validation annotations.
- **🔄Liquibase**: Database schema version control.
- **🐬 MySQL**: Relational database management system.
- **🌶️ Lombok️**: Reducing boilerplate code in Java classes.

## Functionality Overview

1. **Book Controller** 📘
    - **Endpoints**:
        - `GET /api/books`: Retrieve all books (USER).
        - `GET /api/books/{id}`: Get book by ID (USER).
        - `POST /api/books`: Create a new book (ADMIN).
        - `PUT /api/books/{id}`: Update a book by ID (ADMIN).
        - `DELETE /api/books/{id}`: Delete a book by ID (ADMIN).

2. **Category Controller** 📚
    - **Endpoints**:
        - `GET /api/categories`: Retrieve all categories (USER).
        - `GET /api/categories/{id}`: Get category by ID (USER).
        - `POST /api/categories`: Create a new category (ADMIN).
        - `PUT /api/categories/{id}`: Update a category by ID (ADMIN).
        - `DELETE /api/categories/{id}`: Delete a category by ID (ADMIN).

3. **User Controller** 👤
    - **Endpoints**:
        - `POST /api/users/register`: Register a new user (PUBLIC).
        - `POST /api/users/login`: User login functionality (PUBLIC).

4. **Order Controller** 📦
    - **Endpoints**:
        - `GET /api/orders`: Retrieve all orders (USER).
        - `GET /api/orders/{id}`: Get order details by ID (USER).
        - `POST /api/orders`: Create a new order (USER).
        - `PUT /api/orders/{id}`: Update order status by ID (ADMIN).

5. **Shopping Cart Controller** 🛒
    - **Endpoints**:
        - `GET /api/carts/{id}`: Retrieve shopping cart by user ID (USER).
        - `POST /api/carts/{id}/items`: Add item to the shopping cart (USER).
        - `PUT /api/carts/{id}/items/{itemId}`: Update item quantity in the shopping cart (USER).
        - `DELETE /api/carts/{id}/items/{itemId}`: Remove item from the shopping cart (USER).

## Setup and Usage

### Requirements
- JDK 8 or higher ☕️
- Maven 🚀

### Steps to Run
1. Clone the repository 📥
2. Configure `application.properties` with your database settings 🛠️
3. Run `mvn spring-boot:run` to start the application 💻 

### API Endpoints
- Access Swagger UI (`/swagger-ui.html`) to explore and test available endpoints.
- Utilize the following endpoints:
   - User registration 📝
   - Book and category management 📚
   - Shopping cart operations 🛒

## Challenges Encountered

### Integration of Security
Implementing secure authentication and authorization using Spring Security was a challenging task, ensuring robust protection for user data. 🔒

### DTO Mapping
Employing MapStruct for object mapping required meticulous configuration to handle various scenarios and edge cases effectively. 🗺️

## Project Summary

The Bookstore Application is dedicated to providing a secure and efficient platform for book management, user engagement, and seamless order processing. We welcome contributions and feedback! 📧

For inquiries or support, feel free to reach out at chernyonkov.oleksandr@gmail.com
