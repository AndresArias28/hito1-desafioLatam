package san.desafiolatam.domain.model.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingIdTest {

    @Test
    void shouldRejectNullValue() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BookingId(null)
        );

        // Assert
        assertEquals(
                "Booking id must not be null or blank",
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankValue(String value) {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BookingId(value)
        );

        // Assert
        assertEquals(
                "Booking id must not be null or blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateBookingIdWithValidValue() {
        // Act
        BookingId bookingId = new BookingId("B1");

        // Assert
        assertEquals("B1", bookingId.value());
    }
}
