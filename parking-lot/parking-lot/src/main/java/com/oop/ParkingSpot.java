import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingSpot {
    private final String id;
    private final SpotType type;
    private final AtomicBoolean occupied = new AtomicBoolean(false);

    public boolean reserve() {
        // succeeds only if it was previously FREE
        return occupied.compareAndSet(false, true);
    }

    public void free() {
        occupied.set(false);
    }

    public boolean isAvailable() {
        return !occupied.get();
    }

    // getters, equals/hashCode, etc.
}
