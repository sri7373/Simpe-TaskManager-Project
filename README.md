A Simple Task Manager Solo Project

Description:
A simple Task Management System to create, read, update, and delete tasks. Later, it can be expanded with users, priorities, tags, and a React frontend. For now, it can be tested with a swagger UI.

1️⃣ Business Requirements
Users can create tasks with title, description, status, and due date.
Users can view a list of all tasks.
Users can update a task (status, description, due date).
Users can delete tasks.
Backend must use MS SQL 2025 Express as database.
Backend exposes a REST API for all CRUD operations.
Frontend is optional for now; API can be tested using Swagger UI.

2️⃣ Architecture Flow
[React Frontend (Optional, Next.js/ShadUI)]
             |
             v
      [Swagger UI / API Client]
             |
             v
     [Backend REST API (Spring Boot)]
       |                     |
       v                     v
 [Task Microservice]     [User Microservice (Optional)]
       |
       v
   [MS SQL Database]
   - Tasks Table
   - Users Table (optional)


Notes:

Backend uses Spring Boot with REST API endpoints.
Each microservice handles a single responsibility (Task management, optional User management).
MS SQL holds all persistent data.

taskmanager-backend/
│
├─ src/main/java/com/eiu/taskmanager/
│   ├─ controller/                       # Rest API endpoints
│   │   └─ TaskController.java
│   ├─ model/                            # Entity classes
│   │   └─ Task.java
│   ├─ repository/                       # DB Queries
│   │   └─ TaskRepository.java
│   ├─ service/                          # Business Logic
│   │   └─ TaskService.java
│   └─ TaskManagerApplication.java
│
├─ src/main/resources/
│   ├─ application.properties
│
├─ pom.xml

Client  → Controller → Service → Repository → Database Tables
(The 4 backend layers)

New features are going to be added. 