package san.desafiolatam.domain.valueobject;

import san.desafiolatam.domain.exception.InvalidBookingPeriodException;

import java.time.Duration;
import java.time.LocalDateTime;

public record BookingPeriod(LocalDateTime start, LocalDateTime end) {

    public BookingPeriod {
        if (start == null || end == null) {
            throw new InvalidBookingPeriodException(
                    "Start time and end time must not be null"
            );
        }

        if (!start.isBefore(end)) {
            throw new InvalidBookingPeriodException(
                    "Start time must be before end time"
            );
        }
    }

    // Checks whether this period overlaps the given one.
    public boolean overlaps(BookingPeriod other) {
        return start.isBefore(other.end) && end.isAfter(other.start);
    }

    public long durationInMinutes() {
        return Duration.between(start, end).toMinutes();
    }
}
