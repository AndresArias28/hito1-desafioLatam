package san.desafiolatam.domain.entity;

import san.desafiolatam.domain.valueobject.Attendees;
import san.desafiolatam.domain.valueobject.BookingId;
import san.desafiolatam.domain.valueobject.BookingPeriod;
import san.desafiolatam.domain.valueobject.RoomId;

import java.util.Objects;

// Aggregate root for reservations. Identity is defined solely by its BookingId.
// Cross-aggregate references are held by identity (RoomId), never by entity instance.
public class Booking {

    private final BookingId id;
    private final RoomId roomId;
    private final BookingPeriod period;
    private final Attendees attendees;

    public Booking(
            BookingId id,
            RoomId roomId,
            BookingPeriod period,
            Attendees attendees
    ) {
        this.id = Objects.requireNonNull(
                id,
                "Booking id must not be null"
        );
        this.roomId = Objects.requireNonNull(
                roomId,
                "Room id must not be null"
        );
        this.period = Objects.requireNonNull(
                period,
                "Booking period must not be null"
        );
        this.attendees = Objects.requireNonNull(
                attendees,
                "Attendees must not be null"
        );
    }

    public BookingId id() {
        return id;
    }

    public RoomId roomId() {
        return roomId;
    }

    public BookingPeriod period() {
        return period;
    }

    public Attendees attendees() {
        return attendees;
    }

    public boolean isForRoom(RoomId roomId) {
        return this.roomId.equals(roomId);
    }

    public boolean overlaps(BookingPeriod otherPeriod) {
        return period.overlaps(otherPeriod);
    }

    public long durationInMinutes() {
        return period.durationInMinutes();
    }

    // Entity equality is based on identity, not on attributes.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking booking)) {
            return false;
        }
        return id.equals(booking.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
