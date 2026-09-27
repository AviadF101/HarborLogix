package harborlogix.cargo;

import harborlogix.clients.Client;
import harborlogix.ops.TariffPolicy;

/**
 * Part D - the port added a service for oversized loads.
 *
 * This class was ADDED without touching any existing file: Yard already
 * knew how to store "a cargo unit" and which questions to ask it, so a new
 * kind of unit simply answers them.
 *
 * Extra state : lengthM (must exceed 12.0), needsHeavyCrane
 * Daily fee   : OVERSIZE_DAILY_FLAT + (lengthM * 5.0)
 * Category    : "Oversized"
 */
public class OversizedCargo extends CargoUnit {

    private static final double MIN_LENGTH_M = 12.0;
    private static final double LENGTH_RATE_PER_M = 5.0;

    private final double lengthM;
    private final boolean needsHeavyCrane;

    public OversizedCargo(String unitId, Client owner, double weightKg,
                          int daysStored, double lengthM, boolean needsHeavyCrane) {
        super(unitId, owner, weightKg, daysStored);
        if (lengthM <= MIN_LENGTH_M) {
            throw new IllegalArgumentException(
                    "Oversized cargo must exceed " + MIN_LENGTH_M + " m, got " + lengthM);
        }
        this.lengthM = lengthM;
        this.needsHeavyCrane = needsHeavyCrane;
    }

    public double getLengthM() {
        return lengthM;
    }

    public boolean isNeedsHeavyCrane() {
        return needsHeavyCrane;
    }

    @Override
    public double dailyStorageFee() {
        return TariffPolicy.OVERSIZE_DAILY_FLAT + (lengthM * LENGTH_RATE_PER_M);
    }

    @Override
    public String handlingCategory() {
        return "Oversized";
    }

    @Override
    public String safetyBriefing() {
        return String.format(
                "Oversized load %.1f m - %s.", lengthM,
                needsHeavyCrane ? "heavy crane required" : "standard crane sufficient");
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" length=%.1f m heavyCrane=%s",
                lengthM, needsHeavyCrane);
    }
}
