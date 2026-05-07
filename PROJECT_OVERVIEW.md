# SmartConsult Project Overview

## 1. Project Summary

SmartConsult is a Spring Boot-based healthcare appointment and records management system. It supports three roles: `PATIENT`, `DOCTOR`, and `ADMIN`. The application allows patients to book and cancel appointments, doctors to view schedules and create medical records, and admins to register doctor profiles.

This file explains the project architecture, security flow, API contract, frontend guidance, and interview talking points in a complete and structured way.

---

## 2. What the Project Does

### Core capabilities

- Role-based access control for healthcare workflows
- JWT authentication for stateless, secure API access
- Appointment booking with double-booking prevention
- Appointment cancellation by patients
- Doctor profile registration by admins
- Medical record creation by doctors
- Patient access to their own medical records
- Doctor access to medical records they create

### Why this is valuable

- Improves patient experience by letting patients self-book appointments
- Keeps doctor workloads manageable by preventing booking conflicts
- Ensures security through token-based access and role checks
- Supports healthcare auditability by connecting appointments with medical records

---

## 3. Architecture Overview

### Layers

1. **Controller Layer** - Receives HTTP requests and returns JSON responses.
2. **Service Layer** - Contains business rules and validation logic.
3. **Repository Layer** - Persists entities using Spring Data JPA.
4. **Entity Layer** - Models database tables as Java classes.
5. **Security Layer** - JWT validation, role enforcement, and stateless sessions.

### Main Packages

- `controller` - REST endpoints
- `service` - business logic
- `repository` - JPA interfaces
- `entity` - JPA models
- `dto` - request and response objects
- `security` - JWT and Spring Security config
- `exception` - custom error handling

---

## 4. Data Model

### User

- `id`: Long
- `name`: String
- `email`: String (unique)
- `password`: String (BCrypt hashed)
- `role`: `Role` enum (`ADMIN`, `DOCTOR`, `PATIENT`)

### Doctor

- `id`: Long
- `specialization`: String
- `experience`: int
- `consultationFee`: double
- `user`: one-to-one link to `User`

### Appointment

- `id`: Long
- `doctor`: many-to-one to `Doctor`
- `patient`: many-to-one to `User`
- `appointmentDate`: LocalDate
- `timeSlot`: LocalTime
- `status`: `AppointmentStatus` enum (`BOOKED`, `CANCELLED`, `COMPLETED`)

### MedicalRecord

- `id`: Long
- `appointment`: one-to-one to `Appointment`
- `diagnosis`: String
- `prescription`: String
- `notes`: String

---

## 5. Security Architecture

### How security works

- Public endpoints are under `/api/auth/**`
- All other requests require a valid JWT bearer token
- `SecurityConfig` disables CSRF and sets session creation to stateless
- The custom filter `JwtAuthenticationFilter` extracts the token from `Authorization: Bearer <token>`
- The filter validates token claims using `JwtService`
- It loads the user `email` and `role` into Spring Security context
- Role-based request matching is applied:
  - `/api/patient/**` → `PATIENT`
  - `/api/doctor/**` → `DOCTOR`
  - `/api/admin/**` → `ADMIN`

### JWT generation

- Token includes:
  - `sub` claim = user email
  - `role` claim = user role
- Token signing algorithm: `HS256`
- Secret is stored in `application.properties`
- Expiration time: `86400000` ms (24 hours)

### Validation flow

1. Request enters API
2. JWT filter checks `Authorization` header
3. If token is missing or invalid, request continues unauthenticated
4. Security layer rejects unauthorized requests based on role
5. If token is valid, request proceeds with a granted authority

### Error handling

- Invalid token or missing auth returns a security error
- Validation errors are transformed into structured JSON
- Custom exceptions map to correct HTTP codes

---

## 6. API List and Contracts

### 6.1 Authentication APIs

#### `POST /api/auth/register`

- Accepts: `RegisterRequest`
  - `name`: string
  - `email`: string
  - `password`: string
  - `role`: enum (`PATIENT`, `DOCTOR`, `ADMIN`)
- Returns:
  - `201 Created`
  - body: `"User registered successfully"`
- Notes:
  - Uses bean validation
  - Password is hashed before saving
  - `email` must be unique

#### `POST /api/auth/login`

- Accepts: `LoginRequest`
  - `email`: string
  - `password`: string
- Returns:
  - `200 OK`
  - body: `LoginResponse` containing `token`
- Notes:
  - Validates email/password
  - Generates JWT on success

### 6.2 Patient APIs

#### `POST /api/patient/appointments`

