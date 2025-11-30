public interface ParkingService {

    Map<Integer, List<ParkingSpot>> getAvailableSpots();

    ParkingSpot findSpotForVehicle(Vehicle vehicle);

    BookingResult bookSpot(Vehicle vehicle, ParkingSpot spot);

    BookingResult bookAnySpot(Vehicle vehicle);

    BookingResult bookSpotOnFloor(Vehicle vehicle, int floor);
}
