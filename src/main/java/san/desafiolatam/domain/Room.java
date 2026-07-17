package san.desafiolatam.domain;

public class Room {

    private final String id;
    private final String name;
    private final int capacity;
    private final boolean active;

    public Room(String id, String name, int capacity, boolean active) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Room capacity must be greater than zero"
            );
        }

        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.active = active;
    }

    public boolean canAccommodate(int attendees) {
        return attendees > 0 && attendees <= capacity;
    }

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public int getCapacity() {
        return capacity;
    }
}