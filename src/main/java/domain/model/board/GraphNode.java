package domain.model.board;

import domain.model.exceptions.IllegalNodeIdException;
import domain.model.player.PlayerColor;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/** Represents a node (intersection) in the board graph. */
public class GraphNode {
  private final int nodeId;
  private boolean occupied;
  private PlayerColor owningPlayerColor;

  private static final int MAX_NODE_ID = 53;
  private static final int MIN_NODE_ID = 0;

  @SuppressFBWarnings(
      value = "CT_CONSTRUCTOR_THROW",
      justification = "Validation in constructor is intentional; no finalizer risk")
  GraphNode(int nodeId) {
    assertValidNodeId(nodeId);
    this.nodeId = nodeId;
    this.occupied = false;
    this.owningPlayerColor = PlayerColor.SETUP;
  }

  private void assertValidNodeId(int nodeId) {
    if (nodeId < MIN_NODE_ID || nodeId > MAX_NODE_ID) {
      throw new IllegalNodeIdException("Requested nodeId number illegal");
    }
  }

  void playerClaimNode(PlayerColor color) {
    if (checkOccupied()) {
      throw new IllegalArgumentException("Node Already Claimed");
    } else {
      this.occupied = true;
      this.owningPlayerColor = color;
    }
  }

  boolean checkOccupied() {
    return this.occupied;
  }

  PlayerColor checkColor() {
    return this.owningPlayerColor;
  }

  int getNodeId() {
    return this.nodeId;
  }
}
