# Spring Boot 2.7 (Java 11) + SQLite + JWT + Swagger + CRUD

**Características**
- Java 11, Spring Boot 2.7.18
- SQLite embebido (archivo `app.db`)
- Seguridad con JWT (login y registro)
- CRUD de `Todo`
- Documentación Swagger (springdoc 1.7.0) en `/swagger-ui/index.html`

## Cómo ejecutar
```bash
mvn spring-boot:run
```
o
```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## Endpoints
- `POST /api/auth/register` { "username", "password" } → devuelve `{ token }`
- `POST /api/auth/login` { "username", "password" } → devuelve `{ token }`
- `GET/POST/PUT/DELETE /api/todos` (requiere Header `Authorization: Bearer <token>`)

## Swagger
Abrir: `http://localhost:8080/swagger-ui/index.html`
En el botón **Authorize**, pegar `Bearer <token>`.

> Nota: La clave secreta JWT se genera en memoria al iniciar (solo para demo).
  En producción, cambia `JwtUtil` para leer la clave desde variables de entorno o un keystore.
