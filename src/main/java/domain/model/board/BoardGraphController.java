package domain.model.board;

import domain.model.exceptions.AdjacentNodeAlreadyClaimed;
import domain.model.exceptions.IllegalEdgeClaim;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import java.util.List;
import java.util.Set;

/**
 * Controls player interactions with the board graph during gameplay.
 */
public class BoardGraphController {
  private BoardGraph boardGraph;

  BoardGraphController(BoardGraph b) {
    this.boardGraph = b;
  }

  void playerClaimStoredNodeSetupPhase(PlayerColor color, int nodeId) {
    if (boardGraph.checkIfAdjacentNodesNotClaimed(nodeId)) {
      boardGraph.claimGraphNodeObject(color, nodeId);
    } else {
      throw new AdjacentNodeAlreadyClaimed(
          "Can not claim node adjacent to node already claimed");
    }
  }

  boolean playerClaimStoredEdgeSetupPhase(
      PlayerColor color, int nodeId, int startingNodeId, int endingNodeId) {
    checkPlayerOwnsNode(color, nodeId);
    Set<GraphEdge> validEdgesToClaim = boardGraph.getConnectingEdgesById(nodeId);
    try {
      boardGraph.getMatchingEdgeFromSet(validEdgesToClaim, startingNodeId, endingNodeId);
    } catch (IllegalArgumentException e) {
      throw new IllegalEdgeClaim("Edge must be adjacent to just placed settlement");
    }
    boardGraph.claimGraphEdgeObject(color, startingNodeId, endingNodeId);
    return true;
  }

  private void checkPlayerOwnsNode(PlayerColor color, int nodeId) {
    if (!boardGraph.checkPlayerOwnsGraphNodeObject(color, nodeId)) {
      throw new IllegalEdgeClaim(
          "During setup phase, player must own node next to edge they want to claim");
    }
  }

  void playerClaimStoredNode(PlayerColor color, int nodeId) {
    handleCheckNodeIsUnoccupied(nodeId);
    handleCheckAdjacentNodesNotClaimed(nodeId);
    nodeHandleCheckPlayerOwnsNeighboringEdge(color, nodeId);
    boardGraph.claimGraphNodeObject(color, nodeId);
  }

  private void handleCheckNodeIsUnoccupied(int nodeId) {
    if (boardGraph.checkNodeOccupied(nodeId)) {
      throw new IllegalSettlementPlacementException("Node already claimed");
    }
  }

  private void nodeHandleCheckPlayerOwnsNeighboringEdge(PlayerColor color, int nodeId) {
    if (!boardGraph.nodeCheckPlayerOwnsNeighboringEdge(color, nodeId)) {
      throw new IllegalSettlementPlacementException(
          "Must own an adjacent road to claim node");
    }
  }

  private void handleCheckAdjacentNodesNotClaimed(int nodeId) {
    if (!boardGraph.checkIfAdjacentNodesNotClaimed(nodeId)) {
      throw new IllegalSettlementPlacementException(
          "Can not claim node adjacent to node already claimed");
    }
  }

  void playerClaimStoredEdge(PlayerColor color, int startingNodeId, int endingNodeId) {
    handleCheckEdgeIsUnoccupied(startingNodeId, endingNodeId);
    edgeHandleCheckPlayerOwnsNeighboringEdge(color, startingNodeId, endingNodeId);
    boardGraph.claimGraphEdgeObject(color, startingNodeId, endingNodeId);
  }

  private void handleCheckEdgeIsUnoccupied(int startingNodeId, int endingNodeId) {
    if (boardGraph.checkEdgeOccupied(startingNodeId, endingNodeId)) {
      throw new IllegalEdgeClaim("Edge already claimed");
    }
  }

  private void edgeHandleCheckPlayerOwnsNeighboringEdge(
      PlayerColor color, int startingNodeId, int endingNodeId) {
    if (!boardGraph.edgeCheckPlayerOwnsNeighboringEdge(color, startingNodeId, endingNodeId)) {
      throw new IllegalEdgeClaim("Edge must be adjacent to an owned structure");
    }
  }

  PlayerColor calculateLongestRoad(List<Player> players, PlayerColor previousWinner) {
    return boardGraph.calculateLongestRoad(players, previousWinner);
  }

  PlayerColor getEdgeOwner(int nodeId1, int nodeId2) {
    return boardGraph.getEdgeOwner(nodeId1, nodeId2);
  }

  /**
   * Returns whether the specified edge is occupied by a road.
   */
  public boolean checkEdgeOccupied(int nodeId1, int nodeId2) {
    return boardGraph.checkEdgeOccupied(nodeId1, nodeId2);
  }
}
