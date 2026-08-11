package san.desafiolatam.domain.exception;

public class InvalidBookingPeriodException extends RuntimeException {
    public InvalidBookingPeriodException(String message) {
        super(message);
    }
}
