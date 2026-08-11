package san.desafiolatam.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoomNameTest {

    @Test
    void shouldRejectNullValue() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoomName(null)
        );

        // Assert
        assertEquals(
                "Room name must not be null or blank",
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankValue(String value) {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoomName(value)
        );

        // Assert
        assertEquals(
                "Room name must not be null or blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateRoomNameWithValidValue() {
        // Act
        RoomName roomName = new RoomName("Meeting Room");

        // Assert
        assertEquals("Meeting Room", roomName.value());
    }
}
