# Learning Management System (LMS) Application

## Table of Contents
1. [Overview](#overview)
2. [Tech Stack](#tech-stack)
3. [Dependencies](#dependencies)
4. [Code Structure](#code-structure)
5. [Core Features & User Flow](#core-features--user-flow)
6. [Getting Started](#getting-started)
7. [Running the Application](#running-the-application)
8. [Testing](#testing)
9. [Contributing](#contributing)
10. [License](#license)

---

## Overview
The **LMS Application** is a full-featured backend service for managing courses, teachers, students, and departments. It supports role-based authentication, course creation, and management, allowing institutions to manage learning materials efficiently.

The application exposes RESTful APIs for **students, teachers, and administrators**, and follows clean architecture principles for maintainability and scalability.

---

## Tech Stack
- **Backend Framework:** Spring Boot (4.x)
- **Language:** Java 25
- **Database:** H2 (development), MySQL (production)
- **ORM:** Spring Data JPA / Hibernate
- **Validation:** Jakarta Bean Validation
- **Testing:** JUnit 5, Mockito, AssertJ
- **Build Tool:** Maven
- **API Documentation:** Swagger / OpenAPI (optional)

---

## Dependencies
Key dependencies include:
- `spring-boot-starter-web` – REST API development
- `spring-boot-starter-data-jpa` – ORM with Hibernate
- `spring-boot-starter-validation` – Request validation
- `spring-boot-starter-security` – Authentication & role-based authorization
- `spring-boot-starter-test` – JUnit, Mockito, AssertJ for unit/integration testing
- `h2` – In-memory database for dev/testing
- `mysql-connector-java` – MySQL support for production

All dependencies are managed via Maven (`pom.xml`).

---

## Code Structure
```

src/main/java
├── com.interswitch.lms
│   ├── controller      # REST Controllers (endpoints)
│   ├── service         # Service layer (business logic)
│   ├── repository      # Spring Data JPA repositories
│   ├── entity          # JPA entities (Student, Teacher, Course, Department)
│   ├── dto             # Request/Response DTOs
│   ├── mapper          # Mappers (Entity <-> DTO)
│   ├── exception       # Custom exceptions & handlers
│   └── config          # Spring and security configurations
└── resources
├── application.properties  # Configurations
└── data.sql                # Optional sample data

````

---

## Core Features & User Flow

### 1. **Authentication & Authorization**
- **Endpoint:** `/api/auth/login`  
- **Flow:** User logs in → JWT token generated → Token used to access protected endpoints.
- Roles: `ADMIN`, `TEACHER`, `STUDENT`.

### 2. **Course Management**
- **Create Course:** `POST /api/courses`
    - Admin or Teacher creates a course.
    - Request: `CreateCourseRequestDTO` → Response: `CourseResponseDTO`.
    - Required fields: `title`, `description`, `price`, `teacherId`, `departmentId`, `isPaid`.
- **Get Course by ID:** `GET /api/courses/{id}`
    - Fetches course details including department name and teacher full name.
- **Get All Courses:** `GET /api/courses`
    - Supports pagination and filtering by department.

### 3. **Teacher & Department Management**
- **Teacher CRUD:** `/api/teachers`  
- **Department CRUD:** `/api/departments`  
- Courses reference `teacherId` and `departmentId` for relational mapping.

### 4. **Student Enrollment**
- **Enroll in Course:** `POST /api/students/{studentId}/enroll/{courseId}`
- Students can enroll in free or paid courses.
- Payment logic can be integrated later.

---

## Getting Started

### Prerequisites
- Java 25 SDK
- Maven 4.x
- MySQL (or H2 for testing)
- IDE: IntelliJ IDEA / VSCode

### Setup
1. Clone the repository
```bash
git clone https://github.com/your-username/lms-app.git
cd lms-app
````

2. Configure the database in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lms_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

3. Build the project

```bash
mvn clean install
```

---

## Running the Application

```bash
mvn spring-boot:run
```

* The application will run at `http://localhost:8080`
* Swagger UI (if integrated): `http://localhost:8080/swagger-ui.html`

---

## Testing

### Run Unit Tests

```bash
mvn test
```

### Run All Tests in IntelliJ

* Navigate to `src/test/java`

* Right-click `CourseServiceTest` (or root test folder)

* Select **Run 'All Tests'**

* Tests cover:

  * Course creation, retrieval
  * Teacher & Department CRUD
  * Student enrollment
  * Authorization & validation

---

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/xyz`)
3. Commit changes (`git commit -m 'Add xyz feature'`)
4. Push to branch (`git push origin feature/xyz`)
5. Open a Pull Request

## License

This project is licensed under the **MIT License**. See `LICENSE` file for details.

```
