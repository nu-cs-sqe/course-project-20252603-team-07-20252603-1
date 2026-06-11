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
                               ResourceDeck givingDeck, ResourceDeck receivingDeck) throws EmptyDeckException {
    for (int nodeId : nodeIds) {
      if (board.checkPlayerOwnsNode(player.getColor(), nodeId)) {
        break;
      }
    }

    if (givingResource != resource && resource != Resource.ANY) {
      throw new IllegalArgumentException(
              "This port only accepts " + resource + " for " + tradeRatio +":1 trades.");
    }

    if (givingResource == receivingResource){
      throw new IllegalArgumentException("Cannot trade a resource for itself.");
    }

    int playerResourceCount = player.getResourceCount(givingResource);
    if (playerResourceCount < tradeRatio) {
      throw new IllegalStateException("Player has insufficient resources for this trade.");
    }

    player.updateResources(givingResource, -tradeRatio);
    givingDeck.replenish(tradeRatio);
    receivingDeck.draw();
    player.updateResources(receivingResource, 1);
  }
}
