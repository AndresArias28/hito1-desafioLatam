package san.desafiolatam.application.usecase;

import san.desafiolatam.domain.exception.InactiveRoomException;
import san.desafiolatam.domain.exception.RoomNotFoundException;
import san.desafiolatam.domain.exception.RoomUnavailableException;
import san.desafiolatam.domain.model.booking.Attendees;
import san.desafiolatam.domain.model.booking.Booking;
import san.desafiolatam.domain.model.booking.BookingId;
import san.desafiolatam.domain.model.booking.BookingPeriod;
import san.desafiolatam.domain.model.room.Room;
import san.desafiolatam.domain.model.room.RoomId;
import san.desafiolatam.domain.repository.BookingRepository;
import san.desafiolatam.domain.repository.RoomRepository;

import java.util.UUID;

// Application use case: creates a booking for a room.
// Depends exclusively on the domain repository ports, injected through the constructor.
public class CreateBookingUseCase {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public CreateBookingUseCase(
            RoomRepository roomRepository,
            BookingRepository bookingRepository
    ) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public Booking execute(
            RoomId roomId,
            BookingPeriod period,
            Attendees attendees
    ) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(
                        "The room does not exist"
                ));

        if (!room.isActive()) {
            throw new InactiveRoomException(
                    "The room is inactive"
            );
        }

        room.ensureCanAccommodate(attendees);

        boolean overlappingBooking =
                bookingRepository.existsOverlappingBooking(
                        roomId,
                        period
                );

        if (overlappingBooking) {
            throw new RoomUnavailableException(
                    "The room is unavailable for this period"
            );
        }

        Booking booking = new Booking(
                new BookingId(UUID.randomUUID().toString()),
                roomId,
                period,
                attendees
        );

        return bookingRepository.save(booking);
    }
}
