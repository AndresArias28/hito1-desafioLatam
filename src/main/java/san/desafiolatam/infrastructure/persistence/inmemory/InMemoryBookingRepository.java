package san.desafiolatam.infrastructure.persistence.inmemory;

import san.desafiolatam.domain.model.booking.Booking;
import san.desafiolatam.domain.model.booking.BookingPeriod;
import san.desafiolatam.domain.model.room.RoomId;
import san.desafiolatam.domain.repository.BookingRepository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Framework-free adapter implementing the BookingRepository port with an in-memory list.
public class InMemoryBookingRepository implements BookingRepository {

    private final List<Booking> bookings = new CopyOnWriteArrayList<>();

    @Override
    public boolean existsOverlappingBooking(
            RoomId roomId,
            BookingPeriod period
    ) {
        return bookings.stream().anyMatch(
                booking -> booking.isForRoom(roomId)
                        && booking.overlaps(period)
        );
    }

    @Override
    public Booking save(Booking booking) {
        bookings.add(booking);
        return booking;
    }
}
