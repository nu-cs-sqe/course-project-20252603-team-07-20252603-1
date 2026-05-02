package domain;

public class TradeOffer {
    private final Player offeringPlayer;
    private final ResourceQuantity giving;
    private final ResourceQuantity receiving;

    public TradeOffer(Player offeringPlayer, ResourceQuantity giving, ResourceQuantity receiving) {
        if (giving.getResource() == receiving.getResource()) {
            throw new IllegalArgumentException("Cannot trade a resource for itself.");
        }
        this.offeringPlayer = offeringPlayer;
        this.giving = giving;
        this.receiving = receiving;
    }

    public Player getOfferingPlayer() {
        return offeringPlayer;
    }

    public ResourceQuantity getGiving() {
        return giving;
    }

    public ResourceQuantity getReceiving() {
        return receiving;
    }
}
