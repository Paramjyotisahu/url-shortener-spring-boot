# URL Shortener Service 🔗

A backend web application built using **Spring Boot** and **MySQL** that converts long URLs into short, shareable links and redirects users to the original URLs efficiently.

This project demonstrates clean backend architecture, database persistence using Spring Data JPA, and real-world URL shortening logic.

---

## 🚀 Features

- Convert long URLs into unique short URLs
- Redirect short URLs to the original URLs
- Store URL mappings persistently using MySQL
- Clean layered architecture (Controller, Service, Repository)
- REST-based backend logic
- Gradle-based project setup

---

## 🛠 Tech Stack

- Java
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Gradle
- HTML (Thymeleaf)

---

## 📂 Project Structure
src/main/java/com/example/URLShortnerService
├── controller # Handles HTTP requests
├── service # Business logic
├── repository # Database access layer
├── entity # JPA entities
├── dto # Request & response DTOs
├── util # Utility classes (short code generation)
└── UrlShortnerServiceApplication.java

---

## ⚙️ How the Application Works

1. User submits a long URL
2. Application generates a unique short code
3. URL mapping is stored in MySQL database
4. When the short URL is accessed, the user is redirected to the original URL

---

## 🗄 Database Configuration (MySQL)

### 1️⃣ Create Database
```sql
CREATE DATABASE url_shortener;
