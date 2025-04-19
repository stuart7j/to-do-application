# ✅ To-Do REST API

## 🔧 Setup

### 1. Run MySQL
```bash
docker-compose up -d
```

### 2. Run Spring Boot App
```bash
./mvnw spring-boot:run
```

### 3. API URLs
- `POST /auth/register` `{ "username": "user", "password": "pass" }`
- `POST /auth/login` → returns JWT token

#### 📋 Task Endpoints (with Bearer token)
- `GET /tasks` → list all your tasks
- `POST /tasks` → create a task
- `GET /tasks/{id}` → get a single task by ID
- `PUT /tasks/{id}` → update a task by ID
- `DELETE /tasks/{id}` → delete a task by ID

## 🛡️ Security
- JWT auth
- Passwords hashed via BCrypt
- Only user can access their own tasks