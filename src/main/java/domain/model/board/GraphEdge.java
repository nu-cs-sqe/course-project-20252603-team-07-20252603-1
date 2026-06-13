package domain.model.board;

import domain.model.exceptions.EdgeAlreadyClaimedException;
import domain.model.exceptions.IllegalNodeOrderingInEdgeException;
import domain.model.player.PlayerColor;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents a directed edge in the board graph, connecting two nodes.
 */
public class GraphEdge {
  private final int startingNodeId;
  private final int endingNodeId;

  private boolean roadBuilt;
  private PlayerColor owningPlayerColor;

  @SuppressFBWarnings(
      value = "CT_CONSTRUCTOR_THROW",
      justification = "Validation in constructor is intentional; no finalizer risk")
  GraphEdge(int startingNodeId, int endingNodeId) {
    assertValidNodeIdsOrdering(startingNodeId, endingNodeId);
    this.startingNodeId = startingNodeId;
    this.endingNodeId = endingNodeId;
    this.roadBuilt = false;
    this.owningPlayerColor = PlayerColor.SETUP;
  }

  private void assertValidNodeIdsOrdering(int startingNodeId, int endingNodeId) {
    if (!(startingNodeId < endingNodeId)) {
      throw new IllegalNodeOrderingInEdgeException(
          "Starting nodeId must be lower than ending nodeId");
    }
  }

  boolean claimGraphEdge(PlayerColor color) {
    if (this.roadBuilt) {
      throw new EdgeAlreadyClaimedException("Edge already claimed");
    } else {
      this.roadBuilt = true;
      this.owningPlayerColor = color;
      return true;
    }
  }

  boolean checkRoadExists() {
    return this.roadBuilt;
  }

  PlayerColor checkOwningColor() {
    return this.owningPlayerColor;
  }

  int getStartingNodeId() {
    return this.startingNodeId;
  }

  int getEndingNodeId() {
    return this.endingNodeId;
  }
}
