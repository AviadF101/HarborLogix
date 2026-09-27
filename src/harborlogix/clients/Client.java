package harborlogix.clients;

/**
 * SKELETON - implement the TODOs.
 *
 * DESIGN DECISION YOU MUST MAKE AND JUSTIFY IN DESIGN.md:
 * Should Client be abstract, like CargoUnit? Or concrete?
 * Both answers are defensible. Pick one, implement it, and defend it.
 * (The skeleton is concrete; change it if you decide otherwise.)
 */
public class Client {

    private final String clientId;
    private final String name;

    public Client(String clientId, String name) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("Client ID cannot be empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Client name cannot be empty");
        }
        this.clientId = clientId;
        this.name = name;
    }

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    /** Walk-in clients get no discount. Subclasses may override. */
    public double discountPercent() {
        return 0.0;
    }

    public String clientTier() {
        return "Walk-in";
    }

    /** Priority clients are unloaded first. */
    public boolean priorityHandling() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[%s, %s, %.1f%% discount]",
                clientTier(), clientId, name, discountPercent());
    }
}
