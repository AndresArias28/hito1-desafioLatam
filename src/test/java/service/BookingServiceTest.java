package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import san.desafiolatam.domain.Room;
import san.desafiolatam.exception.InactiveRoomException;
import san.desafiolatam.exception.InvalidBookingPeriodException;
import san.desafiolatam.exception.InvalidAttendeesException;
import san.desafiolatam.exception.RoomUnavailableException;
import san.desafiolatam.repository.BookingRepository;
import san.desafiolatam.service.BookingService;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository);

        startTime = LocalDateTime.of(
                2026,
                8,
                10,
                9,
                0
        );

        endTime = startTime.plusHours(1);
    }

    @Test
    void shouldRejectBookingWhenRoomIsInactive() {
        // Arrange
        Room room = new Room("R1", "Meeting Room", 10, false);

        // Act
        InactiveRoomException exception = assertThrows(
                InactiveRoomException.class,
                () -> bookingService.createBooking(
                        room,
                        startTime,
                        endTime,
                        5
                )
        );

        // Assert
        assertEquals("The room is inactive", exception.getMessage());

        verifyNoInteractions(bookingRepository);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5, 11, 20})
    void shouldRejectBookingWhenAttendeesAreInvalid(int attendees) {
        // Arrange
        Room room = new Room(
                "R1",
                "Meeting Room",
                10,
                true
        );

        // Act
        InvalidAttendeesException exception = assertThrows(
                InvalidAttendeesException.class,
                () -> bookingService.createBooking(
                        room,
                        startTime,
                        endTime,
                        attendees
                )
        );

        // Assert
        assertEquals(
                "The number of attendees is invalid",
                exception.getMessage()
        );

        verifyNoInteractions(bookingRepository);
    }

    @ParameterizedTest
    @MethodSource("invalidBookingPeriods")
    void shouldRejectBookingWhenPeriodIsInvalid(
            LocalDateTime invalidStartTime,
            LocalDateTime invalidEndTime
    ) {
        // Arrange
        Room room = new Room(
                "R1",
                "Meeting Room",
                10,
                true
        );

        // Act
        InvalidBookingPeriodException exception = assertThrows(
                InvalidBookingPeriodException.class,
                () -> bookingService.createBooking(
                        room,
                        invalidStartTime,
                        invalidEndTime,
                        5
                )
        );

        // Assert
        assertEquals(
                "Start time must be before end time",
                exception.getMessage()
        );

        verifyNoInteractions(bookingRepository);
    }

    static Stream<Arguments> invalidBookingPeriods() {
        LocalDateTime baseTime = LocalDateTime.of(
                2026,
                8,
                10,
                9,
                0
        );

        return Stream.of(
                Arguments.of(
                        baseTime,
                        baseTime
                ),
                Arguments.of(
                        baseTime.plusHours(1),
                        baseTime
                )
        );
    }

    @Test
    void shouldCreateAndSaveBookingWhenRequestIsValid() {
        // Arrange
        Room room = new Room("R1", "Meeting Room", 10, true);
        when(bookingRepository.existsOverlappingBooking(
                room.getId(),
                startTime,
                endTime
        )).thenReturn(false);
        when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var booking = bookingService.createBooking(room, startTime, endTime, 5);

        // Assert
        assertEquals(room, booking.getRoom());
        assertEquals(startTime, booking.getStartTime());
        assertEquals(endTime, booking.getEndTime());
        assertEquals(5, booking.getAttendees());
        verify(bookingRepository).existsOverlappingBooking(
                room.getId(),
                startTime,
                endTime
        );
        verify(bookingRepository).save(booking);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void shouldRejectBookingWhenRoomHasOverlappingBooking() {
        // Arrange
        Room room = new Room("R1", "Meeting Room", 10, true);
        when(bookingRepository.existsOverlappingBooking(
                room.getId(),
                startTime,
                endTime
        )).thenReturn(true);

        // Act
        RoomUnavailableException exception = assertThrows(
                RoomUnavailableException.class,
                () -> bookingService.createBooking(room, startTime, endTime, 5)
        );

        // Assert
        assertEquals(
                "The room is unavailable for this period",
                exception.getMessage()
        );
        verify(bookingRepository).existsOverlappingBooking(
                room.getId(),
                startTime,
                endTime
        );
        verify(bookingRepository, never()).save(any());
        verifyNoMoreInteractions(bookingRepository);
    }
}
