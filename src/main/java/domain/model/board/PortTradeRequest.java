package domain.model.board;

import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;
import java.util.Map;

/**
 * Encapsulates the parameters for a port trade request.
 * Note that BVA is not required for this class, as it is just a DTO
 */
public class PortTradeRequest {
  private final Resource givingResource;
  private final Resource receivingResource;
  private final Map<Resource, ResourceDeck> decks;

  /**
   * Constructs a PortTradeRequest.
   *
   * @param givingResource    the resource the player is giving
   * @param receivingResource the resource the player wants to receive
   * @param decks             the map of resources to their corresponding bank decks
   */
  public PortTradeRequest(Resource givingResource, Resource receivingResource,
                          Map<Resource, ResourceDeck> decks) {
    this.givingResource = givingResource;
    this.receivingResource = receivingResource;
    this.decks = decks;
  }

  /**
   * Returns the resource being given by the player.
   *
   * @return the giving resource
   */
  public Resource getGivingResource() {
    return givingResource;
  }

  /**
   * Returns the resource being received by the player.
   *
   * @return the receiving resource
   */
  public Resource getReceivingResource() {
    return receivingResource;
  }

  /**
   * Returns the map of resources to bank decks.
   *
   * @return the decks map
   */
  public Map<Resource, ResourceDeck> getDecks() {
    return decks;
  }
}