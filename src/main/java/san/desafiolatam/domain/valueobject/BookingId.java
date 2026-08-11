package san.desafiolatam.domain.valueobject;

public record BookingId(String value) {

    public BookingId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Booking id must not be null or blank"
            );
        }
    }
}
