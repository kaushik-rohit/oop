
public class ParkingManager implements ParkingService {
    private final ParkingLot parkingLot;
    private final SpotAssignmentStrategy spotAssignmentStrategy;
    private final PricingStrategy pricingStrategy;
    private final ConcurrentMap<String, Ticket> activeTickets = new ConcurrentHashMap<>();

    
}