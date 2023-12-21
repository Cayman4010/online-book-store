# <div align="center">Online Bookstore Application</div>


<p align="center">
    <img width="430" src="https://w7.pngwing.com/pngs/202/613/png-transparent-books-school-education-stack-icon-library-pile-college-science-business.png" alt="Bookstore image">
</p>


👋Welcome to the Online Bookstore Application! </br> 
This platform was developed to simplify book management, category organization, and user order handling. My goal is to provide an intuitive interface for book enthusiasts, enabling easy exploration of diverse genres and hassle-free order placement.


The Online Bookstore Application offers a range of functionalities, including:

1. **📘Book Management**: Allows administrators to add, edit, and remove books from the inventory. Users can browse books by category and view detailed information about each book.

2. **📚Category Organization**: Enables the categorization of books into various genres or classifications, aiding users in discovering books based on their preferences.

3. **📦User Order Handling**: Facilitates the ordering process, from cart creation to order placement and status tracking. Users can conveniently manage their orders through their accounts.

4. **🛒Shopping Cart Management**: Provides users with a personalized shopping cart, allowing the addition, removal, and modification of book items before checkout.

5. **👤User Registration and Authentication**: Allows users to create accounts, log in securely, and maintain their profiles, enhancing personalized experiences.

## Technologies Used
- **☕️ JDK 17**: Embracing the functionalities and enhancements introduced in Java 17.
- **🍃 Spring Boot**: Backend development framework.
- **🛡️ Spring Security**: Authentication and authorization.
- **💾 Spring Data JPA**: Data persistence and database operations.
- **🗺️ MapStruct**: Object mapping between DTOs and models.
- **📘 Swagger**: API documentation generation.
- **✅ Jakarta Validation**: Ensuring data integrity with validation annotations.
- **🔄Liquibase**: Database schema version control.
- **🐬 MySQL**: Relational database management system.
- **🌶️ Lombok️**: Reducing boilerplate code in Java classes.
- **🐳 Docker**: Containerization platform for application deployment and management.

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
- JDK 17 or higher ☕️
- Maven 🚀
- Docker 🐳

### Steps to Run
1. Clone the repository 📥
2. Configure `application.properties` with your database settings 🛠️
3. Build the project using Maven: `mvn clean package` 🚀
4. Set up your environment variables in the .env file 🗝️
5. Build the Docker images: `docker-compose build` 🐳
6. Run the Docker containers: `docker-compose up` 💻
7. Access the application at http://localhost:8081 in your browser 🌐

### API Endpoints
- Access Swagger UI (`/swagger-ui.html`) to explore and test available endpoints.
- Utilize the following endpoints:
   - User registration 📝
   - Book and category management 📚
   - Shopping cart operations 🛒
   - Order management 📦

## Additional Implemented Features

### Integration of Security 🔒
Successfully integrated Spring Security for user authentication and authorization, ensuring data protection.

### Advanced DTO Mapping 🗺️
Utilized MapStruct for precise and comprehensive object mapping, managing various scenarios and edge cases effectively.

### Swagger Integration 📋
Implemented Swagger for API documentation, simplifying understanding and interaction with endpoints through a user-friendly interface.

### Exception Handling 🚩
Implemented robust exception handling to provide clear and understandable error messages, ensuring smooth application flow even during unexpected scenarios.

### Query Optimization ⚡
Focused on query optimization to enhance database performance and reduce response time, ensuring efficient data retrieval.

### Pagination 📖
Implemented pagination for improved data handling, allowing the presentation of large data sets in manageable chunks for better user experience.

## Project Summary

The Bookstore Application is dedicated to providing a secure and efficient platform for book management, user engagement, and seamless order processing. I welcome contributions and feedback! 📧

For inquiries or support, feel free to reach out at chernyonkov.oleksandr@gmail.com
