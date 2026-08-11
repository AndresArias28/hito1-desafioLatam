package san.desafiolatam.infrastructure.persistence.inmemory;

import san.desafiolatam.domain.entity.Room;
import san.desafiolatam.domain.valueobject.RoomId;
import san.desafiolatam.domain.repository.RoomRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Framework-free adapter implementing the RoomRepository port with an in-memory map.
public class InMemoryRoomRepository implements RoomRepository {

    private final Map<RoomId, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public Optional<Room> findById(RoomId roomId) {
        return Optional.ofNullable(rooms.get(roomId));
    }

    @Override
    public Room save(Room room) {
        rooms.put(room.id(), room);
        return room;
    }
}
