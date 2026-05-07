# SmartConsult - Smart Healthcare Appointment & Records System

A comprehensive **Spring Boot REST API** for managing healthcare appointments and medical records with role-based access control (RBAC), JWT authentication, and secure appointment booking.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Database Configuration](#database-configuration)
- [API Documentation](#api-documentation)
- [Usage Examples](#usage-examples)
- [Project Structure](#project-structure)
- [Error Handling](#error-handling)
- [Security](#security)
- [Testing](#testing)

## 🎯 Overview

SmartConsult is a **secure, production-ready healthcare management system** designed to streamline appointment booking and medical records management. The system supports three distinct user roles:

- **Patients**: Book appointments, view bookings, access medical records
- **Doctors**: View scheduled appointments, create medical records
- **Admins**: Register and manage doctor profiles

## ✨ Features

### Core Features

- ✅ **Role-Based Access Control (RBAC)** - 3 user roles with granular permissions
- ✅ **JWT Authentication** - Stateless token-based security with 24-hour expiration
- ✅ **Appointment Management** - Book, view, and cancel appointments
- ✅ **Double-Booking Prevention** - Validation logic prevents appointment conflicts
- ✅ **Medical Records System** - Doctors create records, patients access their history
- ✅ **Input Validation** - Comprehensive bean validation on all DTOs
- ✅ **Global Exception Handling** - Centralized error responses with proper HTTP status codes
- ✅ **Secure Password Encoding** - BCrypt encryption for all passwords

### Business Logic

- 15+ REST API endpoints
- Appointment conflict detection at the doctor/date/time level
- Role-specific endpoint access
- Audit trail through appointment status tracking (BOOKED, CANCELLED, COMPLETED)

## 🏗️ Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│   REST Controllers (API Layer)      │
├─────────────────────────────────────┤
│   Services (Business Logic)         │
├─────────────────────────────────────┤
│   Repositories (Data Access)        │
├─────────────────────────────────────┤
│   Entities (JPA/Hibernate ORM)      │
├─────────────────────────────────────┤
│   MySQL Database                    │
└─────────────────────────────────────┘
```

### Security Flow

```
Client Request
    ↓
JWT Authentication Filter
    ↓ (validate token)
Security Context Setup
    ↓
Role-Based Authorization
    ↓
Controller → Service → Repository
    ↓
Response with proper status codes
```

## 🛠️ Tech Stack

| Component             | Technology              |
| --------------------- | ----------------------- |
| **Framework**         | Spring Boot 3.5.9       |
| **Java Version**      | Java 21 (LTS)           |
| **Database**          | MySQL 8.0+              |
| **Authentication**    | JWT (JJWT 0.11.5)       |
| **Security**          | Spring Security 6.x     |
| **ORM**               | JPA/Hibernate           |
| **Validation**        | Jakarta Bean Validation |
| **Build Tool**        | Maven 3.8+              |
| **Password Encoding** | BCrypt                  |

## 📋 Prerequisites

- **Java 21** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/#java21) or use OpenJDK
- **Maven 3.8+** - For build automation
- **MySQL 8.0+** - Database server
- **Git** - For version control
- **Postman/cURL** - For API testing

## 🚀 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/SmartConsult.git
cd SmartConsult
```

### Step 2: Create MySQL Database

```sql
CREATE DATABASE healthcare_db;
USE healthcare_db;
```

### Step 3: Update Database Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Step 4: Build the Project

```bash
mvn clean install
```

### Step 5: Run the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## 🗄️ Database Configuration

### Default Settings

- **Host:** localhost
- **Port:** 3306
- **Database:** healthcare_db
- **Username:** root
- **DDL Mode:** update (auto-creates tables)

### Application Properties

```properties
spring.application.name=SmartConsult
spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_db
spring.datasource.username=root
spring.datasource.password=2003
spring.jpa.hibernate.ddl-auto=update
jwt.secret=smartconsult#@%hegdgd^$58jnf^$$
jwt.expiration=86400000
```

## 📡 API Documentation

### Base URL

```
http://localhost:8080/api
```

### Authentication

All endpoints except `/auth/**` require JWT token in header:

```
Authorization: Bearer <jwt_token>
```

---

## 🔐 Auth Endpoints (Public)

### 1. User Registration

**POST** `/auth/register`

Create a new user account.

**Request Body:**

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "role": "PATIENT"
}
```

**Roles:** `PATIENT`, `DOCTOR`, `ADMIN`

**Response:**

```json
{
  "message": "User registered successfully"
}
```

**Status:** 201 Created

---

### 2. User Login

**POST** `/auth/login`

Authenticate user and receive JWT token.

**Request Body:**

```json
{
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Status:** 200 OK

---

## 👤 Patient Endpoints

### 3. Book Appointment

**POST** `/patient/appointments`

Book a new appointment with a doctor.

**Request Body:**

```json
{
  "doctorId": 1,
  "appointmentDate": "2026-05-15",
  "timeSlot": "10:30:00"
}
```

**Response:**

```json
{
  "message": "Appointment booked successfully"
}
```

**Status:** 201 Created

**Validations:**

- Doctor must exist
- Appointment date must be today or future
- Time slot cannot be double-booked

---

### 4. View My Appointments

**GET** `/patient/appointments`

Retrieve all appointments for the logged-in patient.

**Response:**

```json
[
  {
    "id": 1,
    "doctorName": "Dr. Smith",
    "appointmentDate": "2026-05-15",
    "timeSlot": "10:30:00",
    "status": "BOOKED"
  }
]
```

**Status:** 200 OK

---

### 5. Cancel Appointment

**DELETE** `/patient/appointments/{appointmentId}`

Cancel a scheduled appointment.

**Response:**

```json
{
  "message": "Appointment cancelled successfully"
}
```

**Status:** 200 OK

---

### 6. Browse Doctors

**GET** `/patient/doctors`

View all available doctors in the system.

**Response:**

```json
[
  {
    "id": 1,
    "name": "Dr. Smith",
    "specialization": "Cardiology",
    "experience": 15,
    "consultationFee": 500.0
  }
]
```

**Status:** 200 OK

---

### 7. Get My Medical Records

**GET** `/patient/medical-records`

Retrieve all medical records for the logged-in patient.

**Response:**

```json
[
  {
    "id": 1,
    "appointmentId": 5,
    "diagnosis": "Hypertension",
    "prescription": "Take Lisinopril 10mg daily",
    "notes": "Monitor blood pressure weekly",
    "patientName": "John Doe",
    "doctorName": "Dr. Smith"
  }
]
```

**Status:** 200 OK

---

## 👨‍⚕️ Doctor Endpoints

### 8. View My Appointments

**GET** `/doctor/appointments`

View all scheduled appointments for the logged-in doctor.

**Response:**

```json
[
  {
    "appointmentId": 1,
    "patientName": "John Doe",
    "appointmentDate": "2026-05-15",
    "timeSlot": "10:30:00",
    "status": "BOOKED"
  }
]
```

**Status:** 200 OK

---

### 9. Create Medical Record

**POST** `/doctor/medical-records/{appointmentId}`

Create a medical record for a completed appointment.

**Path Parameter:** `appointmentId` (Long)

**Request Body:**

```json
{
  "diagnosis": "Type 2 Diabetes",
  "prescription": "Metformin 500mg twice daily",
  "notes": "Monitor glucose levels, follow-up in 3 months"
}
```

**Response:**

```json
{
  "message": "Medical record created successfully"
}
```

**Status:** 201 Created

---

### 10. Get My Created Medical Records

**GET** `/doctor/medical-records`

View all medical records created by the logged-in doctor.

**Response:**

```json
[
  {
    "id": 1,
    "appointmentId": 5,
    "diagnosis": "Type 2 Diabetes",
    "prescription": "Metformin 500mg twice daily",
    "notes": "Monitor glucose levels",
    "patientName": "John Doe",
    "doctorName": "Dr. Smith"
  }
]
```

**Status:** 200 OK

---

## 🔑 Admin Endpoints

### 11. Register Doctor

**POST** `/admin/doctors`

Register a doctor profile for an existing doctor user.

**Request Body:**

```json
{
  "userId": 2,
  "specialization": "Cardiology",
  "experience": 15,
  "consultationFee": 500.0
}
```

**Response:**

```json
{
  "message": "Doctor added successfully"
}
```

**Status:** 201 Created

**Validations:**

- User must exist
- User must have DOCTOR role
- Cannot register duplicate doctor

---

## 📊 Entity Relationships

### Data Model

```
User (1) ──────────── (1) Doctor
  │
  └──── (1 to Many) ──── Appointment
                            │
                            └──── (1 to 1) ──── MedicalRecord
```

### Entity Details

**User**

- Roles: PATIENT, DOCTOR, ADMIN
- Email: Unique identifier

**Doctor**

- OneToOne: User (doctor profile)
- Fields: specialization, experience, consultationFee

**Appointment**

- ManyToOne: Doctor
- ManyToOne: Patient (User)
- Status: BOOKED, CANCELLED, COMPLETED

**MedicalRecord**

- OneToOne: Appointment
- Fields: diagnosis, prescription, notes

---

## 💾 Usage Examples

### Complete Workflow Example

#### 1. Register Patient

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "Password123",
    "role": "PATIENT"
  }'
```

#### 2. Register Doctor User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dr. Smith",
    "email": "smith@example.com",
    "password": "Password123",
    "role": "DOCTOR"
  }'
```

#### 3. Patient Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "Password123"
  }'
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### 4. Admin Registers Doctor Profile

```bash
curl -X POST http://localhost:8080/api/admin/doctors \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{
    "userId": 2,
    "specialization": "Cardiology",
    "experience": 15,
    "consultationFee": 500.00
  }'
```

#### 5. Patient Books Appointment

```bash
curl -X POST http://localhost:8080/api/patient/appointments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <patient_token>" \
  -d '{
    "doctorId": 1,
    "appointmentDate": "2026-05-15",
    "timeSlot": "10:30:00"
  }'
```

#### 6. Doctor Creates Medical Record

```bash
curl -X POST http://localhost:8080/api/doctor/medical-records/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <doctor_token>" \
  -d '{
    "diagnosis": "Hypertension",
    "prescription": "Lisinopril 10mg daily",
    "notes": "Monitor blood pressure"
  }'
```

---

## 📁 Project Structure

```
SmartConsult/
├── src/main/java/com/example/SmartConsult/
│   ├── controller/                 # REST API endpoints
│   │   ├── AuthController.java
│   │   ├── AppointmentController.java
│   │   ├── DoctorController.java
│   │   └── MedicalRecordController.java
│   │
│   ├── service/                    # Business logic layer
│   │   ├── AuthService.java
│   │   ├── AppointmentService.java
│   │   ├── DoctorService.java
│   │   └── MedicalRecordService.java
│   │
│   ├── repository/                 # Data access layer
│   │   ├── UserRepository.java
│   │   ├── DoctorRepository.java
│   │   ├── AppointmentRepository.java
│   │   └── MedicalRecordRepository.java
│   │
│   ├── entity/                     # JPA entities
│   │   ├── User.java
│   │   ├── Doctor.java
│   │   ├── Appointment.java
│   │   ├── MedicalRecord.java
│   │   ├── Role.java (enum)
│   │   └── AppointmentStatus.java (enum)
│   │
│   ├── dto/                        # Data transfer objects
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   ├── AppointmentRequest.java
│   │   ├── AppointmentResponse.java
│   │   ├── DoctorRequest.java
│   │   ├── DoctorResponse.java
│   │   ├── MedicalRecordRequest.java
│   │   ├── MedicalRecordResponse.java
│   │   ├── DoctorAppointmentResponse.java
│   │   └── ErrorResponse.java
│   │
│   ├── security/                   # Security & JWT
│   │   ├── SecurityConfig.java
│   │   ├── JwtService.java
│   │   └── JwtAuthenticationFilter.java
│   │
│   ├── exception/                  # Exception handling
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── DuplicateBookingException.java
│   │   ├── InvalidCredentialsException.java
│   │   └── UnauthorizedException.java
│   │
│   ├── config/                     # Configuration classes
│   │   └── SecurityBeansConfig.java
│   │
│   └── SmartConsultApplication.java # Main entry point
│
├── src/main/resources/
│   └── application.properties      # Configuration file
│
├── src/test/java/                  # Test classes
│
├── pom.xml                         # Maven configuration
└── README.md                       # This file
```

---

## ⚠️ Error Handling

The API uses a standardized error response format:

```json
{
  "status": 400,
  "message": "Validation error message",
  "timestamp": "2026-05-07T10:30:00",
  "path": "/api/patient/appointments"
}
```

### HTTP Status Codes

| Status  | Meaning               | Example                       |
| ------- | --------------------- | ----------------------------- |
| **200** | OK                    | Successful retrieval          |
| **201** | Created               | Resource successfully created |
| **400** | Bad Request           | Validation failed             |
| **401** | Unauthorized          | Invalid credentials           |
| **403** | Forbidden             | Insufficient permissions      |
| **404** | Not Found             | Resource doesn't exist        |
| **409** | Conflict              | Double-booking attempt        |
| **500** | Internal Server Error | Unexpected error              |

### Custom Exceptions

- `ResourceNotFoundException` - Resource not found (404)
- `DuplicateBookingException` - Double-booking conflict (409)
- `InvalidCredentialsException` - Auth failure (401)
- `UnauthorizedException` - Permission denied (403)

---

## 🔒 Security Features

### JWT Authentication

- **Token Type:** Bearer
- **Algorithm:** HS256 (HMAC with SHA-256)
- **Expiration:** 24 hours
- **Claims:** email, role

### Security Layers

1. **Authentication Filter** - Validates JWT on every request
2. **Authorization** - Role-based endpoint access
3. **Password Encoding** - BCrypt hashing
4. **CSRF Protection** - Disabled for stateless API

### Best Practices Implemented

- ✅ Stateless session management
- ✅ Role-based access control (RBAC)
- ✅ Input validation on all endpoints
- ✅ Centralized exception handling
- ✅ Secure password encoding
- ✅ JWT token extraction and validation

---

## 🧪 Testing

### Run Tests

```bash
mvn test
```

### Run All Verifications (Build + Tests)

```bash
mvn verify
```

### Test Coverage

- Unit tests for service layer
- Integration tests for API endpoints
- JWT token validation tests
- Authorization tests

---

## 📝 Appointment Management Workflow

### Booking Flow

```
Patient → Browse Doctors → Select Doctor & Time → Book Appointment
           ↓
         System checks for conflicts
           ↓
         Appointment created (BOOKED)
           ↓
         Doctor receives notification
```

### Medical Records Flow

```
Appointment Completed → Doctor creates Medical Record
                           ↓
                        Patient can access record
                           ↓
                        Doctor can view created records
```

### Cancellation Flow

```
Patient initiates cancellation → Validate ownership
                              ↓
                        Status → CANCELLED
                              ↓
                        Slot becomes available
```

---

## 🚀 Performance Considerations

- **Database Indexing:** Email field indexed for faster lookups
- **Pagination:** Consider implementing for large datasets
- **Caching:** JWT tokens cached at application level
- **Query Optimization:** Use native queries for complex reports

---

## 📞 Support & Troubleshooting

### Common Issues

**Issue:** Connection refused to MySQL

```
Solution: Ensure MySQL is running on localhost:3306
          Check database credentials in application.properties
```

**Issue:** JWT token expired

```
Solution: Login again to get a new token
          Token expires after 24 hours
```

**Issue:** Double-booking error

```
Solution: Select a different time slot
          Check doctor availability
```

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👥 Team Contributions

This is a collaborative healthcare project demonstrating:

- Professional REST API design
- Secure authentication & authorization
- Enterprise-level exception handling
- Role-based access control
- Clean code principles

---

## 📞 Contact & Support

For questions or support, please open an issue in the GitHub repository.

**Repository:** [SmartConsult GitHub](https://github.com/your-username/SmartConsult)

---

**Last Updated:** May 7, 2026  
**Version:** 1.0.0

# 🏥 Smart Healthcare Appointment & Records System

A secure, role-based healthcare management system that enables patients to book appointments, doctors to manage schedules, and administrators to control doctor profiles. The application follows real-world healthcare workflows and uses JWT-based authentication for secure and scalable access.

---

## 🚀 Features

### 👤 Authentication & Authorization
- JWT-based **stateless authentication**
- Role-Based Access Control (RBAC)
  - **PATIENT**
  - **DOCTOR**
  - **ADMIN**

### 🧑‍⚕️ Patient Module
- Patient registration and login
- View available doctors
- Book appointments with doctors
- Prevents **double booking** of time slots
- View all personal appointments

### 👨‍⚕️ Doctor Module
- Doctor profile management (via Admin)
- View assigned appointments
- Secure access limited to doctor’s own data

### 🛠 Admin Module
- Add doctor profiles linked to user accounts
- Manage system-level data

---

## 🧠 System Workflow

1. User registers and logs in
2. JWT token is generated and sent to the client
3. Client sends JWT in `Authorization` header for every request
4. Backend validates token using a custom JWT filter
5. Access is granted based on user role
6. Business logic is executed (booking, viewing appointments, etc.)

---

## 🏗 Architecture

- **Controller Layer** – Handles REST API requests
- **Service Layer** – Business logic and validations
- **Repository Layer** – Database access using Spring Data JPA
- **Security Layer** – JWT authentication & role-based authorization

---

## 🧰 Tech Stack

- **Backend:** Java, Spring Boot, Spring Security
- **Authentication:** JWT (JSON Web Tokens)
- **Database:** MySQL
- **ORM:** Spring Data JPA (Hibernate)
- **Build Tool:** Maven
- **API Testing:** Postman

---

## 🔐 Security Highlights

- Stateless authentication using JWT
- Custom `OncePerRequestFilter` for token validation
- Role-based endpoint protection
- Secure password hashing using `BCryptPasswordEncoder`

---

## 📌 API Endpoints (Sample)

### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Patient
- `GET /api/patient/doctors`
- `POST /api/patient/appointments`
- `GET /api/patient/appointments`

### Doctor
- `GET /api/doctor/appointments`

### Admin
- `POST /api/admin/doctors`

---

## ▶️ How to Run the Project

1. Clone the repository
   ```bash
   git clone https://github.com/your-username/smart-healthcare-system.git
Configure MySQL database in application.properties

Run the application

mvn spring-boot:run


Test APIs using Postman
