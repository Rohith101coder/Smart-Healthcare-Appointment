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
