package san.desafiolatam.domain.entity;

import org.junit.jupiter.api.Test;
import san.desafiolatam.domain.valueobject.Attendees;
import san.desafiolatam.domain.valueobject.BookingId;
import san.desafiolatam.domain.valueobject.BookingPeriod;
import san.desafiolatam.domain.valueobject.RoomId;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingTest {

    private static final BookingId BOOKING_ID = new BookingId("B1");
    private static final RoomId ROOM_ID = new RoomId("R1");
    private static final BookingPeriod PERIOD = new BookingPeriod(
            LocalDateTime.of(2026, 8, 10, 9, 0),
            LocalDateTime.of(2026, 8, 10, 10, 30)
    );
    private static final Attendees ATTENDEES = new Attendees(5);

    private Booking createBooking() {
        return new Booking(BOOKING_ID, ROOM_ID, PERIOD, ATTENDEES);
    }

    @Test
    void shouldCreateBookingWithExpectedValues() {
        // Act
        Booking booking = createBooking();

        // Assert
        assertEquals(BOOKING_ID, booking.id());
        assertEquals(ROOM_ID, booking.roomId());
        assertEquals(PERIOD, booking.period());
        assertEquals(ATTENDEES, booking.attendees());
    }

    @Test
    void shouldRejectBookingWhenIdIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Booking(null, ROOM_ID, PERIOD, ATTENDEES)
        );

        // Assert
        assertEquals("Booking id must not be null", exception.getMessage());
    }

    @Test
    void shouldRejectBookingWhenRoomIdIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Booking(BOOKING_ID, null, PERIOD, ATTENDEES)
        );

        // Assert
        assertEquals("Room id must not be null", exception.getMessage());
    }

    @Test
    void shouldRejectBookingWhenPeriodIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Booking(BOOKING_ID, ROOM_ID, null, ATTENDEES)
        );

        // Assert
        assertEquals(
                "Booking period must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBookingWhenAttendeesIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Booking(BOOKING_ID, ROOM_ID, PERIOD, null)
        );

        // Assert
        assertEquals("Attendees must not be null", exception.getMessage());
    }

    @Test
    void shouldIdentifyWhetherBookingBelongsToRoom() {
        // Arrange
        Booking booking = createBooking();

        // Act / Assert
        assertTrue(booking.isForRoom(new RoomId("R1")));
        assertFalse(booking.isForRoom(new RoomId("R2")));
    }

    @Test
    void shouldDetectOverlapWithAnotherPeriod() {
        // Arrange
        Booking booking = createBooking();

        BookingPeriod overlapping = new BookingPeriod(
                LocalDateTime.of(2026, 8, 10, 10, 0),
                LocalDateTime.of(2026, 8, 10, 11, 0)
        );

        BookingPeriod disjoint = new BookingPeriod(
                LocalDateTime.of(2026, 8, 10, 11, 0),
                LocalDateTime.of(2026, 8, 10, 12, 0)
        );

        // Act / Assert
        assertTrue(booking.overlaps(overlapping));
        assertFalse(booking.overlaps(disjoint));
    }

    @Test
    void shouldCalculateDurationInMinutes() {
        // Arrange
        Booking booking = createBooking();

        // Act
        long result = booking.durationInMinutes();

        // Assert
        assertEquals(90, result);
    }

    @Test
    void shouldBeEqualWhenIdentityIsTheSame() {
        // Arrange
        Booking booking = createBooking();
        Booking sameIdentity = new Booking(
                BOOKING_ID,
                new RoomId("R9"),
                new BookingPeriod(
                        LocalDateTime.of(2026, 8, 11, 9, 0),
                        LocalDateTime.of(2026, 8, 11, 10, 0)
                ),
                new Attendees(2)
        );

        // Act / Assert
        assertEquals(booking, sameIdentity);
        assertEquals(booking.hashCode(), sameIdentity.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdentityDiffers() {
        // Arrange
        Booking booking = createBooking();
        Booking other = new Booking(
                new BookingId("B2"),
                ROOM_ID,
                PERIOD,
                ATTENDEES
        );

        // Act / Assert
        assertNotEquals(booking, other);
    }

    @Test
    void shouldBeEqualToItself() {
        // Arrange
        Booking booking = createBooking();

        // Act / Assert
        assertEquals(booking, booking);
    }

    @Test
    void shouldNotBeEqualToNullOrAnotherType() {
        // Arrange
        Booking booking = createBooking();

        // Act / Assert
        assertNotEquals(booking, null);
        assertNotEquals(booking, "B1");
    }
}
