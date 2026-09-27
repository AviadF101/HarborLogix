package harborlogix.cargo;

import harborlogix.clients.Client;
import harborlogix.ops.TariffPolicy;

/**
 * SKELETON.  Level 3 of the hierarchy.
 *
 * Extra state : targetTempC (must be 8.0 or below), powerDrawKw (positive)
 * Daily fee   : the parent's fee PLUS (POWER_RATE * powerDrawKw)
 *               You must EXTEND the parent's fee, not recompute it.
 * Category    : "Reefer"
 */
public class RefrigeratedContainer extends StandardContainer {

    private static final double MAX_TEMP_C = 8.0;

    private final double targetTempC;
    private final double powerDrawKw;

    public RefrigeratedContainer(String unitId, Client owner, double weightKg,
                                 int daysStored, double volumeM3,
                                 double targetTempC, double powerDrawKw) {
        super(unitId, owner, weightKg, daysStored, volumeM3);
        if (targetTempC > MAX_TEMP_C) {
            throw new IllegalArgumentException(
                    "Reefer target temperature must be " + MAX_TEMP_C
                            + " C or below, got " + targetTempC);
        }
        if (powerDrawKw <= 0) {
            throw new IllegalArgumentException("Power draw must be positive, got " + powerDrawKw);
        }
        this.targetTempC = targetTempC;
        this.powerDrawKw = powerDrawKw;
    }

    public double getTargetTempC() {
        return targetTempC;
    }

    public double getPowerDrawKw() {
        return powerDrawKw;
    }

    @Override
    public double dailyStorageFee() {
        // Extend the parent's fee - do not recompute the base rate here.
        return super.dailyStorageFee() + TariffPolicy.POWER_RATE * powerDrawKw;
    }

    @Override
    public String handlingCategory() {
        return "Reefer";
    }

    @Override
    public String safetyBriefing() {
        return String.format(
                "Reefer - keep power connected, target %.1f C, draw %.1f kW.", targetTempC, powerDrawKw);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" temp=%.1f C power=%.1f kW", targetTempC, powerDrawKw);
    }
}
