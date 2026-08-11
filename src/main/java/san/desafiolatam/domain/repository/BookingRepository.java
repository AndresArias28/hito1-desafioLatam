package san.desafiolatam.domain.repository;

import san.desafiolatam.domain.entity.Booking;
import san.desafiolatam.domain.valueobject.BookingPeriod;
import san.desafiolatam.domain.valueobject.RoomId;

// Storage boundary (port) for the Booking aggregate. Pure contract: no framework imports or annotations.
public interface BookingRepository {

    boolean existsOverlappingBooking(
            RoomId roomId,
            BookingPeriod period
    );

    Booking save(Booking booking);
}
