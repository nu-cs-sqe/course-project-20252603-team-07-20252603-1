package domain.model.board;

/**
 * Represents a vertex (corner) on the game board.
 */
public class Vertex {
  boolean isOccupied() {
    return false;
  }

  boolean hasAdjacentSettlementViolatingDistanceRule() {
    return false;
  }
}
