
# 🛠️ Proyecto Integrador

Este es un sistema backend construido con **Java + Spring Boot** para gestión de usuarios y seguridad, y un módulo de analítica con **Python + Pandas**. Implementa autenticación segura con **JWT almacenado en cookies HttpOnly**, control de acceso basado en roles, y una arquitectura modular preparada para crecimiento.

---

## 🚀 Tecnologías utilizadas

### 🔧 Backend
- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- JPA / Hibernate
- MySQL
- Springdoc OpenAPI (Swagger)

### 📊 Analítica
- Python 3
- Pandas

---

## 📁 Estructura del proyecto

```
/backend
  └── src/main/java/com/proyecto/integrador
        ├── controller
        ├── model
        ├── repository
        ├── security
        ├── service
        └── config
```

---

## 🔐 Seguridad y autenticación

- Autenticación con JWT (generado en backend).
- Token entregado como cookie `HttpOnly`, `Secure`, `SameSite=None`.
- Control de acceso mediante roles (`hasRole("ADMIN")`, `hasRole("USER")`).
- Rutas protegidas (`/admin/**`, `/user/**`).
- Logout que elimina la cookie del navegador.

---

## 🧪 Cómo probar localmente

### 1. Clonar el repositorio

```bash
git clone https://github.com/tuusuario/proyecto-integrador.git
cd proyecto-integrador
```

### 2. Backend

```bash
cd backend
./mvnw spring-boot:run
```

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 3. Python (analítica)

```bash
cd python
python scripts/analitica.py
```

---

## ✅ Endpoints principales

| Método | Ruta              | Descripción                         | Acceso      |
|--------|-------------------|-------------------------------------|-------------|
| POST   | `/auth/login`     | Login, genera cookie JWT            | Público     |
| POST   | `/auth/logout`    | Logout, elimina cookie JWT          | Autenticado |
| GET    | `/admin/privado`  | Prueba de acceso para admins        | ADMIN       |
| GET    | `/user/perfil`    | Perfil del usuario autenticado      | USER/ADMIN  |

---

## ⚙️ Variables importantes

### `application.properties` (backend)

```properties
security.jwt.secret=clave_secreta_segura_de_32_bytes
spring.jpa.open-in-view=false
spring.datasource.url=jdbc:mysql://localhost:3306/integrador
```

---

## 🧔 Autor

- **Juan Camilo Serna Madrid**
- [GitHub](https://github.com/uancamilo)

---

## 📝 Licencia

Este proyecto está licenciado bajo la licencia MIT.