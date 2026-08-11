package san.desafiolatam.domain.valueobject;

public record RoomName(String value) {

    public RoomName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Room name must not be null or blank"
            );
        }
    }
}
