package domain.model.player;

import domain.model.resources.Resource;
import domain.model.resources.ResourceQuantity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages active trade offers between players.
 */
public class TradeManager {

  private final List<TradeOffer> offers = new ArrayList<>();

  /**
   * Adds a trade offer to the active list.
   *
   * @param offer the offer to add
   */
  public void offerTrade(TradeOffer offer) {
    offers.add(offer);
  }

  /**
   * Removes all active trade offers.
   */
  public void clearOffers() {
    offers.clear();
  }

  /**
   * Returns an unmodifiable view of the active trade offers.
   *
   * @return the list of active offers
   */
  public List<TradeOffer> listTrades() {
    return Collections.unmodifiableList(offers);
  }

  /**
   * Executes the given trade offer when accepted by the specified player.
   *
   * @param offer           the offer to accept
   * @param acceptingPlayer the player accepting the offer
   */
  public void acceptTrade(TradeOffer offer, Player acceptingPlayer) {
    Player offerer = offer.getOfferingPlayer();

    validateAcceptTradeInput(offer, offerer, acceptingPlayer);

    ResourceQuantity giving = offer.getGiving();
    ResourceQuantity receiving = offer.getReceiving();

    validateSufficientResources(offerer, acceptingPlayer, giving, receiving);
    executeTrade(offerer, acceptingPlayer, giving, receiving);

    offers.remove(offer);
  }

  private void validateAcceptTradeInput(
      TradeOffer offer, Player offerer, Player acceptingPlayer) {
    if (offerer == acceptingPlayer) {
      throw new IllegalArgumentException("A player cannot accept their own trade.");
    }
    if (!offers.contains(offer)) {
      throw new IllegalArgumentException("Trade not found.");
    }
  }

  private void validateSufficientResources(
      Player offerer, Player acceptingPlayer,
      ResourceQuantity giving, ResourceQuantity receiving) {
    if (offerer.getResourceCount(giving.getResource()) < giving.getQuantity()) {
      throw new IllegalStateException("Offering player has insufficient resources.");
    }
    if (acceptingPlayer.getResourceCount(receiving.getResource()) < receiving.getQuantity()) {
      throw new IllegalStateException("Accepting player has insufficient resources.");
    }
  }

  private void executeTrade(
      Player offerer, Player acceptingPlayer,
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
