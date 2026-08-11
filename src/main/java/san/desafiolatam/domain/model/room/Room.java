package san.desafiolatam.domain.model.room;

import san.desafiolatam.domain.exception.InvalidAttendeesException;
import san.desafiolatam.domain.model.booking.Attendees;

import java.util.Objects;

// Aggregate root for the meeting-room catalog. Identity is defined solely by its RoomId.
public class Room {

    private final RoomId id;
    private final RoomName name;
    private final Capacity capacity;
    private final boolean active;

    public Room(
            RoomId id,
            RoomName name,
            Capacity capacity,
            boolean active
    ) {
        this.id = Objects.requireNonNull(id, "Room id must not be null");
        this.name = Objects.requireNonNull(
                name,
                "Room name must not be null"
        );
        this.capacity = Objects.requireNonNull(
                capacity,
                "Room capacity must not be null"
        );
        this.active = active;
    }

    public boolean canAccommodate(Attendees attendees) {
        return attendees.value() <= capacity.value();
    }

    public void ensureCanAccommodate(Attendees attendees) {
        if (!canAccommodate(attendees)) {
            throw new InvalidAttendeesException(
                    "The number of attendees is invalid"
            );
        }
    }

    public RoomId getId() {
        return id;
    }

    public RoomName getName() {
        return name;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public boolean isActive() {
        return active;
    }

    // Entity equality is based on identity, not on attributes.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room room)) {
            return false;
        }
        return id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
