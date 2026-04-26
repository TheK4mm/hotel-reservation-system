# Hotel Paraiso API

---

Sistema de Gestión de Reservas para el **Hotel Paraíso** — API REST completa desarrollada con **Spring Boot 3**, **Spring Data JPA** y **PostgreSQL**.

Proyecto académico que implementa arquitectura en capas, modelado de bases de datos relacionales con múltiples tipos de relaciones (1:1, 1:N, N:M), manejo global de excepciones y buenas prácticas de desarrollo backend.

## Descripción

---

El sistema centraliza la operación del Hotel Paraíso permitiendo:

- **Gestión de clientes** — Registro, búsqueda y actualización de huéspedes
- **Gestión de habitaciones** — Catálogo por tipo, disponibilidad por rango de fechas
- **Gestión de reservas** — Ciclo completo con máquina de estados (Pendiente → Confirmada → Check-in → Check-out)
- **Gestión de pagos** — Registro de pagos parciales con validación contra saldo pendiente
- **Facturación** — Generación automática con cálculo de IVA y descuentos
- **Servicios adicionales** — Spa, alimentación, transporte, lavandería, etc.
- **Control de empleados** — Recepcionistas y personal que gestiona las reservas

### Alcance

---

| Incluido | No incluido |
|----------|-------------|
| CRUD completo (8 entidades) | Autenticación JWT / OAuth2 |
| Control de disponibilidad por fechas | Reportes gráficos / Dashboard |
| Máquina de estados para reservas | Notificaciones email/SMS |
| Cálculo automático de precios | Integración con pasarelas de pago |
| Validación de pagos vs saldo | Módulo de inventario |
| Facturación con IVA y descuentos | Frontend / interfaz web |

---

## Tecnologías

| Componente | Tecnología |
|------------|-----------|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.2.4 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL 16 |
| Validaciones | Jakarta Validation (`@Valid`) |
| Utilidades | Lombok |
| Build | Maven |
| Pruebas API | Postman |

---

```

## Arquitectura

El proyecto sigue una **arquitectura en capas** con separación clara de responsabilidades:
┌──────────────────────────────────────────────────────┐
│            CLIENT (Postman / Frontend)               │
└────────────────────────┬─────────────────────────────┘
                         │ HTTP JSON
┌────────────────────────▼─────────────────────────────┐
│              CONTROLLER LAYER                        │
│  @RestController — Recibe requests, delega al service│
│  Valida con @Valid, retorna ResponseEntity           │
└────────────────────────┬─────────────────────────────┘
                         │ DTOs
┌────────────────────────▼─────────────────────────────┐
│               SERVICE LAYER                          │
│  @Service — Lógica de negocio, transacciones         │
│  Valida reglas, mapea entidades ↔ DTOs               │
└────────────────────────┬─────────────────────────────┘
                         │ Entities
┌────────────────────────▼─────────────────────────────┐
│             REPOSITORY LAYER                         │
│  @Repository — Spring Data JPA                       │
│  JPQL queries, métodos derivados                     │
└────────────────────────┬─────────────────────────────┘
                         │ SQL
┌────────────────────────▼─────────────────────────────┐
│              PostgreSQL DATABASE                     │
│  8 tablas principales + 2 tablas intermedias (N:M)   │
└──────────────────────────────────────────────────────┘

## Modelo de Datos

---

### Entidades

| Entidad | Descripción |
|---------|-------------|
| `Cliente` | Huéspedes del hotel con datos personales |
| `Empleado` | Personal del hotel que gestiona reservas |
| `TipoHabitacion` | Categorías (Individual, Doble, Suite, Familiar, etc.) |
| `Habitacion` | Habitaciones físicas con número, piso y estado |
| `Servicio` | Servicios adicionales (spa, alimentación, transporte) |
| `Reserva` | Entidad central que conecta clientes, habitaciones y servicios |
| `Pago` | Pagos parciales o totales asociados a una reserva |
| `Factura` | Documento fiscal con subtotal, IVA y descuentos |


## Maquina de Estados - Reserva

  PENDIENTE ──── confirmar ──→ CONFIRMADA ──── check-in ──→ CHECKIN ──── check-out ──→ CHECKOUT
      │               │                                          │
      └── cancelar ───┘                                    no_show ──→ NO_SHOW
                      └─────────────── cancelar ──────────────────┘

Las transiciones inválidas retornan HTTP 422 con mensaje descriptivo.

---

## Endpoints API REST

### Tipos de Habitación — `/tipos-habitacion`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/tipos-habitacion` | Crear tipo de habitación |
| `GET` | `/tipos-habitacion` | Listar todos |
| `GET` | `/tipos-habitacion/{id}` | Obtener por ID |
| `PUT` | `/tipos-habitacion/{id}` | Actualizar |
| `DELETE` | `/tipos-habitacion/{id}` | Desactivar (soft delete) |

### Habitaciones — `/habitaciones`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/habitaciones` | Crear habitación |
| `GET` | `/habitaciones` | Listar todas |
| `GET` | `/habitaciones/{id}` | Obtener por ID |
| `GET` | `/habitaciones/disponibles?entrada=YYYY-MM-DD&salida=YYYY-MM-DD` | Disponibilidad por fechas |
| `PUT` | `/habitaciones/{id}` | Actualizar |
| `DELETE` | `/habitaciones/{id}` | Desactivar |

