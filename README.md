MS-Pagos

Funcionalidades
Registrar pagos
Consultar pagos
Actualizar pagos
Eliminar seguros
Buscar pagos 
Buscar pagos por reserva
Consultar pagos mediante HATEOAS
Endpoints Principales

Obtener todos los seguros
GET /api/v1/seguros

Obtener pagos por ID
GET /api/v1/seguros/{id}

Crear pagos
POST /api/v1/pagos

Actualizar pagos
PUT /api/v1/pagos/{id}

Eliminar pagos
DELETE /api/v1/pagos/{id}

Buscar pagos
GET /api/v1/pagos

Buscar por reserva
GET /api/v1/pagos/reserva/{id}
HATEOAS
Obtener todos los seguros con enlaces
GET /api/v2/pagos

Obtener seguro por ID con enlaces
GET /api/v2/pagos/{id}
Integraciones
Este microservicio utiliza OpenFeign para comunicarse con:

MS-Reservas
MS-Inquilinos
Tecnologías
Java 25
Lombok
Spring Boot
Spring Data JPA
SpringDoc
RabbitMQ
MySQL
OpenAPI / Swagger
OpenFeign
HATEOAS
Maven
