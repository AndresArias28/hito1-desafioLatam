package san.desafiolatam.repository;

import san.desafiolatam.domain.Booking;
import java.time.LocalDateTime;

public interface BookingRepository {
    boolean existsOverlappingBooking(
            String roomId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    Booking save(Booking booking);
}
