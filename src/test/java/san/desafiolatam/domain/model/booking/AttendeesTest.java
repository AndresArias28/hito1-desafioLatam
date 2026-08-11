package san.desafiolatam.domain.model.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import san.desafiolatam.domain.exception.InvalidAttendeesException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttendeesTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    void shouldRejectAttendeesWhenValueIsNotPositive(int value) {
        // Act
        InvalidAttendeesException exception = assertThrows(
                InvalidAttendeesException.class,
                () -> new Attendees(value)
        );

        // Assert
        assertEquals(
                "The number of attendees is invalid",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateAttendeesWithPositiveValue() {
        // Act
        Attendees attendees = new Attendees(5);

        // Assert
        assertEquals(5, attendees.value());
    }
}
