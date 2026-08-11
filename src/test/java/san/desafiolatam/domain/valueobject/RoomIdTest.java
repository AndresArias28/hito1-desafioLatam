package san.desafiolatam.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoomIdTest {

    @Test
    void shouldRejectNullValue() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoomId(null)
        );

        // Assert
        assertEquals(
                "Room id must not be null or blank",
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankValue(String value) {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoomId(value)
        );

        // Assert
        assertEquals(
                "Room id must not be null or blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateRoomIdWithValidValue() {
        // Act
        RoomId roomId = new RoomId("R1");

        // Assert
        assertEquals("R1", roomId.value());
    }
}