### Clientes — `/clientes`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/clientes` | Registrar cliente |
| `GET` | `/clientes` | Listar todos |
| `GET` | `/clientes/{id}` | Obtener por ID |
| `GET` | `/clientes/search?termino=nombre` | Buscar por nombre/apellido |
| `PUT` | `/clientes/{id}` | Actualizar |
| `DELETE` | `/clientes/{id}` | Desactivar |

### Empleados — `/empleados`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/empleados` | Registrar empleado |
| `GET` | `/empleados` | Listar todos |
| `GET` | `/empleados/{id}` | Obtener por ID |
| `PUT` | `/empleados/{id}` | Actualizar |
| `DELETE` | `/empleados/{id}` | Desactivar |

### Servicios — `/servicios`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/servicios` | Crear servicio |
| `GET` | `/servicios` | Listar todos |
| `GET` | `/servicios/{id}` | Obtener por ID |
| `GET` | `/servicios/categoria/{categoria}` | Filtrar por categoría |
| `PUT` | `/servicios/{id}` | Actualizar |
| `DELETE` | `/servicios/{id}` | Desactivar |

### Reservas — `/reservas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/reservas` | Crear reserva |
| `GET` | `/reservas` | Listar todas |
| `GET` | `/reservas/{id}` | Obtener por ID |
| `GET` | `/reservas/codigo/{codigo}` | Buscar por código |
| `GET` | `/reservas/cliente/{clienteId}` | Reservas de un cliente |
| `GET` | `/reservas/estado/{estado}` | Filtrar por estado |
| `PUT` | `/reservas/{id}` | Actualizar |
| `PATCH` | `/reservas/{id}/estado` | Cambiar estado |
| `DELETE` | `/reservas/{id}` | Cancelar |

### Pagos — `/pagos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/pagos` | Registrar pago |
| `GET` | `/pagos` | Listar todos |
| `GET` | `/pagos/{id}` | Obtener por ID |
| `GET` | `/pagos/reserva/{reservaId}` | Pagos de una reserva |
| `PUT` | `/pagos/{id}` | Actualizar |
| `DELETE` | `/pagos/{id}` | Cancelar pago |

### Facturas — `/facturas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/facturas` | Generar factura |
| `GET` | `/facturas` | Listar todas |
| `GET` | `/facturas/{id}` | Obtener por ID |
| `GET` | `/facturas/reserva/{reservaId}` | Factura de una reserva |
| `PUT` | `/facturas/{id}` | Actualizar |
| `DELETE` | `/facturas/{id}` | Anular |

---

### Manejo de errores

El sistema retorna respuestas de error claras y estandarizadas:

| Código | Tipo | Ejemplo |
|--------|------|---------|
| `400` | Validación | Campos obligatorios faltantes, email inválido |
| `400` | Duplicado | Email o documento ya registrado |
| `404` | No encontrado | Recurso con ID inexistente |
| `422` | Regla de negocio | Habitación ya reservada, transición de estado inválida |
| `500` | Error interno | Error inesperado del servidor |

---

## Estructura del Proyecto

hotel-paraiso
├── pom.xml
├── db/
│   ├── database.sql
└── src/main/
    ├── resources/
    │   └── application.properties
    └── java/com/hotel/paraiso/
        ├── HotelParaisoApplication.java
        ├── model/             ← Entidades JPA
        │   ├── TipoHabitacion.java
        │   ├── Habitacion.java
        │   ├── Cliente.java
        │   ├── Empleado.java
        │   ├── Servicio.java
        │   ├── Reserva.java      ← Entidad central
        │   ├── Pago.java
        │   └── Factura.java
        ├── repository/        ← Spring Data JPA
        │   ├── TipoHabitacionRepository.java
        │   ├── HabitacionRepository.java
        │   ├── ClienteRepository.java
        │   ├── EmpleadoRepository.java
        │   ├── ServicioRepository.java
        │   ├── ReservaRepository.java
        │   ├── PagoRepository.java
        │   └── FacturaRepository.java
        ├── service/           ← Lógica de negocio
        │   ├── TipoHabitacionService.java
        │   ├── HabitacionService.java
        │   ├── ClienteService.java
        │   ├── EmpleadoService.java
        │   ├── ServicioService.java
        │   ├── ReservaService.java   ← Servicio más complejo
        │   ├── PagoService.java
        │   └── FacturaService.java
        ├── controller/        ← API REST
        │   ├── TipoHabitacionController.java
        │   ├── HabitacionController.java
        │   ├── ClienteController.java
        │   ├── EmpleadoController.java
        │   ├── ServicioController.java
        │   ├── ReservaController.java
        │   ├── PagoController.java
        │   └── FacturaController.java
        ├── dto/               ← Objetos de transferencia
        │   ├── TipoHabitacionDTO.java
        │   ├── HabitacionDTO.java
        │   ├── ClienteDTO.java
        │   ├── EmpleadoDTO.java
        │   ├── ServicioDTO.java
        │   ├── ReservaDTO.java
        │   ├── PagoDTO.java
        │   └── FacturaDTO.java
        └── exception/         ← Manejo de errores
            ├── ResourceNotFoundException.java
            ├── BadRequestException.java
            ├── BusinessException.java
            └── GlobalExceptionHandler.javaç

---

## 👤 Camilo

---
