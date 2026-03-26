# 1️⃣ **System Overview**

The LMS will be a **web-based platform** for managing courses, students, teachers, and administrative operations.

**Goals:**

* Allow **students** to enroll in courses and view their progress.
* Allow **teachers** to create and manage courses, assignments, and grades.
* Allow **admins** to manage users, departments, and system settings.
* Support **role-based access control (RBAC)** via JWT authentication.
* Modular, scalable, and maintainable backend structure.

---

# 2️⃣ **Code / Project Structure (Backend)**

Following **Spring Boot best practices**:

```
com.lms
│
├── config                 # Security, CORS, Interceptors
│   ├── CorsConfig
│   ├── SecurityConfig
│   └── WebConfig
│
├── controller             # REST controllers
│   ├── AuthController
│   ├── StudentController
│   ├── TeacherController
│   ├── DepartmentController
│   └── CourseController
│
├── dto                    # Data Transfer Objects for API requests/responses
│   ├── AuthDTO
│   ├── StudentDTO
│   ├── TeacherDTO
│   └── HealthResponseDTO
│
├── filter                 # JWT filter, logging filter, etc.
│   └── JwtFilter
│
├── interceptor            # RBAC interceptor
│   └── RBAInterceptor
│
├── model/entity           # JPA Entities
│   ├── Auth
│   ├── Student
│   ├── Teacher
│   ├── Department
│   ├── Course
│   ├── Enrollment
│   └── Assignment
│
├── repository             # Spring Data JPA repositories
│   ├── AuthRepository
│   ├── StudentRepository
│   └── CourseRepository
│
├── security               # JWT utilities, password encoder
│   └── JwtUtil
│
├── service                # Business logic
│   ├── AuthService
│   ├── StudentService
│   ├── TeacherService
│   └── CourseService
│
├── exception              # Custom exceptions and handlers
│   └── GlobalExceptionHandler
└── LmsApplication.java    # Main Spring Boot class
```

---

# 3️⃣ **Core Entities and Relationships**

| Entity            | Description                                      | Key Relationships                                            |
| ----------------- | ------------------------------------------------ | ------------------------------------------------------------ |
| **Auth**          | User credentials                                 | 1-to-1 with Student/Teacher                                  |
| **Student**       | Student profile                                  | Belongs to 1 Department; Can enroll in many Courses          |
| **Teacher**       | Teacher profile                                  | Belongs to 1 Department; Can create many Courses             |
| **Department**    | Academic department                              | 1-to-many with Students and Teachers                         |
| **Course**        | Course info                                      | Taught by Teacher; Many-to-many with Students via Enrollment |
| **Enrollment**    | Tracks which student is enrolled in which course | Many-to-one Student & Course                                 |
| **Assignment**    | Assignments for courses                          | Belongs to Course; Can have submission tracking              |
| **Grade / Score** | Stores grades for assignments or courses         | Linked to Enrollment or Assignment                           |

> **Notes:**
>
> * **Department → Students & Teachers** allows grouping by department.
> * **Enrollment** table avoids many-to-many mapping directly between Student and Course.

---

# 4️⃣ **Core Features (MVP)**

### 4.1 Authentication & Authorization

* Register/Login (JWT-based)
* Role-based access (`STUDENT`, `TEACHER`, `ADMIN`)
* Password hashing & secure storage
* Logout / token expiry

### 4.2 Student Features

* View profile & update information
* Enroll in courses
* View enrolled courses and progress
* Submit assignments

### 4.3 Teacher Features

* Create/update/delete courses
* Add assignments to courses
* Grade student assignments
* View students in courses

### 4.4 Admin Features

* Manage users (CRUD for Student/Teacher)
* Manage Departments
* Assign teachers to courses
* View system health (e.g., health endpoints, logs)

### 4.5 Course Management

* Course creation with department association
* Track enrolled students
* Track assignments and grades

### 4.6 Health & System Monitoring

* `/api/v1/health` endpoint
* Logs for security and system events
* Audit trail (future enhancement)

---

# 5️⃣ **Acceptance Criteria (MVP)**

| Feature            | AC (Acceptance Criteria)                                         |
| ------------------ | ---------------------------------------------------------------- |
| User registration  | Must be able to register with email, username, password, role    |
| Login              | Returns JWT token; allows subsequent requests with token         |
| Role enforcement   | Students cannot access teacher/admin endpoints                   |
| Student enrollment | Can enroll only in existing courses; cannot enroll twice         |
| Course CRUD        | Teachers can only modify courses they teach                      |
| Department         | Students must belong to exactly one department                   |
| API responses      | Use DTOs with `@Builder`; sensitive info like password is hidden |
| Health check       | `/api/v1/health` returns status, service, timestamp              |

---

# 6️⃣ **Future Features / Extensions**

1. **Fee Payment Module**

    * Link students to invoices / payment records
    * Integrate with payment gateways (e.g., Interswitch, Stripe, Paystack)
    * Track payment status and generate receipts

2. **Assignment Submission & Grading**

    * File uploads
    * Deadline enforcement
    * Automatic notifications to students

3. **Notification System**

    * Email or in-app notifications for assignment deadlines, course updates

4. **Analytics / Dashboard**

    * For admins: number of students, courses, enrollment trends
    * For teachers: student performance stats

5. **Multi-department Courses**

    * Optional: courses shared across departments

6. **Role Enhancements**

    * Add `COUNSELOR`, `SUPERVISOR`, or other roles as needed

---

# 7️⃣ **Security & Best Practices**

* JWT tokens for stateless authentication
* Role-based access with `@RequiredRole` + `RBAInterceptor`
* Use `@JsonIgnore` for sensitive fields
* Password hashing (BCrypt)
* Centralized exception handling

---

# 8️⃣ **API Versioning and Structure**

```
/api/v1/auth         → login, register
/api/v1/students     → student CRUD, enrollment
/api/v1/teachers     → teacher CRUD, course assignment
/api/v1/departments  → department CRUD
/api/v1/courses      → course CRUD, assignments
/api/v1/health       → system health
```

```
+----------------+       1        +----------------+       1       +----------------+
|    Department  |---------------->|     Teacher    |<---------------|     Course     |
+----------------+ 0..*           +----------------+ 1           0..* +----------------+
| id             |                 | id             |               | id             |
| name           |                 | fullName       |               | name           |
| description    |                 | auth_id (FK)   |               | description    |
| createdAt      |                 | department_id  |               | teacher_id FK  |
| updatedAt      |                 | createdAt      |               | createdAt      |
+----------------+                 | updatedAt      |               | updatedAt      |
                                   +----------------+               +----------------+
           1
+----------------+           1
|     Student    |-------------------+
+----------------+                   |
| id             |                   |
| fullName       |                   |
| enrollmentNum  |                   |
| grade          |                   |
| auth_id (FK)   |                   |
| department_id  |                   |
| createdAt      |                   |
| updatedAt      |                   |
+----------------+                   |
                                     |
                                     |
                                     |
                                     |
                                     | 0..*
                                     |
                             +----------------+
                             |   Enrollment   |
                             +----------------+
                             | id             |
                             | student_id FK  |
                             | course_id FK   |
                             | enrolledAt     |
                             +----------------+

+----------------+ 1
|     Auth       |
+----------------+
| id             |
| email          |
| username       |
| password       |
| role           |
| createdAt      |
| updatedAt      |
+----------------+
      |1
      |
      |1
+----------------+       +----------------+
|    Student     |       |    Teacher     |
+----------------+       +----------------+
```