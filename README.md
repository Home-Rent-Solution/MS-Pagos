# MS-Pagos

Microservicio encargado de la gestión de pagos dentro del sistema **HomeRentSolution**.

## Funcionalidades
- Registrar pagos
- Consultar pagos
- Eliminar pagos
- Buscar pagos por inquilino
- Consultar pagos mediante HATEOAS
- Cancelar pagos

## Endpoints Principales

### API V1
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/pagos` | Crear un pago |
| GET | `/api/v1/pagos` | Obtener todos los pagos |
| GET | `/api/v1/pagos/recibo/{idPago}` | Obtener pago por ID |
| GET | `/api/v1/pagos/cuenta/inquilino/{idInquilino}` | Obtener pagos por inquilino |
| DELETE | `/api/v1/pagos/{id}` | Eliminar un pago |

### API V2 (HATEOAS)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v2/pagos` | Obtener todos los pagos con enlaces |
| GET | `/api/v2/pagos/recibo/{idPago}` | Obtener pago por ID con enlaces |

## Documentación API
Swagger UI disponible en: `http://localhost:8085/doc/swagger-ui.html`

## Integraciones
Este microservicio se comunica mediante:
- **OpenFeign** con MS-Reservas y MS-Inquilinos
- **RabbitMQ** para eventos de creación y cancelación de pagos
- **Eureka** para registro y descubrimiento de servicios

## Tecnologías
- Java 21
- Spring Boot
- Spring Data JPA
- Spring HATEOAS
- Spring Cloud OpenFeign
- Spring Cloud Netflix Eureka Client
- RabbitMQ (AMQP)
- MySQL
- OpenAPI / Swagger
- Lombok
- Docker
- Maven
