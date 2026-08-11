package san.desafiolatam.domain.repository;

import san.desafiolatam.domain.model.booking.Booking;
import san.desafiolatam.domain.model.booking.BookingPeriod;
import san.desafiolatam.domain.model.room.RoomId;

// Storage boundary (port) for the Booking aggregate. Pure contract: no framework imports or annotations.
public interface BookingRepository {

    boolean existsOverlappingBooking(
            RoomId roomId,
            BookingPeriod period
    );

    Booking save(Booking booking);
}