- Accepts: `AppointmentRequest`
  - `doctorId`: long
  - `appointmentDate`: date
  - `timeSlot`: time
- Returns:
  - `201 Created`
  - body: `"Appointment booked successfully"`
- Notes:
  - Validates date is today or future
  - Prevents double-booking for a specific doctor/date/time

#### `GET /api/patient/appointments`

- Returns:
  - `200 OK`
  - body: list of `AppointmentResponse`
    - `id`
    - `doctorName`
    - `appointmentDate`
    - `timeSlot`
    - `status`

#### `DELETE /api/patient/appointments/{appointmentId}`

- Returns:
  - `200 OK`
  - body: `"Appointment cancelled successfully"`
- Notes:
  - Only the owning patient can cancel
  - Appointment status becomes `CANCELLED`

#### `GET /api/patient/doctors`

- Returns:
  - `200 OK`
  - body: list of `DoctorResponse`
    - `id`
    - `name`
    - `specialization`
    - `experience`
    - `consultationFee`

#### `GET /api/patient/medical-records`

- Returns:
  - `200 OK`
  - body: list of `MedicalRecordResponse`
    - `id`
    - `appointmentId`
    - `diagnosis`
    - `prescription`
    - `notes`
    - `patientName`
    - `doctorName`
- Notes:
  - Only the patient’s own records are returned

### 6.3 Doctor APIs

#### `GET /api/doctor/appointments`

- Returns:
  - `200 OK`
  - body: list of `DoctorAppointmentResponse`
    - `appointmentId`
    - `patientName`
    - `appointmentDate`
    - `timeSlot`
    - `status`

#### `POST /api/doctor/medical-records/{appointmentId}`

- Accepts: `MedicalRecordRequest`
  - `diagnosis`: string
  - `prescription`: string
  - `notes`: string
- Returns:
  - `201 Created`
  - body: `"Medical record created successfully"`
- Notes:
  - Only the doctor on the appointment can create the record
  - A record is unique per appointment

#### `GET /api/doctor/medical-records`

- Returns:
  - `200 OK`
  - body: list of `MedicalRecordResponse`
    - same fields as patient record response
- Notes:
  - Returns records created by the logged-in doctor only

### 6.4 Admin APIs

#### `POST /api/admin/doctors`

- Accepts: `DoctorRequest`
  - `userId`: long
  - `specialization`: string
  - `experience`: int
  - `consultationFee`: double
- Returns:
  - `201 Created`
  - body: `"Doctor added successfully"`
- Notes:
  - User must already exist and have `DOCTOR` role
  - Creates the doctor profile that links to the existing user

### 6.5 Support / Health Check

#### `GET /test`

- Returns:
  - `200 OK`
  - body: `"ok"`
- Notes:
  - Not protected by role-based security

---

## 7. Frontend Development Guidance

### Recommended screens

1. **Login / Register**
2. **Patient dashboard**
   - Browse doctors
   - Book appointment
   - View appointments
   - Cancel appointment
   - View medical records
3. **Doctor dashboard**
   - View appointments
   - Create medical records
   - View created records
4. **Admin dashboard**
   - Register doctor profiles

### Authentication handling

- Store JWT in secure storage (e.g. memory or secure store)
- Send `Authorization: Bearer <token>` header for all protected requests
- Refresh login after token expiration (24 hours)

### UI behavior and validation

- Enforce required fields before calling API
- Show validation errors from API responses
- Prevent booking past dates or invalid time formats
- Display doctor details for selection
- Use appointment `id` for cancellation requests

### Data contract tips

- `doctorId` is required for appointment creation
- `appointmentId` is required for record creation and deletion
- `role` is chosen at registration, and must match allowed values

### Error handling

- If API returns `401` or `403`, redirect to login or show insufficient permissions
- If API returns `409`, show conflict message such as `Time slot already booked`
- Use the structured error response format:
  ```json
  {
    "status": 400,
    "message": "Validation error message",
    "path": "/api/auth/register",
    "timestamp": "2026-05-07T20:11:43.5816219"
  }
  ```

### Frontend state model

- `currentUser`: holds user email/role from login
- `token`: JWT for authorization
- `doctorList`: list of doctors from `/patient/doctors`
- `appointments`: patient or doctor appointment list
- `medicalRecords`: patient or doctor records list

---

## 8. Project Flow: End-to-End

### User onboarding

1. **Register** via `/api/auth/register`
2. **Login** via `/api/auth/login`
3. Receive a JWT token

### Patient workflow

