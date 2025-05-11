Standard Backend Development Flow

    Design & Set Up Database

        Define your entities (e.g., User, Role, Product, etc.).

        Set up relationships (OneToMany, ManyToOne, etc.).

        Create schema using JPA/Hibernate or SQL.

    Create Entity Classes & Repositories

        Java classes mapped to database tables.

        Repositories (using Spring Data JPA) for CRUD operations.

    Develop and Test Endpoints (with Postman)

        Build REST APIs for each major entity (GET, POST, PUT, DELETE).

        Test every endpoint individually using Postman:

            Make sure data saves to DB.

            Validate edge cases and bad input.

            Ensure expected HTTP status codes.

    Implement Business Logic (Services)

        Add any logic beyond basic CRUD.

        Validate inputs, handle rules, call other services.

    Secure the APIs (JWT/Auth)

        Once basic functionality is tested and stable, add authentication/authorization.

        Use JWT, role checks, etc.

    Frontend Integration

        Once backend endpoints are working and secure, integrate with frontend.

        Frontend stores token, calls APIs.

    Enhance with Features (Token Refresh, Error Handling, etc.)

        Add polish (e.g., error handling, token refreshing, pagination).

    Add Thymeleaf templates
