# Room Booking TDD

Business core for managing meeting-room reservations. The system validates room availability, the booking period, and the number of attendees before creating a booking.

## Architecture

The project follows a Clean Architecture / Ports and Adapters approach:

- `domain`: pure Java entities (`Room` and `Booking`) and their business rules.
- `service`: the `BookingService` use case, which coordinates validation and booking creation.
- `repository`: the `BookingRepository` port, which defines the contract required by the service.
- `exception`: specific business exceptions.

The core does not use frameworks, a database, or in-memory repositories. During tests, the `BookingRepository` port is isolated with Mockito.

## Business Rules

- A room must have a capacity greater than zero.
- Only active rooms can be booked.
- The start time must be before the end time.
- The number of attendees must be between one and the room capacity.
- Overlapping bookings are not allowed for the same room.

## Main Methods

- `Room.canAccommodate(int attendees)`
- `Booking.overlaps(LocalDateTime otherStart, LocalDateTime otherEnd)`
- `Booking.isForRoom(String roomId)`
- `Booking.durationInMinutes()`
- `BookingService.createBooking(...)`

## Technologies

- Java 25
- Maven
- JUnit 5
- Mockito
- JaCoCo

## Run the Tests

From the project root, run:

```bash
mvn clean test
```

## Generate the Coverage Report

Run:

```bash
mvn verify
```

The HTML report is generated at:

```text
target/site/jacoco/index.html
```

The current suite covers 100% of the lines and branches analyzed by JaCoCo.
