package san.desafiolatam.domain.model.room;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import san.desafiolatam.domain.exception.InvalidAttendeesException;
import san.desafiolatam.domain.model.booking.Attendees;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoomTest {

    private Room createRoom() {
        return new Room(
                new RoomId("R1"),
                new RoomName("Meeting Room"),
                new Capacity(10),
                true
        );
    }

    @Test
    void shouldCreateRoomWithExpectedValues() {
        // Act
        Room room = createRoom();

        // Assert
        assertEquals(new RoomId("R1"), room.getId());
        assertEquals(new RoomName("Meeting Room"), room.getName());
        assertEquals(new Capacity(10), room.getCapacity());
        assertTrue(room.isActive());
    }

    @Test
    void shouldRejectRoomWhenIdIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Room(
                        null,
                        new RoomName("Meeting Room"),
                        new Capacity(10),
                        true
                )
        );

        // Assert
        assertEquals("Room id must not be null", exception.getMessage());
    }

    @Test
    void shouldRejectRoomWhenNameIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Room(
                        new RoomId("R1"),
                        null,
                        new Capacity(10),
                        true
                )
        );

        // Assert
        assertEquals("Room name must not be null", exception.getMessage());
    }

    @Test
    void shouldRejectRoomWhenCapacityIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Room(
                        new RoomId("R1"),
                        new RoomName("Meeting Room"),
                        null,
                        true
                )
        );

        // Assert
        assertEquals(
                "Room capacity must not be null",
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "5, true",
            "10, true",
            "11, false"
    })
    void shouldValidateWhetherRoomCanAccommodateAttendees(
            int attendees,
            boolean expected
    ) {
        // Arrange
        Room room = createRoom();

        // Act
        boolean result = room.canAccommodate(new Attendees(attendees));

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowWhenAttendeesExceedRoomCapacity() {
        // Arrange
        Room room = createRoom();

        // Act
        InvalidAttendeesException exception = assertThrows(
                InvalidAttendeesException.class,
                () -> room.ensureCanAccommodate(new Attendees(11))
        );

        // Assert
        assertEquals(
                "The number of attendees is invalid",
                exception.getMessage()
        );
    }

    @Test
    void shouldPassWhenAttendeesFitRoomCapacity() {
        // Arrange
        Room room = createRoom();

        // Act / Assert
        assertDoesNotThrow(
                () -> room.ensureCanAccommodate(new Attendees(10))
        );
    }

    @Test
    void shouldBeEqualWhenIdentityIsTheSame() {
        // Arrange
        Room room = createRoom();
        Room sameIdentity = new Room(
                new RoomId("R1"),
                new RoomName("Other Name"),
                new Capacity(20),
                false
        );

        // Act / Assert
        assertEquals(room, sameIdentity);
        assertEquals(room.hashCode(), sameIdentity.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdentityDiffers() {
        // Arrange
        Room room = createRoom();
        Room other = new Room(
                new RoomId("R2"),
                new RoomName("Meeting Room"),
                new Capacity(10),
                true
        );

        // Act / Assert
        assertNotEquals(room, other);
    }

    @Test
    void shouldBeEqualToItself() {
        // Arrange
        Room room = createRoom();

        // Act / Assert
        assertEquals(room, room);
    }

    @Test
    void shouldNotBeEqualToNullOrAnotherType() {
        // Arrange
        Room room = createRoom();

        // Act / Assert
        assertNotEquals(room, null);
        assertNotEquals(room, "R1");
    }
}
