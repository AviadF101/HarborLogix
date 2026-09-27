package harborlogix.cargo;

import harborlogix.clients.Client;
import harborlogix.ops.TariffPolicy;

/**
 * SKELETON.  A DIRECT child of CargoUnit - a separate branch from the
 * container family. Do not make it extend StandardContainer.
 *
 * Extra state : capacityLitres (positive), fillPercent (0-100, mutable)
 * currentLitres() : capacityLitres * fillPercent / 100
 * Daily fee   : LIQUID_RATE * currentLitres()   (actual content, not capacity)
 * Category    : "Tank"
 *
 * transferOut() exists ONLY here. It is the method you will use in Part C
 * to demonstrate the one downcast this assignment permits.
 */
public class LiquidTank extends CargoUnit {

    private final double capacityLitres;
    private double fillPercent;

    public LiquidTank(String unitId, Client owner, double weightKg,
                      int daysStored, double capacityLitres, double fillPercent) {
        super(unitId, owner, weightKg, daysStored);
        if (capacityLitres <= 0) {
            throw new IllegalArgumentException(
                    "Capacity must be positive, got " + capacityLitres);
        }
        if (fillPercent < 0 || fillPercent > 100) {
            throw new IllegalArgumentException(
                    "Fill percent must be between 0 and 100, got " + fillPercent);
        }
        this.capacityLitres = capacityLitres;
        this.fillPercent = fillPercent;
    }

    public double getCapacityLitres() {
        return capacityLitres;
    }

    public double getFillPercent() {
        return fillPercent;
    }

    public double currentLitres() {
        return capacityLitres * fillPercent / 100.0;
    }

    /**
     * Pumps out up to `litres` and returns how much was actually moved
     * (never more than is present). Updates fillPercent accordingly.
     * Reject a non-positive request with IllegalArgumentException.
     */
    public double transferOut(double litres) {
        if (litres <= 0) {
            throw new IllegalArgumentException("Pumped amount must be positive, got " + litres);
        }
        double present = currentLitres();
        double moved = Math.min(litres, present);
        fillPercent -= (moved / capacityLitres) * 100.0;
        return moved;
    }

    @Override
    public double dailyStorageFee() {
        // Charged on ACTUAL content, not on capacity.
        return TariffPolicy.LIQUID_RATE * currentLitres();
    }

    @Override
    public String handlingCategory() {
        return "Tank";
    }

    @Override
    public String safetyBriefing() {
        return String.format("Liquid tank - %.1f%% full, verify vents before transfer.",
                fillPercent);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" capacity=%.1f L fill=%.1f%%",
                capacityLitres, fillPercent);
    }
}
