package san.desafiolatam.domain.model.room;

public record RoomId(String value) {

    public RoomId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Room id must not be null or blank"
            );
        }
    }
}
