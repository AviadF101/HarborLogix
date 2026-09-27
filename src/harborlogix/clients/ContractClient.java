package harborlogix.clients;

/**
 * SKELETON. A client with a negotiated discount between 0 and 40 percent.
 * Reject anything outside that range with IllegalArgumentException.
 */
public class ContractClient extends Client {

    private static final double MAX_DISCOUNT = 40.0;

    private final double contractDiscount;

    public ContractClient(String clientId, String name, double contractDiscount) {
        super(clientId, name);
        if (contractDiscount < 0 || contractDiscount > MAX_DISCOUNT) {
            throw new IllegalArgumentException(
                    "Contract discount must be between 0 and " + MAX_DISCOUNT
                            + ", got " + contractDiscount);
        }
        this.contractDiscount = contractDiscount;
    }

    @Override
    public double discountPercent() {
        return contractDiscount;
    }

    @Override
    public String clientTier() {
        return "Contract";
    }

    @Override
    public String toString() {
        return super.toString() + " contract=" + contractDiscount + "%";
    }
}
