package san.desafiolatam.domain.model.room;

public record Capacity(int value) {

    public Capacity {
        if (value <= 0) {
            throw new IllegalArgumentException(
                    "Room capacity must be greater than zero"
            );
        }
    }
}
