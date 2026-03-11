import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        licensePlate = null;
        occupied = false;
    }
}

class ParkingLot {

    private ParkingSpot[] table;
    private int size;
    private int occupiedSpots = 0;
    private int totalProbes = 0;
    private int totalParks = 0;

    public ParkingLot(int capacity) {
        size = capacity;
        table = new ParkingSpot[size];

        for (int i = 0; i < size; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % size;
    }

    // Park vehicle using linear probing
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % size;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedSpots++;
        totalProbes += probes;
        totalParks++;

        System.out.println("parkVehicle(\"" + plate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].occupied) {

            if (plate.equals(table[index].licensePlate)) {

                long exitTime = System.currentTimeMillis();
                long durationMs = exitTime - table[index].entryTime;

                double hours = durationMs / (1000.0 * 60 * 60);
                double fee = hours * 5; // $5 per hour

                table[index].occupied = false;
                table[index].licensePlate = null;

                occupiedSpots--;

                System.out.printf("exitVehicle(\"%s\") → Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        plate, index, hours, fee);
                return;
            }

            index = (index + 1) % size;
        }

        System.out.println("Vehicle not found.");
    }

    // Statistics
    public void getStatistics() {

        double occupancy = (occupiedSpots * 100.0) / size;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.printf("\nParking Statistics:\n");
        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Average Probes: %.2f\n", avgProbes);
    }
}

public class weekly {

    public static void main(String[] args) throws InterruptedException {

        ParkingLot lot = new ParkingLot(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000); // simulate parking time

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}