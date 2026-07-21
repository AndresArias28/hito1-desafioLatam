package domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import san.desafiolatam.domain.Booking;
import san.desafiolatam.domain.Room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class BookingTest {

    @Test
    void shouldCreateBookingWithExpectedValues() {
        // Arrange
        Room room = new Room(
                "R1",
                "Meeting Room",
                10,
                true
        );

        LocalDateTime startTime = LocalDateTime.of(
                2026,
                8,
                10,
                9,
                0
        );

        LocalDateTime endTime = startTime.plusHours(1);

        // Act
        Booking booking = new Booking(
                "B1",
                room,
                startTime,
                endTime,
                5
        );

        // Assert
        assertEquals("B1", booking.getId());
        assertSame(room, booking.getRoom()); //comparacion de todo el objeto romm
                     //valor esperado, valor real
        assertEquals(startTime, booking.getStartTime()); //Comprueba que el horario de inicio de esta reserva sea exactamente el que esperamos
        assertEquals(endTime, booking.getEndTime());
        assertEquals(5, booking.getAttendees());
    }

    @ParameterizedTest
    @MethodSource("overlapCases") //de donde obtiene los datos
    void shouldDetectOverlappingPeriods(
            LocalDateTime otherStart,
            LocalDateTime otherEnd,
            boolean expected
    ) {
        // Arrange
        Room room = new Room("R1", "Meeting Room", 10, true);

        LocalDateTime startTime = LocalDateTime.of(2026, 8, 10, 9, 0);

        LocalDateTime endTime = startTime.plusHours(1);

        Booking booking = new Booking(
                "B1",
                room,
                startTime,
                endTime,
                5
        );

        // Act
        boolean result = booking.overlaps(
                otherStart,
                otherEnd
        );

        // Assert
        assertEquals(expected, result);
    }

    static Stream<Arguments> overlapCases() {
        LocalDateTime base = LocalDateTime.of(2026, 8, 10, 9, 0);

        return Stream.of(
                Arguments.of(
                        base.minusMinutes(30),
                        base.plusMinutes(30),
                        true
                ),
                Arguments.of(
                        base.plusMinutes(30),
                        base.plusHours(2),
                        true
                ),
                Arguments.of(
                        base.minusHours(1),
                        base.plusHours(2),
                        true
                ),
                Arguments.of(
                        base.minusHours(1),
                        base,
                        false
                ),
                Arguments.of(
                        base.plusHours(1),
                        base.plusHours(2),
                        false
                )
        );
    }

    @Test
    void shouldIdentifyWhetherBookingBelongsToRoom() {
        // Arrange
        Room room = new Room("R1", "Meeting Room", 10, true);
        Booking booking = new Booking(
                "B1",
                room,
                LocalDateTime.of(2026, 8, 10, 9, 0),
                LocalDateTime.of(2026, 8, 10, 10, 0),
                5
        );

        // Act
        boolean result = booking.isForRoom("R1");

        // Assert
        assertEquals(true, result);
    }

    @Test
    void shouldCalculateBookingDurationInMinutes() {
        // Arrange
        Room room = new Room("R1", "Meeting Room", 10, true);
        Booking booking = new Booking(
                "B1",
                room,
                LocalDateTime.of(2026, 8, 10, 9, 0),
                LocalDateTime.of(2026, 8, 10, 10, 30),
                5
        );

        // Act
        long result = booking.durationInMinutes();

        // Assert
        assertEquals(90, result);
    }

}
