# Tenpo Calculator Challenge

API REST para cálculo con porcentaje dinámico y almacenamiento de historial.

---

## Tecnologías

**Backend:**
- Java 21
- Spring Boot 3.3.4
- Spring Data JPA
- Spring WebFlux 
- Hibernate

**Base de Datos:**
- PostgreSQL 16
- Flyway

**Cache:**
- Caffeine Cache

**Infraestructura:**
- Docker & Docker Compose
- Gradle 8.10

**Testing:**
- JUnit 5
- Mockito
- TestContainers

**Documentación:**
- OpenAPI/Swagger
- Postman Collection

**Otros:**
- Lombok
- Checkstyle

---

## Requisitos

- Docker Desktop
- Git
- Java 21 (opcional para desarrollo local)

---

## Arquitectura
```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │
       ▼
┌──────────────────────────────────┐
│  Calculator Service :8080        │
│  - API REST                      │
│  - Cache (30 min)                │
│  - Historial asíncrono           │
└──────┬───────────────┬───────────┘
       │               │
       ▼               ▼
┌─────────────┐  ┌──────────────┐
│ Percentage  │  │ PostgreSQL   │
│ Service     │  │ :5434        │
│ :8081       │  │              │
└─────────────┘  └──────────────┘
```

---

## Ejecución

### Opción 1: Build desde código fuente
```bash
git clone https://github.com/JesussPacheco/tenpo-calculator-challenge.git
cd tenpo-challenge
docker-compose up
```

### Opción 2: Desde Docker Hub
```bash
git clone https://github.com/JesussPacheco/tenpo-calculator-challenge.git
cd tenpo-challenge
docker-compose -f docker-compose.remote.yml up
```

### Detener
```bash
docker-compose down
```

---

## Verificación

### Healthchecks
```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
```

### Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Postman Collection

Importar `tenpo-challenge.postman_collection.json` para pruebas rápidas.

---

## Endpoints

### POST /api/v1/calculator/sum
```bash
curl -X POST http://localhost:8080/api/v1/calculator/sum \
  -H "Content-Type: application/json" \
  -d '{"num1": 100, "num2": 200}'
```

**Response:**
```json
{
  "num1": 100.00,
  "num2": 200.00,
  "percentage": 8.45,
  "result": 325.35,
  "timestamp": "2025-10-16T03:15:00"
}
```

### GET /api/v1/history
```bash
curl "http://localhost:8080/api/v1/history?page=0&size=10"
```

**Parámetros:**
- `page`: número de página
- `size`: registros por página

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "endpoint": "/api/v1/calculator/sum",
      "method": "POST",
      "status": 200,
      "responseType": "SUCCESS",
      "requestParams": {"num1": 100, "num2": 200},
      "outcomeJson": {
        "num1": 100,
        "num2": 200,
        "percentage": "8.45",
        "result": "325.35",
        "timestamp": "2025-10-16T03:15:00"
      },
      "executedAt": "2025-10-16T03:15:00"
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

### GET /api/percentage
```bash
curl http://localhost:8081/api/percentage
```

**Response:**
```json
{
  "percentage": 12.34
}
```

---

## Testing
```bash
cd ms-tenpo-calculator
./gradlew test
./gradlew test jacocoTestReport
```

**Cobertura:** 84%

---

## Base de Datos
```
Host:     localhost
Port:     5434
Database: tenpo_calculator
User:     tenpo_user
Password: TenpoCh4ll3ng3!
```

---

## Variables de Entorno

Valores por defecto incluidos. Para personalizar:
```bash
cp .env.example .env
docker-compose up
```

---

## Troubleshooting

### Puerto PostgreSQL

Configurado en 5434 para evitar conflicto con instalación local.

### Puerto ya asignado
```bash
netstat -ano | findstr :8080
```

### Out of memory

Docker Desktop → Settings → Resources → Memory (mínimo 4GB)

### Limpiar todo
```bash
docker-compose down -v
docker system prune -a
docker-compose up --build
```

---

## Imágenes Docker Hub

- `jessy25/tenpo-calculator:latest`
- `jessy25/tenpo-percentage:latest`
```bash
docker pull jessy25/tenpo-calculator:latest
docker pull jessy25/tenpo-percentage:latest
```

---

## Autor

**Jesús Pacheco**

Email: jesuspacheco0219@gmail.com  
GitHub: [@JesussPacheco](https://github.com/JesussPacheco)  
