package san.desafiolatam.domain.model.booking;

import san.desafiolatam.domain.exception.InvalidAttendeesException;

public record Attendees(int value) {

    public Attendees {
        if (value <= 0) {
            throw new InvalidAttendeesException(
                    "The number of attendees is invalid"
            );
        }
    }
}
