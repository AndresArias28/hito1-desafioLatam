# Room Booking TDD

Core de negocio para gestionar reservas de salas de reuniones. El sistema valida la disponibilidad de la sala, el período de reserva y la cantidad de asistentes antes de crear una reserva.

## Arquitectura

El proyecto aplica un enfoque de Clean Architecture / Ports and Adapters:

- `domain`: entidades puras de Java (`Room` y `Booking`) y sus reglas de negocio.
- `service`: caso de uso `BookingService`, que coordina las validaciones y la creación de reservas.
- `repository`: puerto `BookingRepository`; solo define el contrato requerido por el servicio.
- `exception`: excepciones de negocio específicas.

El core no usa frameworks, base de datos ni repositorios en memoria. Durante las pruebas, el puerto `BookingRepository` se aísla usando Mockito.

## Reglas de negocio

- Una sala debe tener capacidad mayor que cero.
- Solo se puede reservar una sala activa.
- La hora de inicio debe ser anterior a la hora de término.
- La cantidad de asistentes debe estar entre uno y la capacidad de la sala.
- No se permiten reservas solapadas para la misma sala.

## Métodos principales

- `Room.canAccommodate(int attendees)`
- `Booking.overlaps(LocalDateTime otherStart, LocalDateTime otherEnd)`
- `Booking.isForRoom(String roomId)`
- `Booking.durationInMinutes()`
- `BookingService.createBooking(...)`

## Tecnologías

- Java 25
- Maven
- JUnit 5
- Mockito
- JaCoCo

## Ejecutar las pruebas

Desde la raíz del proyecto, ejecuta:

```bash
mvn clean test
```

## Generar el reporte de cobertura

Ejecuta:

```bash
mvn verify
```

El reporte HTML se genera en:

```text
target/site/jacoco/index.html
```

La suite actual cubre el 100% de las líneas y ramas de la lógica analizada por JaCoCo.
