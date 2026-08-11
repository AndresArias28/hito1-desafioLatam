package san.desafiolatam.infrastructure.persistence.inmemory;

import org.junit.jupiter.api.Test;
import san.desafiolatam.domain.entity.Booking;
import san.desafiolatam.domain.valueobject.Attendees;
import san.desafiolatam.domain.valueobject.BookingId;
import san.desafiolatam.domain.valueobject.BookingPeriod;
import san.desafiolatam.domain.valueobject.RoomId;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryBookingRepositoryTest {

    private final InMemoryBookingRepository repository =
            new InMemoryBookingRepository();

    private Booking createBooking(
            String bookingId,
            String roomId,
            int startHour,
            int endHour
    ) {
        return new Booking(
                new BookingId(bookingId),
                new RoomId(roomId),
                new BookingPeriod(
                        LocalDateTime.of(2026, 8, 10, startHour, 0),
                        LocalDateTime.of(2026, 8, 10, endHour, 0)
                ),
                new Attendees(5)
        );
    }

    private BookingPeriod period(int startHour, int startMinute, int endHour) {
        return new BookingPeriod(
                LocalDateTime.of(2026, 8, 10, startHour, startMinute),
                LocalDateTime.of(2026, 8, 10, endHour, 0)
        );
    }

    @Test
    void shouldSaveAndReturnBooking() {
        // Arrange
        Booking booking = createBooking("B1", "R1", 9, 10);

        // Act
        Booking saved = repository.save(booking);

        // Assert
        assertSame(booking, saved);
    }

    @Test
    void shouldDetectOverlapForSameRoom() {
        // Arrange
        repository.save(createBooking("B1", "R1", 9, 10));

        // Act
        boolean result = repository.existsOverlappingBooking(
                new RoomId("R1"),
                period(9, 30, 11)
        );

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldNotDetectOverlapWhenPeriodIsDisjointForSameRoom() {
        // Arrange
        repository.save(createBooking("B1", "R1", 9, 10));

        // Act
        boolean result = repository.existsOverlappingBooking(
                new RoomId("R1"),
                period(10, 0, 11)
        );

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldNotDetectOverlapWhenBookingBelongsToAnotherRoom() {
        // Arrange
        repository.save(createBooking("B1", "R1", 9, 10));

        // Act
        boolean result = repository.existsOverlappingBooking(
                new RoomId("R2"),
                period(9, 30, 11)
        );

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldNotDetectOverlapWhenRepositoryIsEmpty() {
        // Act
        boolean result = repository.existsOverlappingBooking(
                new RoomId("R1"),
                period(9, 0, 10)
        );

        // Assert
        assertFalse(result);
    }
}
