package san.desafiolatam.service;

import san.desafiolatam.domain.Booking;
import san.desafiolatam.domain.Room;
import san.desafiolatam.exception.InactiveRoomException;
import san.desafiolatam.exception.InvalidAttendeesException;
import san.desafiolatam.exception.InvalidBookingPeriodException;
import san.desafiolatam.exception.RoomUnavailableException;
import san.desafiolatam.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(
            Room room,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int attendees
    ) {
        if (!room.isActive()) {
            throw new InactiveRoomException(
                    "The room is inactive"
            );
        }

        if (!startTime.isBefore(endTime)) {
            throw new InvalidBookingPeriodException(
                    "Start time must be before end time"
            );
        }

        if (!room.canAccommodate(attendees)) {
            throw new InvalidAttendeesException(
                    "The number of attendees is invalid"
            );
        }

        boolean overlappingBooking =
                bookingRepository.existsOverlappingBooking(
                        room.getId(),
                        startTime,
                        endTime
                );

        if (overlappingBooking) {
            throw new RoomUnavailableException(
                    "The room is unavailable for this period"
            );
        }

        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                room,
                startTime,
                endTime,
                attendees
        );

        return bookingRepository.save(booking);
    }
}
