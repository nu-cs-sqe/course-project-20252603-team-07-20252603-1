package domain;

public class TradeOffer {
    public TradeOffer(Player offeringPlayer, ResourceQuantity giving, ResourceQuantity receiving) {
        if (giving.getResource() == receiving.getResource()) {
            throw new IllegalArgumentException("Cannot trade a resource for itself.");
        }
    }
}
