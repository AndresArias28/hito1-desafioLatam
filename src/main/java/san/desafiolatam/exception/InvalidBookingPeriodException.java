package san.desafiolatam.exception;

public class InvalidBookingPeriodException extends RuntimeException{
    public InvalidBookingPeriodException(String message) {
        super(message);
    }
}
