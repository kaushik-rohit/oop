import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ParkingManager implements ParkingService {
    private final ParkingLot parkingLot;
    private final SpotAssignmentStrategy spotAssignmentStrategy;
    private final PricingStrategy pricingStrategy;
    private final ConcurrentMap<String, Ticket> activeTickets = new ConcurrentHashMap<>();


    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Map<Integer, List<ParkingSpot>> getAvailableSpots() {
        lock.readLock().lock();
        try {
            // however you compute available spots from parkingLot
            return parkingLot.getAvailableSpotsView();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public BookingResult bookAnySpot(Vehicle vehicle) {
        lock.writeLock().lock();
        try {
            ParkingSpot spot =
                spotAssignmentStrategy.findSpot(parkingLot, vehicle);

            if (spot == null) {
                return BookingResult.failure("No spot available");
            }

            // second line of defense against races
            if (!spot.reserve()) {
                return BookingResult.failure("Spot just taken, retry");
            }

            Ticket ticket = new Ticket(vehicle, spot);
            activeTickets.put(ticket.getId(), ticket);

            return BookingResult.success(ticket, spot);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public BookingResult leave(String ticketId) {
        lock.writeLock().lock();
        try {
            Ticket ticket = activeTickets.remove(ticketId);
            if (ticket == null) {
                return BookingResult.failure("Invalid ticket");
            }

            ParkingSpot spot = ticket.getSpot();
            spot.free();
            double fee = pricingStrategy.calculateFee(ticket);

            return BookingResult.success(ticket, spot, fee);
        } finally {
            lock.writeLock().unlock();
        }
    }
}