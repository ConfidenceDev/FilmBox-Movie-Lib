# FilmBox Movie Library

FilmBox is a full-stack movie library application that allows users to browse, add, update, and delete movies. The backend is built with Spring Boot (microservices architecture), and the frontend is a modern React app. The system supports anonymous authentication using JWT and provides a simple, user-friendly interface for managing a movie collection.

---

## Features

- Browse, search, and filter movies
- Add, update, and delete movies (with JWT-based authorization)
- Anonymous sign-in (no registration required)
- Microservices backend: Service Registry, API Gateway, Movie Service
- API documentation via Swagger UI

---

## Frontend: React App

### Prerequisites

- Node.js (v18 or higher recommended)
- npm

### Steps

```sh
cd Frontend
npm install
npm run dev
```

The app will be available at [http://localhost:5173](http://localhost:5173).

---

## Backend: Microservices

### Prerequisites

- Java 21
- Maven
- PostgreSQL (or use Docker as below)

### Run Order

1. **Service Registry**

   ```sh
   cd ServiceRegistry
   mvn spring-boot:run
   ```

   Access Eureka dashboard at [http://localhost:8761](http://localhost:8761).

2. **Cloud Gateway**

   ```sh
   cd CloudGateway
   mvn spring-boot:run
   ```

   Gateway runs at [http://localhost:9090](http://localhost:9090).

3. **Movie Service**
   ```sh
   cd MovieService
   mvn spring-boot:run
   ```
   Movie Service runs at [http://localhost:8082](http://localhost:8082).

#### API Documentation (Swagger UI)

Once the backend is running, access the API docs at:

```
http://localhost:8080/swagger-ui/index.html
```

---

### Authentication & Authorization

- **Authentication:** Anonymous sign-in is supported. Users sign in with a random username and receive a JWT token.
- **Authorization:** All protected endpoints require a valid JWT token in the `Authorization` header. Only the user who posted a movie (poster) can update or delete it.

---

## Alternative: Run with Docker

You can run the frontend (React + Vite) and backend stack (PostgreSQL, Service Registry, Cloud Gateway, Movie Service) using Docker Compose (Start, Stop and Access to PostgreSQL Database):

```sh
docker compose -f docker-compose.yml up -d
```

```sh
docker compose -f docker-compose.yml down
```

```sh
docker exec -it postgres psql -U postgres -d filmboxdb
```

- PostgreSQL: `localhost:5432`
- Service Registry: [http://localhost:8761](http://localhost:8761)
- Cloud Gateway: [http://localhost:9090](http://localhost:9090)
- Movie Service: [http://localhost:8082](http://localhost:8082)
- Frontend: [http://localhost:5173](http://localhost:5173)

---

## Project Structure

```
FilmBox-Movie-Lib/
  ├── CloudGateway/
  ├── Frontend/
  ├── MovieService/
  ├── ServiceRegistry/
  ├── docker-compose.yml
  └── README.md
```

---

## Contact

For inquiries, support, bug reports, or suggestions:

- Email: confidostic3@gmail.com
- Facebook: Confidence Dev
- Twitter: [@ConfidenceDev](https://twitter.com/confidencedev)
