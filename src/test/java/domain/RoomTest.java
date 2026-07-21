package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import san.desafiolatam.domain.Room;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void shouldRejectRoomWhenCapacityIsInvalid(int capacity) {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Room(
                        "R1",
                        "Meeting Room",
                        capacity,
                        true
                )
        );

        // Assert
        assertEquals(
                "Room capacity must be greater than zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateRoomWithExpectedValues() {
        // Arrange / Act
        Room room = new Room(
                "R1",
                "Meeting Room",
                10,
                true
        );

        // Assert
        assertEquals("R1", room.getId());
        assertEquals(10, room.getCapacity());
        assertTrue(room.isActive());
    }

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "5, true",
            "10, true",
            "0, false",
            "-1, false",
            "11, false"
    })
    void shouldValidateWhetherRoomCanAccommodateAttendees(
            int attendees,
            boolean expected
    ) {
        // Arrange
        Room room = new Room(
                "R1",
                "Meeting Room",
                10,
                true
        );

        // Act
        boolean result = room.canAccommodate(attendees);

        // Assert
        assertEquals(expected, result);
    }
}
