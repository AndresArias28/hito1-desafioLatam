package san.desafiolatam.domain;

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
}
