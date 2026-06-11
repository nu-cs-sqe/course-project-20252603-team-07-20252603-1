package domain.model.player;
import domain.model.resources.ResourceQuantity;

public class TradeOffer {
    private final Player offeringPlayer;
    private final ResourceQuantity giving;
    private final ResourceQuantity receiving;

    private TradeOffer(Player offeringPlayer, ResourceQuantity giving, ResourceQuantity receiving) {
        this.offeringPlayer = offeringPlayer;
        this.giving = giving;
        this.receiving = receiving;
    }

    public static TradeOffer create(Player offeringPlayer, ResourceQuantity giving, ResourceQuantity receiving) {
        if (giving.getResource() == receiving.getResource()) {
            throw new IllegalArgumentException("Cannot trade a resource for itself.");
        }
        return new TradeOffer(offeringPlayer, giving, receiving);
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
