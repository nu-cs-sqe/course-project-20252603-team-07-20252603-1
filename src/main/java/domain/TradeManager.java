package domain;

import java.util.ArrayList;
import java.util.Collections;
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
        return Collections.unmodifiableList(offers);
    }

    public void acceptTrade(TradeOffer offer, Player acceptingPlayer) {
        Player offerer = offer.getOfferingPlayer();

        validateAcceptTradeInput(offer, offerer, acceptingPlayer);

        ResourceQuantity giving = offer.getGiving();
        ResourceQuantity receiving = offer.getReceiving();

        validateSufficientResources(offerer, acceptingPlayer, giving, receiving);
        executeTrade(offerer, acceptingPlayer, giving, receiving);

        offers.remove(offer);
    }

    private void validateAcceptTradeInput(TradeOffer offer, Player offerer, Player acceptingPlayer) {
        if (offerer == acceptingPlayer) {
            throw new IllegalArgumentException("A player cannot accept their own trade.");
        }
        if (!offers.contains(offer)) {
            throw new IllegalArgumentException("Trade not found.");
        }
    }

    private void validateSufficientResources(Player offerer, Player acceptingPlayer,
            ResourceQuantity giving, ResourceQuantity receiving) {
        if (offerer.getResourceCount(giving.getResource()) < giving.getQuantity()) {
            throw new IllegalStateException("Offering player has insufficient resources.");
        }
        if (acceptingPlayer.getResourceCount(receiving.getResource()) < receiving.getQuantity()) {
            throw new IllegalStateException("Accepting player has insufficient resources.");
        }
    }

    private void executeTrade(Player offerer, Player acceptingPlayer,
            ResourceQuantity giving, ResourceQuantity receiving) {
        Resource givingResource = giving.getResource();
        int givingQuantity = giving.getQuantity();

        Resource receivingResource = receiving.getResource();
        int receivingQuantity = receiving.getQuantity();

        offerer.updateResources(givingResource, -givingQuantity);
        offerer.updateResources(receivingResource, receivingQuantity);
        acceptingPlayer.updateResources(receivingResource, -receivingQuantity);
        acceptingPlayer.updateResources(givingResource, givingQuantity);
    }
}
