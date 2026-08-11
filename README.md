# Room Booking TDD

Business core for managing meeting-room reservations. The system validates room availability, the booking period, and the number of attendees before creating a booking.

## Architecture

The project follows a Clean Architecture / Ports and Adapters approach with Domain-Driven Design tactical patterns, organized in three strictly separated layers:

```text
san.desafiolatam
├── domain                    (inner layer: pure Java, no framework imports or annotations)
│   ├── entity                (Room and Booking aggregate roots)
│   ├── valueobject           (RoomId, RoomName, Capacity, BookingId, BookingPeriod, Attendees)
│   ├── repository            (storage ports: BookingRepository, RoomRepository)
│   └── exception             (business exceptions)
├── application
│   └── usecase               (CreateBookingUseCase)
└── infrastructure
    └── persistence
        └── inmemory          (InMemoryRoomRepository, InMemoryBookingRepository adapters)
```

### Layer Rules

- **Domain**: contains the business core. It has zero dependencies on frameworks or on the outer layers.
- **Application**: orchestrates use cases. `CreateBookingUseCase` depends exclusively on the domain repository ports, injected through its constructor.
- **Infrastructure**: provides framework-free adapters that implement the domain ports (in-memory persistence).

Dependencies point inward only: `infrastructure` may depend on `application` and `domain`; `application` may depend on `domain`; `domain` never depends on the outer layers. The domain and application layers contain no Spring, JPA, Jackson, or other framework annotations/imports.

### Tactical Patterns

- **Entities with unique identity**: `Room` and `Booking` are aggregate roots; their `equals`/`hashCode` are based solely on their identity (`RoomId` / `BookingId`).
- **Self-validating Value Objects** (`record`): `RoomId`, `RoomName`, `Capacity`, `BookingId`, `BookingPeriod`, and `Attendees` enforce their invariants in their compact constructors.
- **Aggregates**: `Booking` references the `Room` aggregate by identity (`RoomId`), never by entity instance.
- **Repository contracts**: pure interfaces living inside the domain (`domain.repository`) act as storage boundaries; infrastructure implements them.

## Business Rules

- A room must have a capacity greater than zero.
- Only active rooms can be booked.
- The start time must be before the end time.
- The number of attendees must be between one and the room capacity.
- Overlapping bookings are not allowed for the same room.

## Main Methods

- `Room.canAccommodate(Attendees attendees)`
- `Room.ensureCanAccommodate(Attendees attendees)`
- `Room.id()`, `Room.name()`, `Room.capacity()`
- `Booking.overlaps(BookingPeriod otherPeriod)`
- `Booking.isForRoom(RoomId roomId)`
- `Booking.id()`, `Booking.roomId()`, `Booking.period()`, `Booking.attendees()`
- `BookingPeriod.overlaps(BookingPeriod other)`
- `BookingPeriod.durationInMinutes()`
- `CreateBookingUseCase.execute(RoomId roomId, BookingPeriod period, Attendees attendees)`

## Technologies

- Java 25
- Maven
- JUnit 5
- Mockito
- JaCoCo

## Run the Tests

From the project root, run:

```bash
mvn clean compile
mvn test
```

## Generate the Coverage Report

Run the full verification, including the test suite:

```bash
mvn verify
```

The HTML report is generated at:

```text
target/site/jacoco/index.html
```

The current suite covers 100% of the lines and branches analyzed by JaCoCo.
