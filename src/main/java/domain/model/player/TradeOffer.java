package domain.model.player;

import domain.model.resources.ResourceQuantity;

/**
 * Represents a trade proposal from one player to others.
 */
public class TradeOffer {

  private final Player offeringPlayer;
  private final ResourceQuantity giving;
  private final ResourceQuantity receiving;

  private TradeOffer(
      Player offeringPlayer, ResourceQuantity giving, ResourceQuantity receiving) {
    this.offeringPlayer = offeringPlayer;
    this.giving = giving;
    this.receiving = receiving;
  }

  /**
   * Creates a new trade offer with validation.
   *
   * @param offeringPlayer the player making the offer
   * @param giving         the resource the offerer gives
   * @param receiving      the resource the offerer wants in return
   * @return a new TradeOffer
   */
  public static TradeOffer create(
      Player offeringPlayer, ResourceQuantity giving, ResourceQuantity receiving) {
    if (giving.getResource() == receiving.getResource()) {
      throw new IllegalArgumentException("Cannot trade a resource for itself.");
    }
    return new TradeOffer(offeringPlayer, giving, receiving);
  }

  /**
   * Returns the player who made this offer.
   *
   * @return the offering player
   */
  Player getOfferingPlayer() {
    return offeringPlayer;
  }

  /**
   * Returns the resource quantity the offerer is giving.
   *
   * @return the giving resource quantity
   */
  public ResourceQuantity getGiving() {
    return giving;
  }

  /**
   * Returns the resource quantity the offerer wants to receive.
   *
   * @return the receiving resource quantity
   */
  public ResourceQuantity getReceiving() {
    return receiving;
  }
}
