package domain.model.board;

import domain.model.exceptions.EmptyDeckException;
import domain.model.player.Player;
import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;
import java.util.List;

/**
 * Represents a port on the Catan board that allows players
 * to trade resources at a favorable ratio with the bank.
 */
public class Port {
  private static final int RECEIVE_AMOUNT = 1;

  private final int tradeRatio;
  private final Resource resource;
  private final List<Integer> nodeIds;

  /**
   * Constructs a Port with a given trade ratio, resource, and adjacent nodes.
   *
   * @param tradeRatio the trade ratio (2 or 3)
   * @param resource   the specific resource for 2:1 ports, ANY for 3:1 ports
   * @param nodeIds    the node IDs adjacent to this port
   */
  public Port(int tradeRatio, Resource resource, List<Integer> nodeIds) {
    this.tradeRatio = tradeRatio;
    this.resource = resource;
    this.nodeIds = List.copyOf(nodeIds);
  }

  /**
   * Returns the trade ratio of this port (2 or 3).
   *
   * @return the trade ratio
   */
  public int getTradeRatio() {
    return tradeRatio;
  }

  /**
   * Returns the resource this port trades, or {@link Resource#ANY} for 3:1 ports.
   *
   * @return the port's resource
   */
  public Resource getResource() {
    return resource;
  }

  /**
   * Returns the IDs of the two coastal nodes this port is attached to.
   *
   * @return an immutable list of the port's node IDs
   */
  public List<Integer> getNodeIds() {
    return nodeIds;
  }

  /**
   * Checks if a player can use this port based on node ownership.
   *
   * @param board  the board handler to check node ownership
   * @param player the player attempting to use the port
   * @return true if the player owns a node adjacent to this port
   */
  public boolean playerCanUsePort(BoardHandler board, Player player) {
    for (int nodeId : nodeIds) {
      if (board.checkPlayerOwnsNode(player.getColor(), nodeId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Executes a port trade for a player.
   *
   * @param player  the player making the trade
   * @param board   the board handler to verify port access
   * @param request the trade request containing giving/receiving resources and decks
   * @throws EmptyDeckException if the bank has insufficient resources
   */
  public void executePortTrade(Player player, BoardHandler board, PortTradeRequest request)
          throws EmptyDeckException {
    if (!playerCanUsePort(board, player)) {
      throw new IllegalStateException("Player does not have access to this port.");
    }
    validateTradeResources(request.getGivingResource(), request.getReceivingResource());
    validatePlayerResources(player, request.getGivingResource());
    performTrade(player, request);
  }

  private void validateTradeResources(Resource givingResource, Resource receivingResource) {
    if (givingResource == receivingResource) {
      throw new IllegalArgumentException("Cannot trade a resource for itself.");
    }
    if (resource != Resource.ANY && givingResource != resource) {
      throw new IllegalArgumentException(
              "This port only accepts " + resource + " for " + tradeRatio + ":1 trades.");
    }
  }

  private void validatePlayerResources(Player player, Resource givingResource) {
    if (player.getResourceCount(givingResource) < tradeRatio) {
      throw new IllegalStateException("Player has insufficient resources for this trade.");
    }
  }

  private void performTrade(Player player, PortTradeRequest request)
          throws EmptyDeckException {
    Resource givingResource = request.getGivingResource();
    Resource receivingResource = request.getReceivingResource();
    ResourceDeck givingDeck = request.getDecks().get(givingResource);
    ResourceDeck receivingDeck = request.getDecks().get(receivingResource);

    validateDeckHasResources(receivingDeck);

    player.updateResources(givingResource, -tradeRatio);
    givingDeck.replenish(tradeRatio);
    receivingDeck.draw();
    player.updateResources(receivingResource, RECEIVE_AMOUNT);
  }

  private void validateDeckHasResources(ResourceDeck deck) throws EmptyDeckException {
    if (deck.getTotalCards() <= 0) {
      throw new EmptyDeckException("Bank has insufficient resources for this trade.");
    }
  }
}