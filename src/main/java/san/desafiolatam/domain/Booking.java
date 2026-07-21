package san.desafiolatam.domain;

import java.time.Duration;
import java.time.LocalDateTime;

public class Booking {

    private final String id;
    private final Room room;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final int attendees;

    public Booking(
            String id,
            Room room,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int attendees
    ) {
        this.id = id;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attendees = attendees;
    }

    public String getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getAttendees() {
        return attendees;
    }

    // Checks whether this booking overlaps the given time range.
    public boolean overlaps(
            LocalDateTime otherStart,
            LocalDateTime otherEnd
    ) {
        return startTime.isBefore(otherEnd) && endTime.isAfter(otherStart);
    }

    public boolean isForRoom(String roomId) {
        return room.getId().equals(roomId);
    }

    public long durationInMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }
}
