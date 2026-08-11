package san.desafiolatam.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import san.desafiolatam.domain.exception.InactiveRoomException;
import san.desafiolatam.domain.exception.InvalidAttendeesException;
import san.desafiolatam.domain.exception.RoomNotFoundException;
import san.desafiolatam.domain.exception.RoomUnavailableException;
import san.desafiolatam.domain.model.booking.Attendees;
import san.desafiolatam.domain.model.booking.Booking;
import san.desafiolatam.domain.model.booking.BookingPeriod;
import san.desafiolatam.domain.model.room.Capacity;
import san.desafiolatam.domain.model.room.Room;
import san.desafiolatam.domain.model.room.RoomId;
import san.desafiolatam.domain.model.room.RoomName;
import san.desafiolatam.domain.repository.BookingRepository;
import san.desafiolatam.domain.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBookingUseCaseTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    private CreateBookingUseCase useCase;

    private RoomId roomId;
    private BookingPeriod period;
    private Attendees attendees;

    @BeforeEach
    void setUp() {
        useCase = new CreateBookingUseCase(
                roomRepository,
                bookingRepository
        );

        roomId = new RoomId("R1");

        period = new BookingPeriod(
                LocalDateTime.of(2026, 8, 10, 9, 0),
                LocalDateTime.of(2026, 8, 10, 10, 0)
        );

        attendees = new Attendees(5);
    }

    private Room activeRoom() {
        return new Room(
                roomId,
                new RoomName("Meeting Room"),
                new Capacity(10),
                true
        );
    }

    @Test
    void shouldRejectBookingWhenRoomDoesNotExist() {
        // Arrange
        when(roomRepository.findById(roomId))
                .thenReturn(Optional.empty());

        // Act
        RoomNotFoundException exception = assertThrows(
                RoomNotFoundException.class,
                () -> useCase.execute(roomId, period, attendees)
        );

        // Assert
        assertEquals("The room does not exist", exception.getMessage());
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void shouldRejectBookingWhenRoomIsInactive() {
        // Arrange
        Room room = new Room(
                roomId,
                new RoomName("Meeting Room"),
                new Capacity(10),
                false
        );

        when(roomRepository.findById(roomId))
                .thenReturn(Optional.of(room));

        // Act
        InactiveRoomException exception = assertThrows(
                InactiveRoomException.class,
                () -> useCase.execute(roomId, period, attendees)
        );

        // Assert
        assertEquals("The room is inactive", exception.getMessage());
        verifyNoInteractions(bookingRepository);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 20})
    void shouldRejectBookingWhenAttendeesExceedRoomCapacity(
            int attendeesCount
    ) {
        // Arrange
        when(roomRepository.findById(roomId))
                .thenReturn(Optional.of(activeRoom()));

        // Act
        InvalidAttendeesException exception = assertThrows(
                InvalidAttendeesException.class,
                () -> useCase.execute(
                        roomId,
                        period,
                        new Attendees(attendeesCount)
                )
        );

        // Assert
        assertEquals(
                "The number of attendees is invalid",
                exception.getMessage()
        );
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void shouldRejectBookingWhenRoomHasOverlappingBooking() {
        // Arrange
        when(roomRepository.findById(roomId))
                .thenReturn(Optional.of(activeRoom()));

        when(bookingRepository.existsOverlappingBooking(roomId, period))
                .thenReturn(true);

        // Act
        RoomUnavailableException exception = assertThrows(
                RoomUnavailableException.class,
                () -> useCase.execute(roomId, period, attendees)
        );

        // Assert
        assertEquals(
                "The room is unavailable for this period",
                exception.getMessage()
        );
        verify(bookingRepository).existsOverlappingBooking(roomId, period);
        verify(bookingRepository, never()).save(any());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void shouldCreateAndSaveBookingWhenRequestIsValid() {
        // Arrange
        when(roomRepository.findById(roomId))
                .thenReturn(Optional.of(activeRoom()));

        when(bookingRepository.existsOverlappingBooking(roomId, period))
                .thenReturn(false);

        when(bookingRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Booking booking = useCase.execute(roomId, period, attendees);

        // Assert
        assertNotNull(booking.getId());
        assertEquals(roomId, booking.getRoomId());
        assertEquals(period, booking.getPeriod());
        assertEquals(attendees, booking.getAttendees());
        verify(bookingRepository).existsOverlappingBooking(roomId, period);
        verify(bookingRepository).save(booking);
        verifyNoMoreInteractions(bookingRepository);
    }
}