1. Fetch doctors with `/api/patient/doctors`
2. Book appointment with `/api/patient/appointments`
3. View booked appointments with `/api/patient/appointments`
4. Cancel appointment with `/api/patient/appointments/{appointmentId}`
5. View medical records with `/api/patient/medical-records`

### Doctor workflow

1. Login and receive JWT
2. View schedule with `/api/doctor/appointments`
3. Create record with `/api/doctor/medical-records/{appointmentId}`
4. View created records with `/api/doctor/medical-records`

### Admin workflow

1. Login as admin
2. Create doctor profile with `/api/admin/doctors`

---

## 9. STAR Ready Interview Structure

### Situation

The project is a healthcare appointment system built in Spring Boot for patients, doctors, and admins. It was created to solve the problem of manual appointment scheduling, insecure access, and disconnected clinical record workflows.

### Task

The goal was to design and implement a secure, role-based REST API that:

- allows patient self-service booking
- ensures doctors can manage their own schedules
- lets admins register doctor profiles
- connects appointments to medical records
- protects data with JWT and role-based permissions

### Action

I implemented:

- Spring Boot controllers for each role
- Services for appointment booking, cancellation, doctor management, and record creation
- JPA entities and repositories for persistence
- JWT authentication with a custom `OncePerRequestFilter`
- Role-based route protection in `SecurityConfig`
- DTO validation for all requests
- Global exception handling for consistent API errors
- A clear API contract for frontend development

### Result

The system now supports:

- secure patient booking and doctor schedule management
- conflict prevention across appointment slots
- doctor-managed medical records
- clear, safe access control by role
- interview-ready explanation of architecture and flow

---

## 10. Challenges Faced and Solutions

### Challenge 1: Role-based security consistency

- **Solution:** Centralized route permissions in `SecurityConfig` and used JWT claims to attach role authorities.
- **Result:** Only authorized users can access protected resources.

### Challenge 2: Preventing double-booking

- **Solution:** Added repository method `existsByDoctorIdAndAppointmentDateAndTimeSlot` and verified before saving.
- **Result:** Booking conflicts are rejected, protecting doctors from scheduling collisions.

### Challenge 3: Entity relationships and medical record linkage

- **Solution:** Linked `MedicalRecord` to `Appointment` with a one-to-one mapping.
- **Result:** Each appointment can have exactly one associated medical record.

### Challenge 4: Clean API contract for frontend developers

- **Solution:** Created explicit DTOs and structured responses.
- **Result:** Frontend teams can implement against stable request/response shapes.

### Challenge 5: Validation and structured error messages

- **Solution:** Added Jakarta validation annotations and global exception handling.
- **Result:** Users receive consistent and descriptive error responses.

---

## 11. Frontend Implementation Notes

### Useful frontend pages

- Login / Register page
- Patient dashboard
- Doctor dashboard
- Admin doctor registration page
- Appointment booking page
- Medical record display page

### API integration notes

- Use `POST /api/auth/login` to obtain JWT
- Attach `Authorization: Bearer <token>` to every protected request
- For patient actions, use endpoints under `/api/patient`
- For doctor actions, use endpoints under `/api/doctor`
- For admin actions, use `/api/admin`

### Data-driven UI suggestions

- Show doctor specialization and fee when booking
- Disable booking if selected slot already exists server-side
- Keep appointment details and medical records clearly linked by appointment ID
- Display record metadata: doctor name, patient name, diagnosis, prescription, notes

---

## 12. How to Confidently Speak About the Project

### Key talking points

- Built with Spring Boot, Spring Security, and JPA
- Uses JWT authentication for stateless API security
- Implements 3 roles with strict route-level access control
- Supports appointment booking, cancellation, and medical records
- Enforces validation and meaningful error responses
- Designed for easy frontend integration

### What to emphasize

- This is a real-world healthcare workflow, not just CRUD
- The system is secure, token-based, and role-aware
- Business logic protects against booking conflicts
- Medical record creation is tightly coupled with appointments
- Frontend can rely on a stable JSON contract

### What to mention in interviews

- Role-specific API design and authorization
- Validation and data integrity rules
- Security filter chain and JWT processing
- How the frontend would use token storage and headers
- How the backend supports both patient and doctor views

---

## 13. Useful commands

```bash
mvn clean install
mvn spring-boot:run
mvn test
```

## 14. Final confidence statement

This project is a complete backend system with secure user roles, appointment conflict prevention, and medical record management. You can now describe not only what it does, but also how the architecture works, why security is designed this way, how the frontend consumes the API, and what business problems it solves.
