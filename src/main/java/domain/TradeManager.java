package domain;

import java.util.ArrayList;
import java.util.List;

public class TradeManager {
    private final List<TradeOffer> offers = new ArrayList<>();

    public void offerTrade(TradeOffer offer) {
        offers.add(offer);
    }

    public void clearOffers() {
        offers.clear();
    }

    public List<TradeOffer> listTrades() {
        return offers;
    }

    public void acceptTrade(TradeOffer offer, Player acceptingPlayer) {
        Player offerer = offer.getOfferingPlayer();
        if (offerer == acceptingPlayer) {
            throw new IllegalArgumentException("A player cannot accept their own trade.");
        }
        if (!offers.contains(offer)) {
            throw new IllegalArgumentException("Trade not found.");
        }

        ResourceQuantity giving = offer.getGiving();
        ResourceQuantity receiving = offer.getReceiving();

        Resource givingResource = giving.getResource();
        int givingQuantity = giving.getQuantity();

        Resource receivingResource = receiving.getResource();
        int receivingQuantity = receiving.getQuantity();

        if (offerer.getResourceCount(givingResource) < givingQuantity) {
            throw new IllegalStateException("Offering player has insufficient resources.");
        }
        if (acceptingPlayer.getResourceCount(receivingResource) < receivingQuantity) {
            throw new IllegalStateException("Accepting player has insufficient resources.");
        }

        offerer.updateResources(givingResource, -givingQuantity);
        offerer.updateResources(receivingResource, receivingQuantity);
        acceptingPlayer.updateResources(receivingResource, -receivingQuantity);
        acceptingPlayer.updateResources(givingResource, givingQuantity);

        offers.remove(offer);
    }
}