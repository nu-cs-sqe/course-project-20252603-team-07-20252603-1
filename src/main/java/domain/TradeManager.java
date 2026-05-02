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

        if (offerer.getResourceCount(giving.getResource()) < giving.getQuantity()) {
            throw new IllegalStateException("Offering player has insufficient resources.");
        }
        if (acceptingPlayer.getResourceCount(receiving.getResource()) < receiving.getQuantity()) {
            throw new IllegalStateException("Accepting player has insufficient resources.");
        }

        offers.remove(offer);

        offerer.updateResources(giving.getResource(), -giving.getQuantity());
        offerer.updateResources(receiving.getResource(), receiving.getQuantity());
        acceptingPlayer.updateResources(receiving.getResource(), -receiving.getQuantity());
        acceptingPlayer.updateResources(giving.getResource(), giving.getQuantity());
    }
}
