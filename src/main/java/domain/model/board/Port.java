package domain.model.board;

import domain.model.exceptions.EmptyDeckException;
import domain.model.player.Player;
import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;

import java.util.List;

public class Port {
  private static final int TWO_TO_ONE_RATIO = 2;
  private static final int THREE_TO_ONE_RATIO = 3;
  private static final int RECEIVE_AMOUNT = 1;

  private final int tradeRatio;
  private final Resource resource; // ANY if 3:1 port
  private final List<Integer> nodeIds;

  /**
   * Constructs a Port with a given trade ratio, resource, and adjacent nodes.
   *
   * @param tradeRatio the trade ratio (2 or 3)
   * @param resource   the specific resource for 2:1 ports, null for 3:1 ports
   * @param nodeIds    the node IDs adjacent to this port
   */
  public Port(int tradeRatio, Resource resource, List<Integer> nodeIds) {
    this.tradeRatio = tradeRatio;
    this.resource = resource;
    this.nodeIds = List.copyOf(nodeIds);
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

  public void executePortTrade(Player player, BoardHandler board,
                               Resource givingResource, Resource receivingResource,
                               ResourceDeck givingDeck, ResourceDeck receivingDeck)
          throws EmptyDeckException {
    validatePortAccess(player, board);
    validateTradeResources(givingResource, receivingResource);
    validatePlayerResources(player, givingResource);
    performTrade(player, givingResource, receivingResource, givingDeck, receivingDeck);
  }

  private void validatePortAccess(Player player, BoardHandler board) {
    for (int nodeId : nodeIds) {
      if (board.checkPlayerOwnsNode(player.getColor(), nodeId)) {
        return;
      }
    }
    throw new IllegalStateException("Player does not have access to this port.");
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

  private void performTrade(Player player, Resource givingResource, Resource receivingResource,
                            ResourceDeck givingDeck, ResourceDeck receivingDeck)
          throws EmptyDeckException {
    player.updateResources(givingResource, -tradeRatio);
    givingDeck.replenish(tradeRatio);

    try {
      receivingDeck.draw();
    } catch (EmptyDeckException e) {
      player.updateResources(givingResource, tradeRatio);
      givingDeck.drawMultiple(tradeRatio);
      throw e;
    }

    player.updateResources(receivingResource, 1);
  }
}
