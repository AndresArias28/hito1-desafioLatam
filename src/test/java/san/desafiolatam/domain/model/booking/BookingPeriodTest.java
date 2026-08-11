package san.desafiolatam.domain.model.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import san.desafiolatam.domain.exception.InvalidBookingPeriodException;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingPeriodTest {

    private static final LocalDateTime BASE =
            LocalDateTime.of(2026, 8, 10, 9, 0);

    @Test
    void shouldRejectPeriodWhenStartIsNull() {
        // Act
        InvalidBookingPeriodException exception = assertThrows(
                InvalidBookingPeriodException.class,
                () -> new BookingPeriod(null, BASE.plusHours(1))
        );

        // Assert
        assertEquals(
                "Start time and end time must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectPeriodWhenEndIsNull() {
        // Act
        InvalidBookingPeriodException exception = assertThrows(
                InvalidBookingPeriodException.class,
                () -> new BookingPeriod(BASE, null)
        );

        // Assert
        assertEquals(
                "Start time and end time must not be null",
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPeriods")
    void shouldRejectPeriodWhenStartIsNotBeforeEnd(
            LocalDateTime start,
            LocalDateTime end
    ) {
        // Act
        InvalidBookingPeriodException exception = assertThrows(
                InvalidBookingPeriodException.class,
                () -> new BookingPeriod(start, end)
        );

        // Assert
        assertEquals(
                "Start time must be before end time",
                exception.getMessage()
        );
    }

    static Stream<Arguments> invalidPeriods() {
        return Stream.of(
                Arguments.of(BASE, BASE),
                Arguments.of(BASE.plusHours(1), BASE)
        );
    }

    @ParameterizedTest
    @MethodSource("overlapCases")
    void shouldDetectOverlappingPeriods(
            LocalDateTime otherStart,
            LocalDateTime otherEnd,
            boolean expected
    ) {
        // Arrange
        BookingPeriod period = new BookingPeriod(BASE, BASE.plusHours(1));

        // Act
        boolean result = period.overlaps(
                new BookingPeriod(otherStart, otherEnd)
        );

        // Assert
        assertEquals(expected, result);
    }

    static Stream<Arguments> overlapCases() {
        return Stream.of(
                Arguments.of(
                        BASE.minusMinutes(30),
                        BASE.plusMinutes(30),
                        true
                ),
                Arguments.of(
                        BASE.plusMinutes(30),
                        BASE.plusHours(2),
                        true
                ),
                Arguments.of(
                        BASE.minusHours(1),
                        BASE.plusHours(2),
                        true
                ),
                Arguments.of(
                        BASE.minusHours(1),
                        BASE,
                        false
                ),
                Arguments.of(
                        BASE.plusHours(1),
                        BASE.plusHours(2),
                        false
                )
        );
    }

    @Test
    void shouldCalculateDurationInMinutes() {
        // Arrange
        BookingPeriod period = new BookingPeriod(BASE, BASE.plusMinutes(90));

        // Act
        long result = period.durationInMinutes();

        // Assert
        assertEquals(90, result);
    }

    @Test
    void shouldExposeStartAndEnd() {
        // Act
        BookingPeriod period = new BookingPeriod(BASE, BASE.plusHours(1));

        // Assert
        assertEquals(BASE, period.start());
        assertEquals(BASE.plusHours(1), period.end());
    }
}
