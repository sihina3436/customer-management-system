# Customer Management System — Setup Guide

Quick setup for running backend (Spring Boot) and frontend (React + Vite) locally.

---

## Requirements

- **Backend:** Java JDK, Maven, MariaDB/MySQL
- **Frontend:** Node.js 18+, npm
- **IDE:** IntelliJ IDEA (optional)

---

## BACKEND SETUP

### 1) Database Setup (Windows)

1. Install MariaDB from [mariadb.org](https://mariadb.org)
2. Open Command Prompt and create database:

```bash
mysql -u root -p
```

```sql
CREATE DATABASE cmsdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'cmsuser'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON cmsdb.* TO 'cmsuser'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 2) Import Schema & Data

```bash
mysql -u cmsuser -p'1234' cmsdb < src/main/resources/schema.sql
mysql -u cmsuser -p'1234' cmsdb < src/main/resources/data.sql
```

### 3) Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/cmsdb
spring.datasource.username=cmsuser
spring.datasource.password=1234
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false

server.port=8081

spring.servlet.multipart.max-file-size=2GB
spring.servlet.multipart.max-request-size=2GB

logging.level.root=INFO
logging.level.com.sihina.backend=DEBUG
```

### 4) Enable CORS

Add `@CrossOrigin` to your controllers in `src/main/java/com/sihina/backend/controller/`:

```java
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {
    // API endpoints
}
```

```java
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/master")
public class MasterDataController {
    // API endpoints
}
```

### 5) Run Backend

```bash
cd backend
mvn spring-boot:run
```
---

## FRONTEND SETUP

### 1) Install Dependencies

```bash
cd frontend
npm install
```

### 2) Create `.env.local`

Create `.env.local` file in `frontend` folder:

```env
VITE_API_BASE_URL=http://localhost:8081/api
```

### 3) Update axios.js

Ensure `src/api/axios.js` has:

```javascript
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api';

const axiosInstance = axios.create({
    baseURL: API_BASE_URL,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default axiosInstance;
```

### 4) Run Frontend

```bash
npm run dev
```

**Open:** `http://localhost:5173`

---

## API ENDPOINTS

**Base URL:** `http://localhost:8081/api`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/customers` | Create customer |
| GET | `/customers/{id}` | Get customer |
| PUT | `/customers/{id}` | Update customer |
| GET | `/customers?page=0&size=25` | List customers |
| POST | `/customers/bulk-upload` | Upload CSV/XLSX |
| GET | `/master/countries` | List countries |
| GET | `/master/cities` | List cities |

---

## Quick Test Commands (PowerShell)

### Create Customer
```powershell
curl -X POST "http://localhost:8081/api/customers" `
  -H "Content-Type: application/json" `
  -d '{"name":"John Doe","dateOfBirth":"1990-01-15","nic":"NIC-001"}'
```

### Get Customer
```powershell
curl -X GET "http://localhost:8081/api/customers/1"
```

### Update Customer
```powershell
curl -X PUT "http://localhost:8081/api/customers/1" `
  -H "Content-Type: application/json" `
  -d '{"name":"Jane Doe","dateOfBirth":"1990-01-15","nic":"NIC-001"}'
```

### List Customers
```powershell
curl -X GET "http://localhost:8081/api/customers?page=0&size=10"
```

### Upload File
```powershell
curl -X POST "http://localhost:8081/api/customers/bulk-upload" `
  -F "file=@C:\path\to\customers.csv"
```

---

## CSV Format for Upload

```csv
Name,DateOfBirth,NIC
John Doe,1990-01-15,NIC-001
Jane Smith,1985-05-20,NIC-002
Bob Johnson,1992-03-10,NIC-003
```

---

## Run Both Together

**Terminal 1 - Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm run dev
```

**Open Browser:** `http://localhost:5173`

---

## Troubleshooting

### CORS Error
- Verify `@CrossOrigin(origins = "http://localhost:5173")` in all controllers

### "Network Error" in Frontend
- Check backend is running on `http://localhost:8081`
- Verify `.env.local` has correct `VITE_API_BASE_URL`
- Restart frontend: `npm run dev`

### Database Connection Error
- Check credentials in `application.properties`: `cmsuser` / `1234`
- Verify database exists: `mysql -u cmsuser -p'1234' cmsdb -e "SHOW TABLES;"`

### Port Already in Use
```bash
# Backend - use different port in application.properties: server.port=8082
# Frontend - use different port
npm run dev -- --port 5174
```

### File Upload Fails
- Do NOT manually set `Content-Type` in form data
- Max file size: 2GB (configured in application.properties)

---

## Build for Production

### Backend
```bash
mvn -U clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
npm run build
npm run preview
```

---

## Useful Commands

### Backend
```bash
mvn spring-boot:run          # Run backend
mvn test                     # Run tests
mvn clean package            # Build JAR
```

### Frontend
```bash
npm install                  # Install dependencies
npm run dev                  # Start dev server
npm run build                # Create production build
npm run preview              # Preview production build
```

### Database
```bash
mysql -u cmsuser -p'1234' cmsdb              # Connect
mysql -u cmsuser -p'1234' cmsdb < schema.sql # Import
```

---

**Updated:** April 2026
**Sihina Nimnada**
