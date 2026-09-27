package harborlogix.ops;

import harborlogix.cargo.CargoUnit;
import harborlogix.clients.Client;
import java.util.ArrayList;

/**
 * SKELETON - and the most important file in the assignment.
 *
 * THE RULE
 * --------
 * This file must NOT contain:
 *   - the name of any CargoUnit subclass
 *   - the name of any Client subclass
 *   - the word `instanceof`
 *   - any cast to a hierarchy type
 *
 * Verify with:
 *   java harborlogix.tools.OpenClosedCheck src/harborlogix/ops/Yard.java
 *
 * Yard knows there is *a* cargo unit and *a* client. It knows which questions
 * to ask them. It must not know what kind they are.
 */
public class Yard {

    private final String yardName;
    private final int capacity;
    private final ArrayList<CargoUnit> units = new ArrayList<>();

    public Yard(String yardName, int capacity) {
        if (yardName == null || yardName.isBlank()) {
            throw new IllegalArgumentException("Yard name cannot be empty");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive, got " + capacity);
        }
        this.yardName = yardName;
        this.capacity = capacity;
    }

    public String getYardName() { return yardName; }
    public int getCapacity()    { return capacity; }
    public int getUnitCount()   { return units.size(); }

    /**
     * Returns the stored units.
     * THINK: should this return the internal list, or a copy? Your choice
     * affects encapsulation, and you will be asked about it.
     */
    public ArrayList<CargoUnit> getUnits() {
        // A defensive copy: handing out the internal list would let any caller
        // add or remove units behind the Yard's back, bypassing every rule
        // enforced in receive().
        return new ArrayList<>(units);
    }

    /**
     * Adds a unit.
     * Reject null with IllegalArgumentException.
     * Reject a duplicate unit ID with IllegalArgumentException.
     * Reject exceeding capacity with IllegalStateException.
     * Think about why those two situations deserve different exception types.
     */
    public void receive(CargoUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Cannot receive a null unit");
        }
        for (CargoUnit existing : units) {
            if (existing.getUnitId().equals(unit.getUnitId())) {
                throw new IllegalArgumentException(
                        "Unit ID already in yard: " + unit.getUnitId());
            }
        }
        if (units.size() >= capacity) {
            throw new IllegalStateException(
                    "Yard is full: capacity " + capacity);
        }
        units.add(unit);
    }

    /** Sum of every unit's daily fee. */
    public double totalDailyRevenue() {
        double total = 0.0;
        for (CargoUnit unit : units) {
            total += unit.dailyStorageFee();
        }
        return total;
    }

    /**
     * Total owed by one client: sum of totalStorageCharge() over that client's
     * units, reduced by that client's own discount percentage.
     * Match clients by clientId, not by object identity.
     */
    public double invoiceFor(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Cannot invoice a null client");
        }
        double gross = 0.0;
        for (CargoUnit unit : units) {
            if (unit.getOwner().getClientId().equals(client.getClientId())) {
                gross += unit.totalStorageCharge();
            }
        }
        return gross * (1.0 - client.discountPercent() / 100.0);
    }

    /** The heaviest unit in the yard, or null if the yard is empty. */
    public CargoUnit heaviestUnit() {
        CargoUnit heaviest = null;
        for (CargoUnit unit : units) {
            if (heaviest == null || unit.getWeightKg() > heaviest.getWeightKg()) {
                heaviest = unit;
            }
        }
        return heaviest;
    }

    /** Prints one line per unit plus its safety briefing, then the daily revenue. */
    public void printManifest() {
        System.out.println("Manifest for " + yardName
                + " (" + units.size() + "/" + capacity + " units)");
        for (CargoUnit unit : units) {
            System.out.println("  " + unit);
            System.out.println("      " + unit.safetyBriefing());
        }
        System.out.printf("  Daily revenue: %.2f%n", totalDailyRevenue());
    }
}
