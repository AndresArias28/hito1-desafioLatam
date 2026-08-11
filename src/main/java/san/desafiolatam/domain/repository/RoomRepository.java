package san.desafiolatam.domain.repository;

import san.desafiolatam.domain.entity.Room;
import san.desafiolatam.domain.valueobject.RoomId;

import java.util.Optional;

// Storage boundary (port) for the Room aggregate. Pure contract: no framework imports or annotations.
public interface RoomRepository {

    Optional<Room> findById(RoomId roomId);

    Room save(Room room);
}
