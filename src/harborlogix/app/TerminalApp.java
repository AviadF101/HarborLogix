package harborlogix.app;

import harborlogix.cargo.*;
import harborlogix.clients.*;
import harborlogix.ops.*;

/**
 * SKELETON - your demonstration program.
 *
 * It must print, in this order, with clear section headings:
 *
 *   1. A yard manifest holding AT LEAST one of every cargo type.
 *   2. Invoices for all three client tiers, showing the discount applied.
 *   3. The drainage round - the one place a downcast is permitted.
 *      Use pattern matching: `if (unit instanceof LiquidTank tank)`.
 *   4. Part D: receive an OversizedCargo into the SAME yard object and
 *      reprint the manifest, proving Yard needed no changes.
 *
 * Build the yard with capacity TariffPolicy.YARD_CAPACITY.
 */
public class TerminalApp {

    public static void main(String[] args) {
        System.out.println("HarborLogix terminal - student " + TariffPolicy.STUDENT_ID);
        System.out.println("Yard capacity: " + TariffPolicy.YARD_CAPACITY);
        System.out.println();

        Client walkIn = new Client("C-100", "Walk In Ltd");
        ContractClient contract = new ContractClient("C-101", "Blue Star Shipping", 25.0);
        GovernmentClient government = new GovernmentClient("C-102", "Port Authority", "AG-9");

        Yard yard = new Yard("Ashdod Terminal A", TariffPolicy.YARD_CAPACITY);

        StandardContainer standard =
                new StandardContainer("U-001", walkIn, 18000, 10, 33.0);
        RefrigeratedContainer reefer =
                new RefrigeratedContainer("U-002", contract, 21000, 7, 28.0, -18.0, 6.5);
        HazmatContainer hazmat =
                new HazmatContainer("U-003", government, 15000, 4, 24.0, 3, true);
        LiquidTank tank =
                new LiquidTank("U-004", walkIn, 26000, 12, 20000.0, 80.0);

        yard.receive(standard);
        yard.receive(reefer);
        yard.receive(hazmat);
        yard.receive(tank);

        // ---------- 1. manifest ----------
        System.out.println("== 1. YARD MANIFEST ==");
        yard.printManifest();
        System.out.println();

        // ---------- 2. invoices per client tier ----------
        System.out.println("== 2. INVOICES BY CLIENT TIER ==");
        for (Client client : new Client[]{walkIn, contract, government}) {
            System.out.printf("  %-12s %-20s discount %.1f%% -> %.2f%n",
                    client.clientTier(), client.getName(),
                    client.discountPercent(), yard.invoiceFor(client));
        }
        System.out.println();

        // ---------- 3. drainage round ----------
        // The one place in the assignment where a downcast is permitted.
        System.out.println("== 3. DRAINAGE ROUND (3000 L per tank) ==");
        for (CargoUnit unit : yard.getUnits()) {
            if (unit instanceof LiquidTank liquidTank) {
                double moved = liquidTank.transferOut(3000.0);
                System.out.printf("  %s: moved %.1f L, now %.1f%% full%n",
                        liquidTank.getUnitId(), moved, liquidTank.getFillPercent());
            }
        }
        System.out.println();
        System.out.println("  Revenue after drainage:");
        yard.printManifest();
        System.out.println();

        // ---------- 4. Part D: oversized cargo joins the SAME yard ----------
        System.out.println("== 4. PART D - OVERSIZED CARGO JOINS THE EXISTING YARD ==");
        OversizedCargo oversized =
                new OversizedCargo("U-005", contract, 45000, 3, 18.5, true);
        yard.receive(oversized);
        System.out.println("  Received " + oversized.getUnitId()
                + " - no change was needed in Yard.");
        System.out.println();
        yard.printManifest();
    }
}
