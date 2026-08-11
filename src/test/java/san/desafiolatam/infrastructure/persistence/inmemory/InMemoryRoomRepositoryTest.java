package san.desafiolatam.infrastructure.persistence.inmemory;

import org.junit.jupiter.api.Test;
import san.desafiolatam.domain.model.room.Capacity;
import san.desafiolatam.domain.model.room.Room;
import san.desafiolatam.domain.model.room.RoomId;
import san.desafiolatam.domain.model.room.RoomName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryRoomRepositoryTest {

    private final InMemoryRoomRepository repository =
            new InMemoryRoomRepository();

    @Test
    void shouldSaveAndFindRoomById() {
        // Arrange
        RoomId roomId = new RoomId("R1");
        Room room = new Room(
                roomId,
                new RoomName("Meeting Room"),
                new Capacity(10),
                true
        );

        // Act
        Room saved = repository.save(room);
        Optional<Room> found = repository.findById(roomId);

        // Assert
        assertSame(room, saved);
        assertTrue(found.isPresent());
        assertSame(room, found.get());
    }

    @Test
    void shouldReturnEmptyWhenRoomDoesNotExist() {
        // Act
        Optional<Room> found = repository.findById(new RoomId("R99"));

        // Assert
        assertTrue(found.isEmpty());
    }
}
